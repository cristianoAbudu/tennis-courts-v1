package com.tenniscourts.reservations;

import com.sun.xml.bind.v2.schemagen.xmlschema.Any;
import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping("/bookReservation")
    public ResponseEntity<Object> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        try {
            return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping("/cancelReservation/")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/rescheduleReservation")
    public ResponseEntity<Object> rescheduleReservation(@PathVariable Long reservationId, @PathVariable Long scheduleId) {
        try {
            return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
