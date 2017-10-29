// This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
// and the org.json library (org.json:json:20170516).

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main
{
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    public static final String subscriptionKey = "048db2ec3be64d539222de6e7e527f21";

    // Replace or verify the region.
    //
    // You must use the same region in your REST API call as you used to obtain your subscription keys.
    // For example, if you obtained your subscription keys from the westus region, replace
    // "westcentralus" in the URI below with "westus".
    //
    // NOTE: Free trial subscription keys are generated in the westcentralus region, so if you are using
    // a free trial subscription key, you should not need to change this region.
    public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";

    public static void main(String[] args)
    {
        HttpClient httpclient = new DefaultHttpClient();

        try
        {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("visualFeatures", "Description,Color");
            builder.setParameter("language", "en");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity("{\"url\":\"http://www.esquireclothing.com/images/products/verylarge/Black_Crew_Neck_Jumper_Remus_Black_Jumper_1.jpg\"}");
            
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);

                
                //System.out.println(json.toString(2));
                
                String colour = json.getJSONObject("color").get("dominantColorForeground").toString();
                JSONArray descriptions = (json.getJSONObject("description").getJSONArray("tags"));
                ArrayList<String> tags = new ArrayList<String>();
                
                ArrayList<String> clothes = new ArrayList<String>();
                clothes.add("jacket");
                clothes.add("pant");
                clothes.add("suit");
                clothes.add("shirt");
                clothes.add("hat");
                clothes.add("sweater");
                clothes.add("pants");
                clothes.add("dress");
                clothes.add("shoes");
                clothes.add("sock");
                
                for (int i = 0; i < descriptions.length(); i++){
                	if (clothes.contains(descriptions.get(i))){
                	tags.add(descriptions.getString(i));
                	}
                }
                
                // new json onject with a string colour, and a JSON array tags.
                JSONObject info = new JSONObject();
                info.put("tags", tags);
                info.put("colour", colour);
                		
                System.out.println(info.toString(2));
                
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}