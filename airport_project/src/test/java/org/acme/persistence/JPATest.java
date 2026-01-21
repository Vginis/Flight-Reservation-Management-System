package org.acme.persistence;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public abstract class JPATest {

    @Inject
    EntityManager em;

}
