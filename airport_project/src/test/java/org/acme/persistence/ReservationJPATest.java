package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Reservation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SortBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class ReservationJPATest extends JPATest {

    @Inject
    ReservationRepository reservationRepository;

    @Test
    void listReservations(){
        List<?> result = em.createQuery("select r from Reservation r").getResultList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void test_searchReservationByParams() {
        PageQuery<SortBy> query = new PageQuery<>(10, 0, SortDirection.ASCENDING);
        PageResult<Reservation> result = reservationRepository.searchReservationByParams("passenger1", query);
        Assertions.assertEquals(1, result.getTotal());
    }
}
