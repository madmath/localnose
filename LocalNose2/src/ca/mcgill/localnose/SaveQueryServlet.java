/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

package ca.mcgill.localnose;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SaveQueryServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(SaveQueryServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// Get User associated with the Google Account.
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // Get PersistenceManager.
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // If user is not logged in to the Google Account.
        if (user != null) {
        	// Get query (required).
        	String query = req.getParameter("q");
        	String emails = req.getParameter("emails");
        	String lat = req.getParameter("lat");
        	String lon = req.getParameter("lon");
        	String ulat = req.getParameter("ulat");
        	String ulon = req.getParameter("ulon");

        	if (isNullOrBlank(query) || isNullOrBlank(emails) ||
        			isNullOrBlank(ulat) || isNullOrBlank(ulon)) {
        	    log.info("Setting status code to Bad Request");
        		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        		return;
        	} else {
        		SocialProfile sp;
        		try {
        			sp = pm.getObjectById(SocialProfile.class, user.getEmail());
        		} catch (JDOObjectNotFoundException e) {
        			sp = new SocialProfile(user, new Date(), user.getEmail(), ulat, ulon);
        		}

        		List<Question> questions = sp.getQuestions();
        		if (questions == null) {

        			questions = new ArrayList<Question>();
        			sp.setQuestions(questions);
        		}
        		Question q = new Question(query, new Date());
        		if (!isNullOrBlank(lat)) {
        			q.setLatitude(lat);
        		}
        		if (!isNullOrBlank(lat)) {
        			q.setLongitude(lon);
        		}
        		questions.add(q);

        		try {
        			pm.makePersistent(sp);
        		} finally {
        			pm.close();
        		}
    			log.info("We wrote this Profile to the database: " + sp.toString());

    			// Now for this question, find nearby users and send emails!
    			String[] emailArray = emails.split(",");

    			if (sendQuestion(user, query, emailArray)) {
    				JSONObject jo = new JSONObject();
    				try {
						jo.put("success", true);
					} catch (JSONException e) {
						e.printStackTrace();
					}
    				resp.setContentType("application/json");
    				resp.getWriter().print(jo.toString());
    				log.info("Success!");
    			} else {
    				JSONObject jo = new JSONObject();
    				try {
						jo.put("success", false);
					} catch (JSONException e) {
						e.printStackTrace();
					}
    				resp.setContentType("application/json");
    				resp.getWriter().print(jo.toString());
    				log.info("Failure!");
    			}
        	}
        } else {
        	log.info("Forbidden because user is logged out: " + user);
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
	}

	public static boolean isNullOrBlank(String param) {
		return param == null || param.trim().length() == 0;
	}

	private boolean sendQuestion(User from, String question, String[] recipients) {
		boolean success = false;

		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

	    for (String email: recipients) {
	    	String msgBody = "Hey " + email + "!\n\nA Localnose user has requested your help!\n\n" +
    		"They would like to have the following question answered:\n\n" + question + "\n\nHope you can help!\n\n" +
    				"Localnose";

	    	try {
                Message msg = new MimeMessage(session);
                try {
    				msg.setFrom(new InternetAddress(from.getEmail(), "Localnose User"));
    				msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(email, "Localnose user"));
    			} catch (UnsupportedEncodingException e) {
    				e.printStackTrace();
    			}

                msg.setSubject("A Localnose user needs your help!");
                msg.setText(msgBody);
                Transport.send(msg);
                log.info("Sent 1 message to " + email);
            } catch (AddressException e) {
                log.info(e.getMessage());
            } catch (MessagingException e) {
                log.info(e.getMessage());
            }
	           success = true;
	    }
        return success;
	}

	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles                                   :*/
	/*::                  'K' is kilometers (default)                            :*/
	/*::                  'N' is nautical miles   */
	// from http://www.zipcodeworld.com/samples/distance.java.html
	private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit == "K") {
		    dist = dist * 1.609344;
		  } else if (unit == "N") {
		  	dist = dist * 0.8684;
		    }
		  return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}

}
