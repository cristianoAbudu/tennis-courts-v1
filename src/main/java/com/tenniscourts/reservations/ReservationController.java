package com.tenniscourts.reservations;

import com.sun.xml.bind.v2.schemagen.xmlschema.Any;
import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.exceptions.BusinessException;
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

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping("/cancelReservation/{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/rescheduleReservation")
    public ResponseEntity<Object> rescheduleReservation(@RequestParam Long reservationId, @RequestParam Long scheduleId) {
        try {
            return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
        }catch (EntityNotFoundException | BusinessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/admin/refund/{reservationId}")
    public ResponseEntity<Object> refund(@PathVariable Long reservationId) {
        try {
            return ResponseEntity.ok(reservationService.refund(reservationId));
        }catch (EntityNotFoundException | BusinessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
