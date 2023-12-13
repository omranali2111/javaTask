package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
public class Main {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .addHeader("X-Api-Key", "d+bEyireTftNMaeXa34joQ==3TzDaf3W6RbPzGXl")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(responseBody).getAsJsonArray();

            if (jsonArray.size() > 0) {
                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                String id = jsonObject.get("id").getAsString();
                String imageUrl = jsonObject.get("url").getAsString();

                File file = new File("./" + id + ".jpg");

                try (InputStream in = new URL(imageUrl).openStream()) {

                    FileUtils.copyInputStreamToFile(in, file);
                    System.out.println("Image downloaded and saved as " + file.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to download image: " + e.getMessage());
                }
            } else {
                System.err.println("No images found in the JSON response.");
            }
        } else {
            System.err.println("Failed to fetch image. HTTP status code: " + response.code());
        }
    }
}