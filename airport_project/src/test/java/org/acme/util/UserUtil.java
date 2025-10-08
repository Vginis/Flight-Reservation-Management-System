package org.acme.util;

import org.acme.representation.user.AirlineAdministratorCreateRepresentation;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    public static AirlineAdministratorCreateRepresentation createAirlineAdministratorCreateRepresentation(){
        return new AirlineAdministratorCreateRepresentation("user-1","Kendrick", "Nunn","email@email.com","1234557890",
                new ArrayList<>(List.of(AddressUtil.createAddressCreateRepresentation())),"AA");
    }
}
