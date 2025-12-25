package org.acme.resource.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.service.CountryCityService;

@Path(AirportProjectURIs.CITIES_COUNTRIES)
public class CountryCityResource {

    @Inject
    CountryCityService countryCityService;

    @GET
    @Path("smart-search/country")
    @Produces(MediaType.APPLICATION_JSON)
    public Response smartSearchCountries(@QueryParam("value") String value){
        return Response.ok(countryCityService.smartSearchCountries(value)).build();
    }

    @GET
    @Path("smart-search/city")
    @Produces(MediaType.APPLICATION_JSON)
    public Response smartSearchCities(@QueryParam("value") String value,
                                      @QueryParam("country") String countryName){
        return Response.ok(countryCityService.smartSearchCities(value, countryName)).build();
    }
}
