package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/Greetings")
public class GreetingResource {
    @ConfigProperty(name = "Greetings.message", defaultValue="Hello from default")
    String message;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String Greetings() {
        return message;
        
    }
}






