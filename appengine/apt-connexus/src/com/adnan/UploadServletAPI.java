package com.adnan;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;

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

@SuppressWarnings("deprecation")
class Image {
	double latitude;
	double longitude;
	byte[] physicalImage;

	public Image(double a, double b, byte[] c) {
		latitude = a;
		longitude = b;
		physicalImage = c;
	}

	public Image() {
	}
}

@SuppressWarnings("serial")
public class UploadServletAPI extends HttpServlet {

	// small image, used for testing only
	public static String tstJson = "{\"latitude\":1.2,\"longitude\":3.14,\"physicalImage\":[-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,20,0,"
			+ "0,0,22,2,3,0,0,0,-67,47,84,-107,0,0,0,12,80,76,84,69,-1,-1,-1,-52,-1,-1,0"
			+ ",-103,51,0,0,0,44,-118,-84,-23,0,0,0,2,116,82,78,83,-1,0,-27,-73,48,74,0,0,"
			+ "0,1,98,75,71,68,0,-120,5,29,72,0,0,0,126,73,68,65,84,8,-41,99,8,5,1,-122,-48,"
			+ "-16,-1,87,-127,100,-35,-86,-67,-95,12,-31,39,56,-72,-81,50,-60,53,-83,90,-76," 
			+ "-107,33,-90,-117,107,-59,86,-122,-118,69,74,74,-70,12,85,77,29,10,-67,12,15,58," 
			+ "52,20,86,51,60,88,-96,-47,-79,24,72,54,-83,82,102,120,-44,-59,-60,-95,-52,-16,-118," 
			+ "9,68,-66,96,90,-59,-95,-51,80,-59,-63,-75,106,45,67,-63,10,14,14,93,-122,24,13,14,14," 
			+ "83,-122,24,-91,21,92,91,25,-62,31,104,-83,-66,-54,16,90,-93,96,11,-73,17,4,0,63," 
			+ "-114,45,61,-81,-115,51,-121,0,0,0,86,116,69,88,116,99,111,109,109,101,110,116,0,84,104," 
			+ "105,115,32,97,114,116,32,105,115,32,105,110,32,116,104,101,32,112,117,98,108,105,99,32," 
			+ "100,111,109,97,105,110,46,32,75,101,118,105,110,32,72,117,103,104,101,115,44,32,107," 
			+ "101,118,105,110,104,64,101,105,116,46,99,111,109,44,32,83,101,112,116,101,109,98,101," 
			+ "114,32,49,57,57,53,118,-10,-17,-100,0,0,0,0,73,69,78,68,-82,66,96,-126]}";


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
		System.out.println(tstImage.latitude);

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

		// your blobKey to your data in Google App Engine BlobStore
		BlobKey blobKey = fileService.getBlobKey(file);

		// THANKS TO BLOBKEY YOU CAN GET FOR EXAMPLE SERVING URL FOR IMAGES

		// Get the image serving URL (in https:// format)
		String imageUrl = ImagesServiceFactory.getImagesService()
				.getServingUrl(
						ServingUrlOptions.Builder.withBlobKey(blobKey)
								.secureUrl(true)) + "=s100";

		System.out.println(imageUrl);
		
		Long streamId = new Long(req.getParameter("streamId"));
		String streamName = req.getParameter("streamName");
		String content = "dummy";
		ConnexusImage s = new ConnexusImage(streamId, streamName, content, imageUrl);
		ofy().save().entity(s).now();
	}
}