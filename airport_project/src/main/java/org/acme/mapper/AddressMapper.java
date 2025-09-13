package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Address;
import org.acme.representation.AddressCreateRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@RequestScoped
public abstract class AddressMapper {
    @Mapping(target = "user", ignore = true)
    public abstract Address mapToEntity(AddressCreateRepresentation addressCreateRepresentation);
}
