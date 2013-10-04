package com.adnan;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class SingleStreamServletAPI extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		List<ConnexusImage> allImages = OfyService.ofy().load().type(ConnexusImage.class).list();
		Long streamId = new Long(req.getParameter("streamId"));
		List<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (ConnexusImage s : allImages ) {
			if ( s.streamId.equals(streamId) ) {
				result.add(s);
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(result);
		resp.setContentType("application/json");
		resp.getWriter().print(json);
		
		// for debugging
		Type t = new TypeToken<List<ConnexusImage>>(){}.getType();
		List<ConnexusImage> fromJson = (List<ConnexusImage>) gson.fromJson(json, t);
		for (ConnexusImage s : fromJson ) {
			System.out.println(s);
		}
	}
}