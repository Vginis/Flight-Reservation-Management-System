package org.acme.persistence;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JPATest {

    @Inject
    EntityManager em;

    @Transactional
    @BeforeEach
    public void initializeDB() {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("import.sql");
        String sql = convertStreamToString(in);
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        em.createNativeQuery(sql).executeUpdate();
    }

    private String convertStreamToString(InputStream in) {
        Scanner s = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
