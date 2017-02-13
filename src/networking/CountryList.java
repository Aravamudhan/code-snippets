package networking;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CountryList {

  private static String hostName = "api.worldbank.org";
  private static String countries = "/countries";


  public static void main(String[] args) throws Exception, IOException {
    List<String> countries = getAllCountries();
    System.out.println("Total countries " + countries.size());
    int i = 1;
    for (String country : countries) {
      System.out.println("Country " + i + " : " + country);
      i++;
    }
  }

  /**
   * 
   * @return The name of all countries
   * @throws ClientProtocolException
   * @throws IOException
   * @throws URISyntaxException
   */
  public static List<String> getAllCountries()
      throws ClientProtocolException, IOException, URISyntaxException {
    URI uri = new URIBuilder().setScheme("http").setHost(hostName).setPath(countries)
        .setParameter("pages", "4").setParameter("per_page", "80").setParameter("format", "json")
        .setParameter("page", "1").build();

    // Creation of the request
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget;
    httpget = new HttpGet(uri);

    List<String> countries = new ArrayList<>();
    JsonParser parser = new JsonParser();
    for (int page = 1; page <= 4; page++) {
      uri = new URIBuilder(uri).setParameter("page", new Integer(page).toString()).build();
      httpget = new HttpGet(uri);
      String responseBody = httpclient.execute(httpget, getResponseHandler());
      JsonArray json = (JsonArray) parser.parse(responseBody);
      for (JsonElement element : json.get(1).getAsJsonArray()) {
        JsonObject countryJson = element.getAsJsonObject();
        String country = countryJson.get("name").getAsString();
        // country = country + " " + countryJson.get("iso2Code").getAsString();
        // The objects with region id as NA are not countries
        String regionId = countryJson.get("region").getAsJsonObject().get("id").getAsString();
        if (!regionId.equalsIgnoreCase(("NA"))) {
          countries.add(country);
        }
      }
    }
    return countries;

  }

  /**
   * This is a custom response handler
   * 
   * @return
   */
  private static ResponseHandler<String> getResponseHandler() {
    ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
      @Override
      public String handleResponse(final HttpResponse response)
          throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = response.getEntity();
          return entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException("Unexpected response status: " + status);
        }
      }
    };
    return responseHandler;
  }

}
