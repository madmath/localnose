/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

package ca.mcgill.localnose;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class QueryAllServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
	    String query = "select from " + SocialProfile.class.getName();
	    List<SocialProfile> profiles = (List<SocialProfile>) pm.newQuery(query).execute();
	    JSONObject response = new JSONObject();
	    if (profiles.isEmpty()) {
	    	//return error
	    	try {
				response.put("success", false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    } else {
	    	JSONArray profilesarray = new JSONArray();
	    	for (SocialProfile p: profiles) {
	    		JSONObject jo = new JSONObject();
	    		try {
					jo.put("lng", p.getUlon());
					jo.put("name", p.getEmail());
		    		jo.put("lat", p.getUlat());
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    		profilesarray.put(jo);
	    	}

	    	try {
				response.put("success", true);
				response.put("profiles", profilesarray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	    resp.setContentType("application/json");
		resp.getWriter().print(response.toString());
	}
}
