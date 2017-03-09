/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sample_maven_web_app;

import data.Model;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import objects.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author wlloyd
 */
@Path("users")
public class UserService {

    static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public UserService() {}

    /**
     * Retrieves representation of an instance of services.GenericResource
     *
     * @return an instance of java.lang.String
     */
//    @GET
//    @Produces(MediaType.TEXT_HTML)
//    public String getUsers() {
//        //TODO return proper representation object
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><body><style>table, th, td {font-family:Arial,Verdana,sans-serif;font-size:16px;padding: 0px;border-spacing: 0px;}</style><b>USERS LIST:</b><br><br><table cellpadding=10 border=1><tr><td>username</td><td>password</td><td>email</td><td>uid</td><td>Fname</td><td>Lname</td><td>utype</td></tr>");
//        try {
//            Model db = Model.singleton();
//            User[] users = db.getUsers();
//            for (int i = 0; i < users.length; i++) {
//                sb.append("<tr><td>" + users[i].getUsername() + "</td><td>" + users[i].getPassword() + "</td><td>" + users[i].getEmail() + "</td><td>" + users[i].getUserid() + "</td><td>" + users[i].getFname() + "</td><td>" + users[i].getLname() + "</td><td>" + users[i].getUtype() + "</td></tr>");
//            }
//        } catch (Exception e) {
//            sb.append("</table><br>Error getting users: " + e.toString() + "<br>");
//        }
//        sb.append("</table></body></html>");
//        return sb.toString();
//    }
    @GET
    @Path("{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsersJson(@PathParam("userid") String id) {
        LinkedList<User> lusers = new LinkedList<User>();
     
        try
        {
            int userid = Integer.parseInt(id);
            Model db = Model.singleton();
            User[] users = db.getUsers();
            if (userid ==0)
                for (int i=0;i<users.length;i++)
                    lusers.add(users[i]);
            else
                lusers.add(users[0]);
            logger.log(Level.INFO, "Received request to fetch user id=" + userid);
            return lusers;
        }
        catch (Exception e)
        {
            JSONObject obj = new JSONObject();
                logger.log(Level.WARNING, "Error getting users:" + e.toString());
                return null;
        }
    }    

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUser(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jobj.toString(), User.class);
        StringBuilder text = new StringBuilder();
        try {
            Model db = Model.singleton();
            int userid = user.getUserid();
            db.updateUser(user);
            logger.log(Level.INFO, "update user with userid=" + userid);
            text.append("User id updated with user id=" + userid + "\n");
        } catch (SQLException sqle) {
            String errText = "Error updating user after db connection made:\n" + sqle.getMessage() + " --- " + sqle.getSQLState() + "\n";
            logger.log(Level.SEVERE, errText);
            text.append(errText);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error connecting to db.");
            text.append("Error connecting to db.");
        }
        return text.toString();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteUser(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jobj.toString(), User.class);
        StringBuilder text = new StringBuilder();
        try {
            Model db = Model.singleton();
            int userid = user.getUserid();
            db.deleteUser(userid);
            logger.log(Level.INFO, "user deleted from db=" + userid);
            text.append("User id deleted with id=" + userid);
        } catch (SQLException sqle) {
            String errText = "Error deleteing user after db connection made:\n" + sqle.getMessage() + " --- " + sqle.getSQLState() + "\n";
            logger.log(Level.SEVERE, errText);
            text.append(errText);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error connecting to db.");
            text.append("Error connecting to db.");
        }
        return text.toString();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jobj.toString(), User.class);

        StringBuilder text = new StringBuilder();
        text.append("The JSON obj:" + jobj.toString() + "\n");
        //text.append("Hello " + user.getName() + "\n");
        //text.append("You're only " + user.getAge() + " years old.\n");
        try {
            Model db = Model.singleton();
            int userid = db.newUser(user);
            logger.log(Level.INFO, "user persisted to db as userid=" + userid);
            text.append("User id persisted with id=" + userid);
        } catch (SQLException sqle) {
            String errText = "Error persisting user after db connection made:\n" + sqle.getMessage() + " --- " + sqle.getSQLState() + "\n";
            logger.log(Level.SEVERE, errText);
            text.append(errText);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error connecting to db.");
        }

        return text.toString();
    }
}
