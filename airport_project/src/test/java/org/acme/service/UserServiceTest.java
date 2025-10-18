package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.Role;
import org.acme.constant.search.SortDirection;
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
import org.acme.util.AirlineUtil;
import org.acme.util.UserContext;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserMapper userMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    UserContext userContext;
    @Mock
    KeycloakService keycloakService;
    @Mock
    AddressMapper addressMapper;
    @Mock
    EntityManager entityManager;
    @Mock
    AirlineRepository airlineRepository;

    UserRepresentation userRepresentation;
    PasswordResetRepresentation passwordResetRepresentation;
    User user;
    UserUpdateRepresentation userUpdateRepresentation;
    Airline airline;
    AirlineAdministrator airlineAdministrator;
    @BeforeEach
    void setup(){
        userRepresentation = UserUtil.createUserRepresentation();
        passwordResetRepresentation = new PasswordResetRepresentation();
        passwordResetRepresentation.setNewPassword("new Password");
        user = UserUtil.createUser();
        userUpdateRepresentation = UserUtil.createUserUpdateRepresentation();
        airline = AirlineUtil.createAirline();
        airlineAdministrator = new AirlineAdministrator(UserUtil.createAirlineAdministratorCreateRepresentation(),
                airline);
    }

    @Test
    void test_search_by_params(){
        PageResult<User> pageResult = new PageResult<>(1, new ArrayList<>());
        PageResult<UserRepresentation> pageResultRepresentation = new PageResult<>(1, new ArrayList<>());
        Mockito.when(userRepository.searchUsersByParams(Mockito.any(PageQuery.class)))
                .thenReturn(pageResult);
        Mockito.when(userMapper.map(pageResult)).thenReturn(pageResultRepresentation);

        Assertions.assertDoesNotThrow(() -> userService.searchUsersByParams(
                new PageQuery<>(UserSortAndFilterBy.EMAIL, "value", 5, 0, UserSortAndFilterBy.EMAIL, SortDirection.ASCENDING)));
    }

    @Test
    void test_reset_password(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.of(new User()));
        Mockito.doNothing().when(keycloakService).updateUserPassword("user-1","new Password");

        Assertions.assertDoesNotThrow(() -> userService.resetPassword(passwordResetRepresentation));
    }

    @Test
    void test_reset_password_not_found_exception(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.resetPassword(passwordResetRepresentation));
    }

    @Test
    void test_get_user_profile(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.of(user));
        Mockito.when(userContext.extractRoles()).thenReturn(Set.of(Role.PASSENGER));
        Mockito.when(userMapper.map(Mockito.any(User.class))).thenReturn(userRepresentation);
        UserRepresentation dto = userService.getUserProfile();

        Assertions.assertEquals("user-1", dto.getUsername());
    }

    @Test
    void test_get_user_profile_throws_not_found_exception(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.empty());
        Mockito.when(userContext.extractRoles()).thenReturn(Set.of(Role.PASSENGER));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfile());
    }

    @Test
    void test_get_user_profile_persist_system_admin(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.empty());
        Mockito.when(userContext.extractRoles()).thenReturn(Set.of(Role.SYSTEM_ADMIN));
        Mockito.when(userContext.extractFirstName()).thenReturn("firstName");
        Mockito.when(userContext.extractLastName()).thenReturn("lastName");
        Mockito.when(userContext.extractEmail()).thenReturn("email@email.com");
        Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));
        Mockito.when(userMapper.map(Mockito.any(User.class))).thenReturn(userRepresentation);

        Assertions.assertDoesNotThrow(() -> userService.getUserProfile());
    }

    @Test
    void test_update_user(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.of(user));
        Mockito.when(addressMapper.mapToEntity(Mockito.any())).thenReturn(new Address());
        Mockito.when(userRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(User.class))).thenReturn(user);

        Assertions.assertDoesNotThrow(() -> userService.updateUser(userUpdateRepresentation));
    }

    @Test
    void test_update_user_throws_not_found_exception(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(userRepository.findUserByUsername("user-1")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userUpdateRepresentation));
    }

    @Test
    void test_delete_user(){
        Mockito.when(userRepository.findByIdOptional(1)).thenReturn(Optional.of(user));
        Mockito.when(userContext.extractUsername()).thenReturn("user-2");
        Mockito.when(userRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.doNothing().when(entityManager).remove(Mockito.any());

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1));
    }

    @Test
    void test_delete_user_throws_not_found_exception(){
        Mockito.when(userRepository.findByIdOptional(1)).thenReturn(Optional.empty());
        Mockito.when(userContext.extractUsername()).thenReturn("user-2");

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1));
    }

    @Test
    void test_delete_user_delete_your_account(){
        Mockito.when(userRepository.findByIdOptional(1)).thenReturn(Optional.of(user));
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");

        Assertions.assertThrows(InvalidRequestException.class, () -> userService.deleteUser(1));
    }

    @Test
    void test_delete_airline_admin(){
        Mockito.when(userRepository.findByIdOptional(1)).thenReturn(Optional.of(airlineAdministrator));
        Mockito.when(userContext.extractUsername()).thenReturn("user-2");
        Mockito.when(userRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(airlineRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.doNothing().when(entityManager).remove(Mockito.any());

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1));
    }

    @Test
    void test_delete_airline_admin_sole_admin(){
        airline.setAdministrators(List.of(airlineAdministrator));
        Mockito.when(userRepository.findByIdOptional(1)).thenReturn(Optional.of(airlineAdministrator));
        Mockito.when(userContext.extractUsername()).thenReturn("user-2");

        Assertions.assertThrows(InvalidRequestException.class, () -> userService.deleteUser(1));
    }
}
