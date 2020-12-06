package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

@RestController
public class ItineraryController{

    private String target = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
    private String access_token = "pk.eyJ1IjoiamFpbmFrc2hhdDU0NiIsImEiOiJja2k5OHFndjQwZGg3MnRsY3d2cHV5N2h0In0.rXewv5rwoCRzvu1bhqbuQQ";
    private String travel_query = "Istanbul museum";
    private String country_code = "tr";
    private String language_code = "en";
    private String types = "poi";
    private String limit = "10";


    @GetMapping("/itinerary")
    public ArrayList<PlaceClass> getItinerary() throws IOException{
        ArrayList<PlaceClass> placeList = new ArrayList<PlaceClass>();
        boolean flag = true;
        final String uri = target + travel_query + ".json?" + "limit=" + limit + "&country=" + country_code + "&language=" + language_code + "&types=" + types + "&access_token=" + access_token;
        RestTemplate restTemplate = new RestTemplate();

        JsonNode result = restTemplate.getForObject(uri, JsonNode.class);

        if(result == null){
            System.err.println("Wrong response");
            return placeList;
        }
        
        if(result.has("message")){
            System.err.println(result.get("message").toString());
            return placeList;
        }
        if(!result.has("features")){
            System.err.println("No Features");
            return placeList;
        }
        
        JsonNode features = result.get("features");
        if (features.isArray()){
            for (JsonNode node : features) {
                String name = "";
                if(node.has("text_en")){
                    name = node.get("text_en").toString().replaceAll("^\"|\"$", "");
                }
                String[] categories = {};
                String region = "";

                try {
                    JsonNode properties = node.get("properties");
                    if ( properties != null && !properties.isEmpty()){
                        String category = properties.get("category").toString().replaceAll("^\"|\"$", "");
                        categories = category.split(",");
                    }        
                } catch (Exception e) {
                    //TODO: handle exception
                }
                
                try {
                    JsonNode context = node.get("context");
                    if(context.isArray() && !context.isEmpty()){
                        ArrayNode contextArray = (ArrayNode) context;
                        region = contextArray.get(0).get("text_en").toString().replaceAll("^\"|\"$", "");
                        String city = contextArray.get(1).get("text_en").toString().replaceAll("^\"|\"$", "");
                        if(!city.equals("Istanbul")){
                            flag = false;
                        }
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                }
                
                if(flag == true){
                    PlaceClass place = new PlaceClass(name, categories, region);
                    placeList.add(place);
                }
                
            }
        }
        return placeList;
    }
}
