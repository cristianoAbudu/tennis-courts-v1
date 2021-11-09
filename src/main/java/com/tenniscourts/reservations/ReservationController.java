package com.tenniscourts.reservations;

import com.sun.xml.bind.v2.schemagen.xmlschema.Any;
import com.tenniscourts.config.BaseRestController;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Void> get() {
        System.out.println("teste");
        return null;
    }

    @PostMapping("/bookReservation")
    public ResponseEntity<Object> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        try {
            return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<ReservationDTO> findReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    public ResponseEntity<Object> rescheduleReservation(Long reservationId, Long scheduleId) {
        try {
            return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
