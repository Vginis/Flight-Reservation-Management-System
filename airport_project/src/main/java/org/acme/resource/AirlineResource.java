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
import org.acme.domain.Airline;
import org.acme.domain.Airport;
import org.acme.domain.Flight;
import org.acme.domain.Reservation;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.FlightRepository;
import org.acme.representation.AirlineMapper;
import org.acme.representation.AirlineRepresentation;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.acme.resource.AirportProjectURIs.AIRLINES;

@Path(AIRLINES)
@RequestScoped
public class AirlineResource {

    @Inject
    EntityManager em;

    @Context
    UriInfo uriInfo;

    @Inject
    AirlineRepository airlineRepository;

    @Inject
    AirlineMapper airlineMapper;

    @Inject
    FlightRepository flightRepository;

    @Inject
    AirportRepository airportRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<AirlineRepresentation> findByName(@QueryParam("name") String name) {
        return airlineMapper.toRepresentationList(airlineRepository.findAirlineByAirlineName(name));
    }

    @GET
    @Path("{id:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findByPathParamId(@PathParam("id") Integer id) {
        Airline airline = airlineRepository.findById(id);
        if (airline == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok().entity(airlineMapper.toRepresentation(airline)).build();
    }

    @GET
    @Path("MostPopularAirport/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getMostPopularAirport(@PathParam("id") Integer id){
        String stat = airlineRepository.getMostPopularAirportByAirline(id);
        if (stat==null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(stat, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("Completeness/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getCompletenessByAirlineId(@PathParam("id") Integer id){
        Double stat = airlineRepository.getCompletenessByAirline(id);
        if (stat==null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(stat, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createAirline(AirlineRepresentation airlineDto){
        Airline airline = airlineMapper.toModel(airlineDto);
        airline = em.merge(airline);
        airlineRepository.persist(airline);
        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(airline.getId())).build();
        return Response.created(location).entity(airlineMapper.toRepresentation(airline)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAirline(@PathParam("id") Integer id, AirlineRepresentation representation) {
        if (!(id.equals(representation.id))) return Response.status(400).build();
        Airline airline = airlineMapper.toModel(representation);
        airlineRepository.getEntityManager().merge(airline);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteAirline(@PathParam("id") Integer id){
        Airline airline = airlineRepository.find("id", id).firstResult();
        if (airline == null) return Response.status(404).build();
        airlineRepository.deleteAirline(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/makeFlight/{flightNo}/{depAirport}/{arrAirport}/{aircraftType}/{ticketPrice}/{aircraftCapacity}")
    @Transactional
    public Response makeFlight(@PathParam("id") Integer id, @PathParam("flightNo") String flightNo,
                               @PathParam("depAirport") String dep3D,@PathParam("arrAirport") String arr3D,
                               @PathParam("aircraftType") String aircraftType, @PathParam("ticketPrice") Long ticketPrice, @PathParam("aircraftCapacity") Integer capacity){
        Flight flight = createFlight(id,flightNo,dep3D,arr3D,aircraftType,ticketPrice,capacity);
        if (flight ==null){return Response.status(404).build();}
        flight = em.merge(flight);
        flightRepository.persist(flight);
        URI makingFlightURI = uriInfo.getBaseUriBuilder().path("Flights").path(Integer.toString(flight.getId())).build();
        return Response.created(makingFlightURI).build();
    }

    @DELETE
    @Path("/{id}/deleteFlight/{flightNo}/{depAirport}/{arrAirport}")
    @Transactional
    public Response deleteFlight(@PathParam("id") Integer id, @PathParam("flightNo") String flightNo,
                                 @PathParam("depAirport") String depAirport,@PathParam("arrAirport") String arrAirport){
        Airport departureAirport = airportRepository.findAirportBy3DCode(depAirport).get(0);
        Airport arrivalAirport = airportRepository.findAirportBy3DCode(arrAirport).get(0);
        List<Flight> flights = flightRepository.findFlightByAirlineId(id);
        for (Flight f : flights) {
            if (f.getFlightNo().equals(flightNo) && f.getDepartureAirport()==departureAirport && f.getArrivalAirport()==arrivalAirport) {
                flightRepository.delete(f);
                return Response.noContent().build();
            }
        }
        return Response.status(404).build();
    }

    @Transactional
    protected Flight createFlight(Integer id,String flightNo,String dep3D, String arr3D,String aircraftType,Long ticketPrice,Integer capacity){
        Airline airline = airlineRepository.findById(id);
        Airport departureAirport = airportRepository.findAirportBy3DCode(dep3D).get(0);
        Airport arrivalAirport = airportRepository.findAirportBy3DCode(arr3D).get(0);
        if (airline == null){ return null;}
        if (departureAirport == null){ return null;}
        if (arrivalAirport == null){ return null;}
        Flight flight = new Flight();
        flight.setTicketList(new ArrayList<>());
        flight.setAirline(airline);
        flight.setFlightNo(flightNo);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setAircraftType(aircraftType);
        flight.setTicketPrice(ticketPrice);
        flight.setAircraftCapacity(capacity);
        flight.setAvailableSeats(flight.getAircraftCapacity()-flight.getTicketList().size());
        flight.setDepartureTime(LocalDateTime.now().plusMonths(4));
        flight.setArrivalTime(flight.getDepartureTime().plusHours(4));
        for (Flight f : airline.getFlights()){
            if (Objects.equals(f.getFlightNo(), flight.getFlightNo()) && f.getDepartureAirport()==flight.getDepartureAirport() && f.getArrivalAirport()==flight.getArrivalAirport()
                && Objects.equals(f.getTicketPrice(), flight.getTicketPrice()) && Objects.equals(f.getAircraftType(), flight.getAircraftType()) && Objects.equals(f.getAircraftCapacity(), flight.getAircraftCapacity()))
                return null;
        }
        airline.addFlight(flight);
        return flight;
    }

}
