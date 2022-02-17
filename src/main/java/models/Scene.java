package models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scene {
    //Fields
    private final String prompt;
    private final List<String> options, outcomes;
    private final List<Map<String, Object>> effects;
    private boolean hasBeenUsed = false;
    private String category;

    //Constructors
    public Scene(String prompt, List<String> options, List<String> outcomes, List<Map<String, Object>> effects) {
        this.prompt = prompt;
        this.options = options;
        this.outcomes = outcomes;
        this.effects = effects;
    }

    //Business Methods

    /**
     * Returns a newly created Scene object using data withing the JSONObject.
     * @param json JSONObject in this format
     *                  {
     *                  'prompt' : "......",
     *                  'options' : [".......", "......" , ...],
     *                  'outcomes' : [".......", "......" , ...],
     *                  'effects': [{'key': 'value'}, {'key': 'value'} , ...]
     *                  }
     *
     * @return newly created Scene Object.
     */
    public static Scene fromJson(JSONObject json) {
        List<String> options = new ArrayList<>();
        for (Object option : json.getJSONArray("options"))
            options.add(option.toString());

        List<String> outcomes = new ArrayList<>();
        for (Object outcome : json.getJSONArray("outcomes"))
            outcomes.add(outcome.toString());

        List<Map<String, Object>> effects = new ArrayList<>();

        if (json.has("effects")) {
            for (Object effect : json.getJSONArray("effects"))
                effects.add(((JSONObject) effect).toMap());
        }

        return new Scene(json.getString("prompt"), options, outcomes, effects);
    }

    /**
     * Returns ASCII art using Art class static method 'getArt()'.
     * Inside method: desired category (String) name (Available names: backstory, career, children,
     * education, health, partner, privilege) is passed to static method.
     * @return ASCII art as a String
     */
    public String getArt() {
        return Art.getArt(category);
    }

    //Setters and Getters

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public List<Map<String, Object>> getEffects() {
        return effects;
    }

    public void setHasBeenUsed(boolean b){
        this.hasBeenUsed = b;
    }

    public boolean hasBeenUsed() {
        return this.hasBeenUsed;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
