package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.User;
import org.acme.representation.user.UserRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@RequestScoped
public interface UserMapper extends OneWayMapper<UserRepresentation, User> {
}
