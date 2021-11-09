package com.tenniscourts.reservations;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @Mock
    private  GuestRepository guestRepository;

    @Mock
    private  ScheduleRepository scheduleRepository;

    @Mock
    private  ReservationMapper reservationMapper;

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void bookReservationGuestsNotFoundTest() throws NotFoundException {
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(0l,0l);

        try{
            reservationService.bookReservation(createReservationRequestDTO);
            Assert.fail();
        }catch(NotFoundException e){
            Assert.assertEquals("No guests found with given id", e.getMessage());
        }
    }

    @Test
    public void bookReservationSchedulesNotFoundTest() throws NotFoundException {
        Mockito.when(guestRepository.findById(0l)).thenReturn(Optional.of(new Guest()));

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(0l,0l);

        try{
            reservationService.bookReservation(createReservationRequestDTO);
            Assert.fail();
        }catch(NotFoundException e){
            Assert.assertEquals("No schedules found with given id", e.getMessage());
        }
    }

    @Test
    public void bookReservationSchedulesOKTest() throws NotFoundException {
        Mockito.when(guestRepository.findById(0l)).thenReturn(Optional.of(new Guest()));
        Mockito.when(scheduleRepository.findById(0l)).thenReturn(Optional.of(new Schedule()));

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(0l,0l);

        reservationService.bookReservation(createReservationRequestDTO);
    }
}