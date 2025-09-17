package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.SortDirection;
import org.acme.constant.search.UserSortAndFilterBy;
import org.acme.representation.user.UserUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.UserService;
import org.acme.validation.EnumerationValue;

@Path(AirportProjectURIs.USERS)
@Authenticated
public class UserResource {
    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = UserSortAndFilterBy.class) String searchField,
                                   @QueryParam("searchValue") String searchValue,
                                   @QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortBy") @EnumerationValue(acceptedEnum = UserSortAndFilterBy.class) String sortBy,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<UserSortAndFilterBy> query = new PageQuery<>(ValueEnum.fromValue(searchField, UserSortAndFilterBy.class), searchValue, size, index
                , ValueEnum.fromValue(sortBy, UserSortAndFilterBy.class), ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(userService.searchUsersByParams(query)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserDetails(@Valid UserUpdateRepresentation userUpdateRepresentation,
                                  @QueryParam("username") String username){
        userService.updateUser(userUpdateRepresentation, username);
        return Response.ok(SuccessMessages.USER_UPDATE_SUCCESS).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Integer id){
        userService.deleteUser(id);
        return Response.ok(SuccessMessages.USER_DELETE_SUCCESS).build();
    }

}
