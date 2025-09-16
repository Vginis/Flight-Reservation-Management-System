package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Aircraft;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class})
@RequestScoped
public interface AircraftMapper extends OneWayMapper<AircraftRepresentation, Aircraft>{
    @Override
    @Mapping(target = "airline2DigitCode", expression = "java(e.getAirline().getU2digitCode())")
    AircraftRepresentation map(Aircraft e);
}
