package models;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Art {
    //Fields
    private static final Map<String, String> artMap = loadArt();
    //Constructors


    //Business Method

    /**
     * Reads from art.json file to load ASCII art to artMap field above
     *
     * @return A Map with category name as key and the ASCII art as the value.
     */
    private static Map<String, String> loadArt() {

        Map<String, String> tempArtMap = new HashMap<>();
        //Using static method readJsonObject() from SceneContainer class to read from external file and returning JSONObject
        JSONObject fileData = SceneContainer.readJsonObject("scenes/art" + ".json");

        for (String category : fileData.keySet()) {
            tempArtMap.put(category, fileData.getString(category));
        }
        return tempArtMap;
    }

    /**
     * Returns ASCII art from Map (artMap field) using the (String) name of category as key
     *
     * @param category Name of desired category name :Available names: backstory, career, children, education, health, partner, privilege.
     * @return ASCII art as a String
     */
    public static String getArt(String category) {
        return artMap.get(category);
    }

    //Setters and Getters
}
