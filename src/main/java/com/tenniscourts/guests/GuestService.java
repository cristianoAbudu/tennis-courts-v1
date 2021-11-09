package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.*;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO create(CreateGuestRequestDTO createGuestRequestDTO) {

        return guestMapper.map(guestRepository.save(guestMapper.map(createGuestRequestDTO)));

    }

    public GuestDTO update(GuestDTO guestDTO) throws EntityNotFoundException {

        if (guestRepository.findById(guestDTO.getId()).isEmpty()) {
            throw new EntityNotFoundException("No guests found with given id");
        }
        return guestMapper.map(guestRepository.save(guestMapper.map(guestDTO)));

    }

    public GuestDTO findById(Long id) throws EntityNotFoundException {
        Optional<Guest> guestOptional = guestRepository.findById(id);
        if (guestOptional.isEmpty()) {
            throw new EntityNotFoundException("No guests found with given id");
        }
        return guestMapper.map(guestOptional.get());
    }

    public List<GuestDTO> findAll() {
        return guestMapper.map(guestRepository.findAll());

    }

    public void delete(Long id) throws EntityNotFoundException {
        Optional<Guest> guestOptional = guestRepository.findById(id);
        if (guestOptional.isEmpty()) {
            throw new EntityNotFoundException("No guests found with given id");
        }
        guestRepository.delete(guestOptional.get());
    }

    public List<GuestDTO> findByName(String name) {
        return guestMapper.map(guestRepository.findByName(name));

    }
}
