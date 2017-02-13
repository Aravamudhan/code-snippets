package networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChuckNorrisJokes {

  public static void main(String[] args) {
    try {
      // Get the url
      URL url = new URL("https://api.icndb.com/jokes/random");
      // Open a connection to the url
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      // Get an input stream through the connection
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      // Read the data from the connection
      String inputLine = in.readLine();
      JsonParser parser = new JsonParser();
      // Parse the resultant string and convert that to json object
      JsonObject responseObject = (JsonObject) parser.parse(inputLine);
      // The response has type and value. Inside the value, exists the joke and the joke id. We need
      // the joke string alone.
      // Getting the value first
      JsonObject jokeObject = (JsonObject) responseObject.get("value");
      // Then getting the joke
      System.out.println(jokeObject.get("joke").getAsString().replaceAll("&quot;", "\""));
      in.close();
    } catch (Exception e) {
      System.out.println("There is some problem in connecting with the url" + e);
    }
  }

}
