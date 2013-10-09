package com.adnan;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.gson.Gson;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.ServingUrlOptions;


@SuppressWarnings("serial")
public class UploadServletAPI extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(UploadServletAPI.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		log.warning("starting doPost");

		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		// JSONObject jObj = new JSONObject(sb.toString());
		String tstJson = sb.toString();
		Gson gson = new Gson();
		Image tstImage = gson.fromJson(tstJson, Image.class);
	

		// from
		// http://stackoverflow.com/questions/12328622/writing-byte-array-to-gae-blobstore
		String mimeType = "image/png"; // TODO: is it needed to specify png? jpg
										// uploads have worked fine
		// save data to Google App Engine Blobstore
		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile file = fileService.createNewBlobFile(mimeType);
		FileWriteChannel writeChannel = fileService
				.openWriteChannel(file, true);
		writeChannel.write(java.nio.ByteBuffer.wrap(tstImage.physicalImage));
		writeChannel.closeFinally();
		log.warning("done blobstore");

		
		// your blobKey to your data in Google App Engine BlobStore
		BlobKey blobKey = fileService.getBlobKey(file);

		// THANKS TO BLOBKEY YOU CAN GET FOR EXAMPLE SERVING URL FOR IMAGES

		// Get the image serving URL (in https:// format)
		String imageUrl = ImagesServiceFactory.getImagesService()
				.getServingUrl(
						ServingUrlOptions.Builder.withBlobKey(blobKey)
								.secureUrl(true));

		System.out.println(imageUrl);
		
		Long streamId = new Long(req.getParameter("streamId"));
		String streamName = req.getParameter("streamName");
		String content = "dummy";
		ConnexusImage s = new ConnexusImage(streamId, streamName, content, imageUrl);
		ofy().save().entity(s).now();
		log.warning("done saving");

	}
}
