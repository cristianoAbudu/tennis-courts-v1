package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.guests.CreateGuestRequestDTO;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.reservations.CreateReservationRequestDTO;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("private/guests")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.create(createGuestRequestDTO).getId())).build();
    }

    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody GuestDTO guestDTO) {
        try {
            return ResponseEntity.created(locationByEntity(guestService.update(guestDTO).getId())).build();
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(guestService.findById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/findByName")
    public ResponseEntity<Object> findByName(@RequestParam String name ) {
        return ResponseEntity.ok(guestService.findByName(name));
    }

    @GetMapping()
    public ResponseEntity<List<GuestDTO>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            guestService.delete(id);
            return ResponseEntity.ok("Guest deleted");
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
