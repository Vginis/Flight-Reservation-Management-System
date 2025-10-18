package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.Role;
import org.acme.constant.search.UserSortAndFilterBy;
import org.acme.domain.Address;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AddressMapper;
import org.acme.mapper.UserMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.user.PasswordResetRepresentation;
import org.acme.representation.user.UserRepresentation;
import org.acme.representation.user.UserUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.UserContext;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    AddressMapper addressMapper;
    @Inject
    UserMapper userMapper;
    @Inject
    AirlineRepository airlineRepository;
    @Inject
    UserContext userContext;
    @Inject
    KeycloakService keycloakService;

    public PageResult<UserRepresentation> searchUsersByParams(PageQuery<UserSortAndFilterBy> query){
        return userMapper.map(userRepository.searchUsersByParams(query));
    }

    public void resetPassword(PasswordResetRepresentation passwordResetRepresentation){
        String username = userContext.extractUsername();
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        keycloakService.updateUserPassword(username, passwordResetRepresentation.getNewPassword());
    }

    @Transactional
    public UserRepresentation getUserProfile(){
        String username = userContext.extractUsername();
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        Set<String> roles = userContext.extractRoles();

        if(userOptional.isEmpty()){
            if(!roles.contains("system_admin")){
                throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
            }
            return persistSystemAdminUser(username);
        }

        return userMapper.map(userOptional.get());
    }

    private UserRepresentation persistSystemAdminUser(String username){
        String firstName = userContext.extractFirstName();
        String lastName = userContext.extractLastName();
        String email = userContext.extractEmail();
        User systemAdmin = new User(username, firstName, lastName, email);
        userRepository.persist(systemAdmin);
        return userMapper.map(systemAdmin);
    }

    @Transactional
    public void updateUser(UserUpdateRepresentation userUpdateRepresentation){
        String username = userContext.extractUsername();
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        user.updateDetails(userUpdateRepresentation);

        user.getAddresses().clear();
        user.getAddresses().addAll(userUpdateRepresentation.getAddresses().stream()
                .map(a -> {
                    Address address = addressMapper.mapToEntity(a);
                    address.setUser(user);
                    return address;
                }).toList());
        userRepository.getEntityManager().merge(user);
    }

    @Transactional
    public void deleteUser(Integer id){
        Optional<User> userOptional = userRepository.findByIdOptional(id);
        String username = userContext.extractUsername();
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        if(username.equals(user.getUsername())){
            throw new InvalidRequestException("The system doesn't allow to delete your account.");
        }

        if (user.getRole().equals(Role.AIRLINE_ADMINISTRATOR)){
            handleAirlineAdministratorDeletion(user);
        }

        userRepository.getEntityManager().remove(user);
    }

    private void handleAirlineAdministratorDeletion(User user){
        AirlineAdministrator airlineAdministrator = (AirlineAdministrator) user;
        Airline airline = airlineAdministrator.getAirline();

        if(airline.getAdministrators().size() == 1){
            throw new InvalidRequestException(ErrorMessages.AIRLINE_HAS_SOLE_ADMIN);
        }

        airline.setAdministrators(new ArrayList<>(airline.getAdministrators().stream().filter(admin ->
                !admin.getUsername().equals(user.getUsername())).toList()));
        airlineRepository.getEntityManager().merge(airline);
    }
}
