package models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Backstory {
    //Fields
    private final String prompt;
    private final List<BackstoryOption> options;

    //Constructors
    private Backstory(String prompt, List<BackstoryOption> options) {
        this.prompt = prompt;
        this.options = options;
    }

    //Business Methods

    /**
     * Returns a Backstory object after reading from parameter(JSONObject) value
     *
     * @param json JSON object
     * @return BackStory(Class) Object contains a List of BackStoryOption(Class) options
     */
    public static Backstory fromJson(JSONObject json) {
        // Parse the options from the JSON into the correct datatype
        List<BackstoryOption> options = new ArrayList<>();
        JSONArray optionsJson = json.getJSONArray("options");
        for (Object optionJson : optionsJson) {
            JSONObject parsedOption = (JSONObject) optionJson;
            BackstoryOption bo = new BackstoryOption(
                    parsedOption.getString("text"),
                    parsedOption.getString("attribute"),
                    parsedOption.getString("outcome")
            );
            options.add(bo);
        }

        String prompt = (String) json.get("prompt");

        // Use the parsed data to construct the object
        return new Backstory(prompt, options);
    }

    /**
     * Return a List of the values in the List options field using the key 'text'
     *
     * @return List of the value in the List options field
     * Ex: ['action figure', 'rocket ship', 'crayons']
     */
    public List<String> getBackstoryOptionsText() {
        List<String> optionsText = new ArrayList<>();
        for (BackstoryOption o : options)
            optionsText.add(o.getText());

        return optionsText;
    }

    //Getters and Setters
    public List<BackstoryOption> getOptions() {
        return options;
    }

    public String getPrompt() {
        return prompt;
    }

}
