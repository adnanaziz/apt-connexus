import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import java.util.Date;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.common.io.Files;

import java.util.Map;


class Task implements Callable<String> {
	 String apiUrl;
	 String tstJson;
	 public Task( String apiUrl, String tstJson ) {
		 this.apiUrl = apiUrl;
		 this.tstJson = tstJson;
	 }	
	 public String call() {
		 TestUploadAPIServlet.makeHTTPPOSTRequest(apiUrl, tstJson);
		 return "" + TestUploadAPIServlet.i.incrementAndGet() 
				+ "\tUploaded " + " at time " + new Date();
	 }
 }
 
/* Code to illustrate how to post an image from a local file to appengine.
 * Uses multiple libraries to achive this goal.
 *
 * @author adnanaziz
 */

public class TestUploadAPIServlet {

    private static final int NTHREADS=4;

    private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
    public static AtomicLong i = new AtomicLong(0);
 
    public static void main(String[] args) {
  	String tstJson;
  	byte[] b ={} ;

	String[] fileNames = {
		"/Users/adnanaziz/Desktop/naan.jpg"
	};
	String apiUrl = "http://apt-connexus.appspot.com/UploadServletAPI?streamId=" 
			+ "5795199372492800" 
			+ "&streamName=dummy";

	String apiUrlLocal = "http://localhost:8888/UploadServletAPI?streamId="
			+ "5629499534213120" + "&streamName=First";
	while ( true ) {
	for ( String fn : fileNames ) {
    		try {
      			b = Files.toByteArray( new File(fn));
    		} catch (Exception e) {
      			e.printStackTrace();
    		}
    		Image img = new Image( 0.0d, 0.0d, b );
    		Gson gson = new Gson();
    		tstJson = gson.toJson(img);
       		// makeHTTPPOSTRequest(apiUrl, tstJson);
		Callable<String> t = new Task(apiUrl, tstJson);
		// exec.execute(t);
		Future<String> f = exec.submit(t);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String s = f.get(); 
			System.out.println(s);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException");
			e.printStackTrace();
		} 
		//catch (TimeoutException e) {
 		//	f.cancel(true);
		//} 
		catch (Exception e) 	{
			System.out.println("GeneralException");
			e.printStackTrace();
		}
		
	}
	}

    }
 
    public static void makeHTTPPOSTRequest(String apiUrl, String tstJson) {
        try {
            HttpClient c = new DefaultHttpClient();        
            HttpPost p = new HttpPost(apiUrl);        
            p.setEntity(new StringEntity(tstJson, ContentType.create("application/json")));
            HttpResponse r = c.execute(p);

            // used to process response, if any
            /* 
            BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
               JSONParser j = new JSONParser();
               JSONObject o = (JSONObject)j.parse(line);
               Map response = (Map)o.get("response");
               System.out.println(response.get("somevalue"));
            }
	    */
        }
        // catch(ParseException e) {
        //    System.out.println(e);
        // }
        catch(IOException e) {
            System.out.println(e);
        }                        
    }    
}

