package com.adnan;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

// Backs up CreateStream.html form submission. Trivial since there's no image uploaded, just a URL
@SuppressWarnings("serial")
public class CreateStreamServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Stream s = new Stream(req.getParameter("streamName"),
				req.getParameter("tags"), req.getParameter("url"));
		// persist to datastore
		ofy().save().entity(s).now();
		resp.sendRedirect("/ViewAllStreams.jsp");
	}
}