package com.plotsquared.iserver.example;

import com.plotsquared.iserver.object.Header;
import com.plotsquared.iserver.object.Request;
import com.plotsquared.iserver.object.Response;
import com.plotsquared.iserver.util.Assert;
import com.plotsquared.iserver.views.decl.ViewMatcher;
import com.plotsquared.iserver.views.requesthandler.Middleware;
import com.plotsquared.iserver.views.requesthandler.MiddlewareQueue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings( "ALL" )
public class APITest
{

    private static final List<User> users = Arrays.asList( new User( "User1", "user1@example.com" ), new User( "User2",
            "user2@example.com" ) );

    @ViewMatcher(filter = "user", cache = false, name = "Users")
    public void users(final Request request, final Response response)
    {
        final JSONArray array = new JSONArray(  );
        for ( final User user : users )
        {
            array.put( userToJson( user ) );
        }
        response.setContent( array.toString() ).getHeader().set( Header.HEADER_CONTENT_TYPE, Header.CONTENT_TYPE_JSON );
    }

    @ViewMatcher(filter = "no/such/user", cache = true, name = "NoSuchUser")
    public void userNotFound(final Request request, final Response response)
    {
        response.setContent( "<h1>There is no such user!</h1>" );
    }

    @ViewMatcher(filter = "user/<username>", cache = false, name = "User", middlewares = { UserMiddleware.class } )
    public void user(final Request request, final Response response)
    {
        final User user = (User) request.getMeta( "apiUser" );
        response.setContent( userToJson( user ).toString() ).getHeader().set( Header.HEADER_CONTENT_TYPE, Header
            .CONTENT_TYPE_JSON );
    }

    private JSONObject userToJson(final User user)
    {
        return new JSONObject( user );
    }

    /**
     * Checks if the requested user actually exists,
     * if it doesn't - the request gets redirected
     */
    public static class UserMiddleware extends Middleware
    {

        @Override
        public void handle(final Request request, final MiddlewareQueue queue)
        {
            final Optional<User> user = users.stream().filter( u -> u.name.equalsIgnoreCase( request.getVariables()
                .get( "username" ) ) ).findAny();
            if ( !user.isPresent() )
            {
                request.internalRedirect( "no/such/user" );
            } else
            {
                request.addMeta( "apiUser", user.get() );
                queue.handle( request );
            }
        }
    }

    /**
     * A very simple user implementation, should be very straight forward
     *
     * Getters are needed for the JSON mapping to work!
     */
    @SuppressWarnings( "ALL" )
    public static class User
    {

        private final String name;
        private final String email;

        public String getName()
        {
            return name;
        }

        public String getEmail()
        {
            return email;
        }

        public User(final String name, final String email)
        {
            Assert.notNull( name, email );

            this.name = name;
            this.email = email;
        }
    }

}