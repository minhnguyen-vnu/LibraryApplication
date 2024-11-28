package com.jmc.library.Controllers.GoogleBookAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for handling Google Books API methods.
 */
public class GoogleBookAPIMethod {
    public JSONObject searchBook(String ISBN) {
        try {
            String query = "isbn:" + ISBN;
            String API_KEY = new GoogleBookAPI().getApiKey();
            String URLString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + API_KEY;
            URL url = new URL(URLString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new JSONObject();
    }

    /**
     * Gets the items array from the JSON object.
     *
     * @param jsonObject The JSON object.
     * @return The items array.
     */
    public int getTotalItems(JSONObject jsonObject) {
        return jsonObject.getInt("totalItems");
    }
}