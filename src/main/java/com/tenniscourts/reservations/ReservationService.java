package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.BusinessException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final GuestRepository guestRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) throws EntityNotFoundException {
        Reservation reservation = new Reservation();

        Optional<Guest> guestOptional = guestRepository.findById(createReservationRequestDTO.getGuestId());
        if(guestOptional.isEmpty()){
            throw new EntityNotFoundException("No guests found with given id");
        }

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(createReservationRequestDTO.getScheduleId());
        if(scheduleOptional.isEmpty()){
            throw new EntityNotFoundException("No schedules found with given id");
        }

        Guest guest = guestOptional.get();
        Schedule schedule = scheduleOptional.get();

        reservation.setGuest(guest);
        reservation.setSchedule(schedule);

        // Charge user on 10 every time he schedules
        reservation.setValue(new BigDecimal(10));

        reservationRepository.save(reservation);

        return reservationMapper.map(reservation);
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }


    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) throws EntityNotFoundException {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new BusinessException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    public ReservationDTO refund(Long reservationId) {

        Optional<Reservation> optional = reservationRepository.findById(reservationId);
        if(optional.isEmpty()){
            throw new EntityNotFoundException("No reservation found with given id");
        }

        Reservation reservation = optional.get();

        // Refund
        reservation.setRefundValue(new BigDecimal(10));

        return reservationMapper.map(reservationRepository.save(reservation));

    }
}
