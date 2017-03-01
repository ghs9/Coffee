/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
 *
 * @author Reid
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import objects.User;
import objects.Review;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * REST Web Service
 *
 * @author wlloyd
 */
@Path("reviews")
public class Review_Service {

    static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public Review_Service() {
    }

    /**
     * Retrieves representation of an instance of services.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getReviews() {
        //TODO return proper representation object
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><style>table, th, td {font-family:Arial,Verdana,sans-serif;font-size:16px;padding: 0px;border-spacing: 0px;}</style><b>Review LIST:</b><br><br><table cellpadding=10 border=1><tr><td>date</td><td>text</td><td>rating</td><td>cid</td><td>userid</td></tr>");
        try {
            Model db = Model.singleton();
            Review[] rvws = db.getReviews();
            for (int i = 0; i < rvws.length; i++) {
                sb.append("<tr><td>" + rvws[i].getDate() + "</td><td>" + rvws[i].getText() + "</td><td>" + rvws[i].getRating() + "</td><td>" + rvws[i].getCid() + "</td><td>" + rvws[i].getUserid() + "</td></tr>");
            }
        } catch (Exception e) {
            sb.append("</table><br>Error getting users: " + e.toString() + "<br>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT //here
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateReview(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Review rvw = mapper.readValue(jobj.toString(), Review.class);
        StringBuilder text = new StringBuilder();
        try {
            Model db = Model.singleton();
            int rid = rvw.getRid();
            db.updateReview(rvw);
            logger.log(Level.INFO, "update review with rid=" + rid);
            text.append("review id updated with r id=" + rid + "\n");
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
    public String deleteReview(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Review rvw = mapper.readValue(jobj.toString(), Review.class);
        StringBuilder text = new StringBuilder();
        try {
            Model db = Model.singleton();
            int rid = rvw.getRid();
            db.deleteReview(rid);
            logger.log(Level.INFO, "review deleted from db=" + rid);
            text.append("review id deleted with id=" + rid);
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
    public String createReview(String jobj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Review rvw = mapper.readValue(jobj.toString(), Review.class);

        StringBuilder text = new StringBuilder();
        text.append("The JSON obj:" + jobj.toString() + "\n");
        text.append("Review: " + rvw.getText() + "\n");
        text.append("Rid " + rvw.getRid() + "\n");
        try {
            Model db = Model.singleton();
            int rid = db.createReview(rvw);
            logger.log(Level.INFO, "review persisted to db as rid=" + rid);
            text.append("Review id persisted with id=" + rid);
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

