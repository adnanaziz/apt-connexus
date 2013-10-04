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

 
/* Code to illustrate how to post an image from a local file to appengine.
 * Uses multiple libraries to achive this goal.
 *
 * @author adnanaziz
 */
class Image {
  double latitude;
  double longitude;
  byte[] physicalImage;
  public Image(double a, double b, byte[] c) {
    latitude = a;
    longitude = b;
    physicalImage = c;
  }
}

public class TestUploadAPIServlet {
 
  private String apiURL;

  public static String tstJson;
  public static byte[] b ;

  { // static initializer block
    try {
      // tmp.dat holds image, can be png, jpg
      b = Files.toByteArray( new File("tmp.dat"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    Image img = new Image( 1.2d, 3.14d, b );
    Gson gson = new Gson();
    tstJson = gson.toJson(img);
    System.out.println(tstJson);
  }

    // static String tstJson =  "{\"latitude\":1.2,\"longitude\":3.14,\"physicalImage\":[-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,20,0,0,0,22,2,3,0,0,0,-67,47,84,-107,0,0,0,12,80,76,84,69,-1,-1,-1,-52,-1,-1,0,-103,51,0,0,0,44,-118,-84,-23,0,0,0,2,116,82,78,83,-1,0,-27,-73,48,74,0,0,0,1,98,75,71,68,0,-120,5,29,72,0,0,0,126,73,68,65,84,8,-41,99,8,5,1,-122,-48,-16,-1,87,-127,100,-35,-86,-67,-95,12,-31,39,56,-72,-81,50,-60,53,-83,90,-76,-107,33,-90,-117,107,-59,86,-122,-118,69,74,74,-70,12,85,77,29,10,-67,12,15,58,52,20,86,51,60,88,-96,-47,-79,24,72,54,-83,82,102,120,-44,-59,-60,-95,-52,-16,-118,9,68,-66,96,90,-59,-95,-51,80,-59,-63,-75,106,45,67,-63,10,14,14,93,-122,24,13,14,14,83,-122,24,-91,21,92,91,25,-62,31,104,-83,-66,-54,16,90,-93,96,11,-73,17,4,0,63,-114,45,61,-81,-115,51,-121,0,0,0,86,116,69,88,116,99,111,109,109,101,110,116,0,84,104,105,115,32,97,114,116,32,105,115,32,105,110,32,116,104,101,32,112,117,98,108,105,99,32,100,111,109,97,105,110,46,32,75,101,118,105,110,32,72,117,103,104,101,115,44,32,107,101,118,105,110,104,64,101,105,116,46,99,111,109,44,32,83,101,112,116,101,109,98,101,114,32,49,57,57,53,118,-10,-17,-100,0,0,0,0,73,69,78,68,-82,66,96,-126]}";

    public TestUploadAPIServlet(String apiURL) {        
        this.apiURL = apiURL;
    }

    public static void main(String[] args) {
       TestUploadAPIServlet tmp = new TestUploadAPIServlet("http://apt-connexus.appspot.com/UploadServletAPI?streamId=5681726336532480&streamName=Second");
       tmp.makeHTTPPOSTRequest();
    }
 
    public void makeHTTPPOSTRequest() {
        try {
            HttpClient c = new DefaultHttpClient();        
            HttpPost p = new HttpPost(this.apiURL);        
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
