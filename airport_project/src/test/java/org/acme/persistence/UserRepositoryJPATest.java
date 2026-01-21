package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.SortDirection;
import org.acme.constant.search.UserSortAndFilterBy;
import org.acme.domain.User;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@QuarkusTest
class UserRepositoryJPATest extends JPATest {

    @Inject
    UserRepository userRepository;

    @Test
    void test_searchUsersByParams() {
        PageQuery<UserSortAndFilterBy> query = new PageQuery<>(
                UserSortAndFilterBy.USERNAME, "a", 5, 0,
                UserSortAndFilterBy.FIRST_NAME, SortDirection.ASCENDING
        );

        PageResult<User> userPageResult = userRepository.searchUsersByParams(query);
        Assertions.assertEquals(2, userPageResult.getTotal());
        Assertions.assertEquals("Alice", userPageResult.getResults().getFirst().getFirstName());
    }

    @Test
    void test_findUserByUsername() {
        Optional<User> userOptional = userRepository.findUserByUsername("passenger1");
        Assertions.assertTrue(userOptional.isPresent());
    }
}
