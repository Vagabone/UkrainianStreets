package ua.skarb.ukrainianStreets.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.OverpassNode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class OverpassService {

    private static final String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";

    private final JsonParser jsonParser = new JsonParser();

    private final Gson gson = new Gson();

    public OverpassNode[] executeQuery(String query) throws IOException {
        StringBuilder result = new StringBuilder();
        String inputLine;
        String hostname = OVERPASS_API;

        URL osm = new URL(hostname);
        HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
        printout.writeBytes("data=" + URLEncoder.encode(query, "utf-8"));
        printout.flush();
        printout.close();
        try (BufferedReader stream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            while ((inputLine = stream.readLine()) != null) {
                result.append(inputLine);
            }
        } finally {
            // releases any system resources associated with the stream
            if (connection.getInputStream() != null)
                connection.getInputStream().close();

        }
        JsonObject jsonObject = jsonParser.parse(result.toString()).getAsJsonObject();
        JsonArray elements = jsonObject.getAsJsonArray("elements");
        return gson.fromJson(elements, OverpassNode[].class);
    }

}
