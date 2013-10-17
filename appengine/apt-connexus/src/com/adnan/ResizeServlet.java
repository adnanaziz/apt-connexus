package com.adnan;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO: meant for resizing images, but not correctly implemented, not to be used
public class ResizeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		List<ConnexusImage> images = OfyService.ofy().load()
				.type(ConnexusImage.class).list();
		for (ConnexusImage img : images) {
			if (img.bkUrl.endsWith("=s200")) {
				continue;
			} else if (img.bkUrl.endsWith("=s100")) {
				img.bkUrl = img.bkUrl.replace("=s100", "=s200");
			} else {
				img.bkUrl = img.bkUrl + "=s200";
			}
			ofy().save().entity(img).now();
		}
	}
}
