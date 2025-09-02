package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import org.acme.mapper.FlightMapper;
import org.acme.representation.FlightRepresentation;

import java.net.URI;
import java.util.List;

import static org.acme.resource.AirportProjectURIs.FLIGHTS;

@Path(FLIGHTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FlightResource {

    @Inject
    EntityManager em;

    @Context
    UriInfo uriInfo;

    @Inject
    FlightRepository flightRepository;

    @Inject
    FlightMapper flightMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<FlightRepresentation> findByAirlineId(@QueryParam("airlineId") Integer airlineId) {
        return flightMapper.toRepresentationList(flightRepository.findFlightByAirlineId(airlineId));
    }

    @GET
    @Path("{flightId:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findByPathParamId(@PathParam("flightId") Integer flightId) {
        Flight flight = flightRepository.findById(flightId);
        if (flight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(flightMapper.toRepresentation(flight)).build();
    }

    @GET
    @Path("Completeness/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getCompletenessByFlightId(@PathParam("id") Integer id) {
        Double stat = flightRepository.getCompletenessByFlight(id);
        if (stat == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(stat, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("departure/{departureAirport}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response searchFlightsByDepartureAirport(@PathParam("departureAirport") String departureAirport) {
        List<Flight> flights = flightRepository.findFlightByDepartureAirport(departureAirport);
        if (flights.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(flightMapper.toRepresentationList(flights)).build();
    }

    @GET
    @Path("arrival/{arrivalAirport}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response searchFlightsByArrivalAirport(@PathParam("arrivalAirport") String arrivalAirport) {
        List<Flight> flights = flightRepository.findFlightByArrivalAirport(arrivalAirport);
        if (flights.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(flightMapper.toRepresentationList(flights)).build();
    }

    @GET
    @Path("arrivals/{arrivalTime}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response searchFlightsByArrivalTime(@PathParam("arrivalTime") String arrivalTime) {
        List<Flight> flights = flightRepository.findFlightByArrivalTime(arrivalTime);
        if (flights.isEmpty()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(flightMapper.toRepresentationList(flights)).build();
    }

    @GET
    @Path("departures/{departureTime}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response searchFlightsByDepartureTime(@PathParam("departureTime") String departureTime) {
        List<Flight> flights = flightRepository.findFlightByDepartureTime(departureTime);
        if (flights.isEmpty()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(flightMapper.toRepresentationList(flights)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createFLight(FlightRepresentation flightDto) {
        Flight flight = flightMapper.toModel(flightDto);
        flight = em.merge(flight);
        flightRepository.persist(flight);
        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(flight.getId())).build();
        return Response.created(location).entity(flightMapper.toRepresentation(flight)).build();
    }

    @PUT
    @Path("/{flightId}")
    @Transactional
    public Response updateFlight(@PathParam("flightId") Integer id, FlightRepresentation representation) {
        if (!(id.equals(representation.id))) return Response.status(400).build();
        Flight flight = flightMapper.toModel(representation);
        flightRepository.getEntityManager().merge(flight);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteFlight(@PathParam("id") Integer id) {
        Flight flight = flightRepository.find("id", id).firstResult();
        if (flight == null) return Response.status(404).build();
        flightRepository.deleteFlight(id);
        return Response.noContent().build();
    }

    @GET
    @Path("dep/{departureAirport}/arr/{arrivalAirport}/dept/{departureTime}/arrt/{arrivalTime}/passc/{passCount}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response SearchByParams(@PathParam("departureAirport") String depAirport,
                                                     @PathParam("arrivalAirport") String arrAirport,
                                                     @PathParam("departureTime") String depTime,
                                                     @PathParam("arrivalTime") String arrTime,
                                                     @PathParam("passCount") Integer passCount) {
        List<Flight> flights = flightRepository.findFlightByParameters(depAirport, arrAirport, depTime, arrTime, passCount);
        if (flights.isEmpty()) return Response.status(404).build();
        return Response.ok().entity(flightMapper.toRepresentationList(flights)).build();
    }

}
