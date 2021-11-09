package com.tenniscourts.guests;

import com.tenniscourts.guests.CreateGuestRequestDTO;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.CreateGuestRequestDTO;
import com.tenniscourts.guests.Guest;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest map(GuestDTO source);

    @InheritInverseConfiguration
    GuestDTO map(Guest source);

    Guest map(CreateGuestRequestDTO source);

    List<GuestDTO> map(List<Guest> GuestList);

}
