package org.acme.util;

import org.acme.domain.Address;
import org.acme.representation.AddressCreateRepresentation;

public class AddressUtil {
    public static AddressCreateRepresentation createAddressCreateRepresentation(){
        return new AddressCreateRepresentation("Address 1", "Athens", "Greece");
    }

    public static Address createAddress(){
        return new Address();
    }
}
