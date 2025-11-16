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
import org.acme.representation.AddressRepresentation;
import org.acme.representation.user.PasswordResetRepresentation;
import org.acme.representation.user.UserCreateRepresentation;
import org.acme.representation.user.UserRepresentation;
import org.acme.representation.user.UserUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.UserContext;

import java.util.ArrayList;
import java.util.List;
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
        PageResult<User> userPageResult = userRepository.searchUsersByParams(query);
        List<UserRepresentation> userRepresentations = userPageResult.getResults().stream().map(
                user -> {
                    UserRepresentation userRepresentation = userMapper.map(user);
                    userRepresentation.setAddresses(new ArrayList<>());
                    userRepresentation.getAddresses().addAll(user.getAddresses().stream().map(address ->
                            new AddressRepresentation(address.getAddressName(), address.getCity().getCityName(),
                                    address.getCountry().getCountryName())).toList());
                    return userRepresentation;
                }
        ).toList();
        return new PageResult<>(userRepresentations.size(), userRepresentations);
    }

    @Transactional
    public void createSystemAdministrator(UserCreateRepresentation userCreateRepresentation){
        Optional<User> userOptional = userRepository.findUserByUsername(userCreateRepresentation.getUsername());
        if(userOptional.isPresent()){
            throw new InvalidRequestException(ErrorMessages.USER_EXISTS);
        }

        User user = new User(userCreateRepresentation, Role.SYSTEM_ADMIN);
        keycloakService.createKeycloakUser(userCreateRepresentation, Role.SYSTEM_ADMIN);
        userRepository.persist(user);
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

        User user = userOptional.get();
        UserRepresentation userRepresentation = userMapper.map(user);
        userRepresentation.setAddresses(new ArrayList<>());
        userRepresentation.getAddresses().addAll(user.getAddresses().stream().map(address ->
                new AddressRepresentation(address.getAddressName(),address.getCity().getCityName(),
                        address.getCountry().getCountryName())).toList());
        return userRepresentation;
    }

    private UserRepresentation persistSystemAdminUser(String username){
        String firstName = userContext.extractFirstName();
        String lastName = userContext.extractLastName();
        String email = userContext.extractEmail();
        User systemAdmin = new User(username, firstName, lastName, email);
        userRepository.persist(systemAdmin);
        UserRepresentation userRepresentation = userMapper.map(systemAdmin);
        userRepresentation.setAddresses(new ArrayList<>());
        userRepresentation.getAddresses().addAll(systemAdmin.getAddresses().stream().map(address ->
                new AddressRepresentation(address.getAddressName(),address.getCity().getCityName(),
                        address.getCountry().getCountryName())).toList());
        return userRepresentation;
    }

    @Transactional
    public void updateUserProfile(UserUpdateRepresentation userUpdateRepresentation){
        String username = userContext.extractUsername();
        this.updateUserDetails(userUpdateRepresentation, username);
    }

    @Transactional
    public void updateUserDetails(UserUpdateRepresentation userUpdateRepresentation, String username){
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        user.updateDetails(userUpdateRepresentation);

        user.getAddresses().clear();
        user.getAddresses().addAll(userUpdateRepresentation.getAddresses().stream()
                .map(a -> {
                    Address address = addressMapper.mapRepresentationToEntity(a);
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

        keycloakService.deleteUser(user.getUsername());
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
