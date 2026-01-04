package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Reservation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SortBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReservationRepository extends AbstractSearchRepository<Reservation> {

    public PageResult<Reservation> searchReservationByParams(String username, PageQuery<SortBy> query) {
        Map<String, Object> params = new HashMap<>();
        String queryBuilder = "SELECT r FROM Reservation r " + "JOIN r.user u " +
                "WHERE 1=1 AND u.username = :username";
        params.put("username", username);

        PanacheQuery<Reservation> reservationsPanacheQuery = this.find(queryBuilder, params)
                .page(query.getIndex(), query.getSize());

        List<Reservation> reservationList = reservationsPanacheQuery.list();
        return new PageResult<>(reservationList.size(), reservationList);
    }
}
