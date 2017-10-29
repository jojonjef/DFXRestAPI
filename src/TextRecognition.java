
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

public class TextRecognition {
	// **********************************************
	// *** Update or verify the following values. ***
	// **********************************************

	// Replace the subscriptionKey string value with your valid subscription
	// key.
	public static final String subscriptionKey = "048db2ec3be64d539222de6e7e527f21";

	// Replace or verify the region.
	//
	// You must use the same region in your REST API call as you used to obtain
	// your subscription keys.
	// For example, if you obtained your subscription keys from the westus
	// region, replace
	// "westcentralus" in the URI below with "westus".
	//
	// NOTE: Free trial subscription keys are generated in the westcentralus
	// region, so if you are using
	// a free trial subscription key, you should not need to change this region.
	//
	// Also, if you want to use the celebrities model, change "landmarks" to
	// "celebrities" here and in
	// uriBuilder.setParameter to use the Celebrities model.
	public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/ocr";

	public static void main(String[] args) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			// NOTE: You must use the same location in your REST call as you
			// used to obtain your subscription keys.
			// For example, if you obtained your subscription keys from westus,
			// replace "westcentralus" in the
			// URL below with "westus".
			URIBuilder uriBuilder = new URIBuilder(uriBase);

			uriBuilder.setParameter("language", "unk");
			uriBuilder.setParameter("detectOrientation ", "true");

			// Request parameters.
			URI uri = uriBuilder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body.
			StringEntity requestEntity = new StringEntity(
					"{\"url\":\"https://realthread.s3.amazonaws.com/cms%2F1443739324065-printed_tag_480.jpg\"}");
			request.setEntity(requestEntity);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Format and display the JSON response.
				ArrayList<String> sizes = new ArrayList<String>();
				sizes.add("s");
				sizes.add("m");
				sizes.add("l");
				sizes.add("xl");
				sizes.add("xs");
				String size = "size not recognised";
				String jsonString = EntityUtils.toString(entity);
				JSONObject json = new JSONObject(jsonString);
				System.out.println("REST Response:\n");
				String text = "Size not found";
				JSONArray JArr = json.getJSONArray("regions");
				for (int k = 0; k < JArr.length(); k++) {
					JSONObject jsonObject1 = JArr.getJSONObject(k);
					// System.out.println(jsonObject1);
					JSONArray JLines = jsonObject1.getJSONArray("lines");
					for (int i = 0; i < JLines.length(); i++) {
						JSONObject JLineArr = JLines.getJSONObject(i);
						JSONArray JWords = JLineArr.getJSONArray("words");
						for (int j = 0; j < JWords.length(); j++) {
							JSONObject JWordArr = JWords.getJSONObject(j);
							String qwe = JWordArr.get("text").toString().toLowerCase();
							if (sizes.contains(qwe)){
								size=qwe;
							}
						}
					}
				}
				System.out.println(size);
			}
		}


		catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
	}
}