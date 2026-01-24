package org.acme.profile;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;
import java.util.UUID;

public class IsolatedDbProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.datasource.jdbc.url",
                "jdbc:h2:mem:testdb-" + UUID.randomUUID()
        );
    }
}

