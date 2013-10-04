package com.adnan;

import static com.googlecode.objectify.ObjectifyService.ofy;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

public class Upload extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// APT: dont worry that the getUploadedBlobs/getServingUrl are
		// deprecated
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		// the "myFile" keyword comes from the upload form in ShowSream.jsp
		BlobKey blobKey = blobs.get("myFile");

		// APT: the BlobKey is not a url, so we use ImagesServiceFactory to get
		// a URL to serve the image stored in the blob from
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		String bkUrl = imagesService.getServingUrl(blobKey);

		// note that these parameters are set when we do the
		// blobstoreService.createUploadUrl() call
		// in ShowStream.jsp
		Long streamId = new Long(req.getParameter("streamId"));
		String streamName = req.getParameter("streamName");
		// at a later day we might allow for tags on a per image basis
		String content = "dummy content";

		ConnexusImage s = new ConnexusImage(streamId, streamName, content,
				bkUrl);
		ofy().save().entity(s).now();
		// after the creation of the image is complete we want to return to the
		// stream that it was added to
		// we use the streamId as an argument to the ShowStream jsp
		// NOTE: in the "/ShowStream..." below if we left off the leading / we
		// woud
		// get a relative redirect, which is not what we want
		res.sendRedirect("/ShowStream.jsp?streamId=" + streamId + "&"
				+ "streamName=" + streamName);
	}
}
