package models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SceneContainer {
    //Fields
    private final Random random = new Random();
    private List<Map<String, List<Scene>>> categories;
    private Map<String, List<Scene>> midLifeCrisis;
    private Map<String, Person> users;
    private List<JSONObject> investingScenes;

    //Constructors
    public SceneContainer() {
        setCategories(loadCategories());
        setUsers(loadUsers(getDataBasePath()));
        setMidLifeCrisis(loadScenes("midlifeCrisis", "true", "false"));
        setInvestingScenes(loadInvestmentScenes());
    }

    //Business Methods

    /**
     * Method to add the different categories into List after reading from external file.
     *
     * @return List of type Map with key: String, value: List<Scene>.
     */
    private List<Map<String, List<Scene>>> loadCategories() {
        List<Map<String, List<Scene>>> categoryHolder = new ArrayList<>();
        categoryHolder.add(loadScenes("career", "danger", "knowledge", "passion"));
        categoryHolder.add(loadScenes("education", "true", "false"));
        categoryHolder.add(loadScenes("partner", "married", "single", "partner"));
        categoryHolder.add(loadScenes("privilege", "true", "false"));
        categoryHolder.add(loadScenes("children", "true", "false"));
        categoryHolder.add(loadScenes("health", "true", "false"));

        return categoryHolder;
    }

    /**
     * @param category      Name of category (Using name of file as category name)
     * @param subcategories String name of subcategories
     * @return Map of subcategories (each contain a list of Scene Objects) from each category (Using subcategories as key)
     * returned Map Format:
     *   {
     *              'key'               'value'
     *      'subcategory name' :[ Scene Object, Scene Object, .....],
     *      'subcategory name' : [ Scene Object, Scene Object, .....],
     *      more.........
     *  }
     */
    private Map<String, List<Scene>> loadScenes(String category, String... subcategories) {
        JSONObject externalData = readJsonObject("scenes/" + category + ".json");
        Map<String, List<Scene>> tempMap = new HashMap<>();
        for (String subcategory : subcategories) {
            List<Scene> subCategoryScenes = new ArrayList<>();
            for (Object sceneObject : externalData.getJSONArray(subcategory)) {
                JSONObject definitelyJson = (JSONObject) sceneObject;
                Scene newScene = Scene.fromJson(definitelyJson); //calls method to create Scene Object
                newScene.setCategory(category);
                subCategoryScenes.add(newScene);
            }
            tempMap.put(subcategory, subCategoryScenes);
        }
        return tempMap;
    }

    /**
     * Method that reads external file that contains the investment scenes.
     *
     * @return List the contains JSONObject's.
     */
    public List<JSONObject> loadInvestmentScenes() {
        List<JSONObject> gamblingScenes = new ArrayList<>();
        JSONArray gamblingObjectScenes = readJsonArray("scenes/gambling.json");
        for (Object gamblingObject : gamblingObjectScenes) {
            JSONObject JSONGamblingObject = (JSONObject) gamblingObject;
            gamblingScenes.add(JSONGamblingObject);
        }
        return gamblingScenes;
    }

    /**
     * Method will read external file containing previous users. If not file is found then new file is created.
     */
    public Map<String, Person> loadUsers(String fileName) {
        //name of file to store users.

        //Temporary Map to hold previous user when they are being read from external file.
        HashMap<String, Person> userLoader = new HashMap<>();
        String name;
        int netWorth, health, age, children, strength, intellect, creativity;
        boolean education, isMarried, hasPrivilege;
        Careers career;
        Person partner;
        JSONObject currentInvestment;
        boolean midLifeCrisis, finishedInitialization, isCurrentlyInvesting;
        int investmentAmount;
        File userFile = new File(fileName);
        try {
            //If file does not exist then it will create it and if it already there then it will read from it.
            if (!userFile.createNewFile()) {
                JSONObject userData = readJsonObject(fileName);
                for (String playerKey : userData.keySet()) {
                    JSONObject player = userData.getJSONObject(playerKey);
                    name = player.getString("name");
                    netWorth = Integer.parseInt(player.get("netWorth").toString());
                    health = Integer.parseInt(player.get("health").toString());
                    age = Integer.parseInt(player.get("age").toString());
                    children = Integer.parseInt(player.get("children").toString());
                    strength = Integer.parseInt(player.get("strength").toString());
                    intellect = Integer.parseInt(player.get("intellect").toString());
                    creativity = Integer.parseInt(player.get("creativity").toString());
                    education = Boolean.parseBoolean(player.get("education").toString());
                    isMarried = Boolean.parseBoolean(player.get("isMarried").toString());
                    hasPrivilege = Boolean.parseBoolean(player.get("hasPrivilege").toString());
                    career = Careers.valueOf(player.get("career").toString());
                    midLifeCrisis = Boolean.parseBoolean(player.get("midLifeCrisis").toString());
                    finishedInitialization = Boolean.parseBoolean(player.get("finishedInitialization").toString());
                    isCurrentlyInvesting = Boolean.parseBoolean(player.get("isCurrentlyInvesting").toString());
                    investmentAmount = Integer.parseInt(player.get("investmentAmount").toString());




                    //If players partner value is null then a new Person object is create that does not include partner parameter
                    if ("null".equals(player.get("partner").toString())) {
                        partner = null;
                    }
                    //If partner does exist then a new Person instance is created with the partners name and netWorth as parameters.
                    else {
                        JSONObject playersPartner = player.getJSONObject("partner");
                        String partnerName = playersPartner.getString("name");
                        int partnerNetWorth = Integer.parseInt(playersPartner.getString("netWorth"));
                        partner = new Person(partnerName, partnerNetWorth);
                    }
                    if ("null".equals(player.get("currentInvestment").toString())){
                        currentInvestment = null;
                    }
                    else {
                        currentInvestment = player.getJSONObject("currentInvestment");
                    }

                    userLoader.put(name, new Person(netWorth, health, age, children, strength, intellect, creativity, education, isMarried, hasPrivilege, career, partner, name, currentInvestment, midLifeCrisis, finishedInitialization, isCurrentlyInvesting, investmentAmount));
                }

            } else {
                WriteFile userWriter = new WriteFile("userStorage.json", "{}");
                userWriter.save();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //sets users field using userLoader variable
        return userLoader;

    }

    /**
     * Method used to save the users map field into external file as a Json object
     *
     * @param person Current player object
     */
    public void saveUsers(Person person) {
        //Outer JSONObject is created
        JSONObject jsonObject = new JSONObject();
        //Default content to write to external file
        String result = "{}";
        //When user prompts to quit if the new Person object has not been created then new Object is not included in map of users
        if (person.getName() != null) {
            getUsers().put(person.getName(), person);
        }
        //If users Map filed is empty then external file is saved with default value
        if (!getUsers().isEmpty()) {
            for (String userKey : getUsers().keySet()) {

                Person player = getUsers().get(userKey);

                Person playersPartner;

                String partnerString = "null";
                if (player.getPartner() != null) {
                    playersPartner = player.getPartner();
                    partnerString = new JSONObject()
                            .put("name", String.valueOf(playersPartner.getName()))
                            .put("netWorth", String.valueOf(playersPartner.getNetWorth())).toString();
                }
                String currentInvestmentString = "null";
                if (player.getCurrentInvestment() != null) {
                    currentInvestmentString = player.getCurrentInvestment().toString();
                }


                String playerString = new JSONObject()
                        .put("name", String.valueOf(player.getName()))
                        .put("netWorth", String.valueOf(player.getNetWorth()))
                        .put("health", String.valueOf(player.getHealth()))
                        .put("age", String.valueOf(player.getAge()))
                        .put("children", String.valueOf(player.getChildren()))
                        .put("strength", String.valueOf(player.getStrength()))
                        .put("intellect", String.valueOf(player.getIntellect()))
                        .put("creativity", String.valueOf(player.getCreativity()))
                        .put("education", String.valueOf(player.hasEducation()))
                        .put("isMarried", String.valueOf(player.isMarried()))
                        .put("hasPrivilege", String.valueOf(player.getHasPrivilege()))
                        .put("career", String.valueOf(player.getCareer()))
                        .put("partner", partnerString)
                        .put("midLifeCrisis", String.valueOf(player.isMidLifeCrisis()))
                        .put("finishedInitialization", String.valueOf(player.isFinishedInitialization()))
                        .put("isCurrentlyInvesting", String.valueOf(player.isCurrentlyInvesting()))
                        .put("investmentAmount", String.valueOf(player.getInvestmentAmount()))
                        .put("currentInvestment", currentInvestmentString).toString();
                jsonObject.put(userKey, playerString);
            }
            result = jsonObject.toString().replace("\"{", "{").replace("\\", "").replace("}\",\"", "},\"").replace("}\"}", "}}");


        }
        //Creates new WriteFile object passing is name of external file and content being written
        SceneContainer.WriteFile userWriter = new SceneContainer.WriteFile("userStorage.json", result);
        //Save to external file.
        userWriter.save();
    }

    /**
     * Return JSONObject after reading an external .json file
     *
     * @param path Path of external file
     * @return JSONObject which can then have values unpacked
     */
    public static JSONObject readJsonObject(String path) {
        File file = new File(path);
        StringBuilder jsonString = new StringBuilder();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine())
                jsonString.append(reader.nextLine());

        } catch (Exception e) {
            System.out.println("Error occurred while loading scenes: " + e);
        }


        return new JSONObject(jsonString.toString());
    }

    /**
     * Method used to read external json files.
     *
     * @param path String representation of path to desired file
     * @return JSONArray with data from external file.
     */
    private static JSONArray readJsonArray(String path) {
        File file = new File(path);
        StringBuilder jsonString = new StringBuilder();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine())
                jsonString.append(reader.nextLine());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new JSONArray(jsonString.toString());
    }

    /**
     * Method to retrive a midlife crisis scene or a random from other categories
     *
     * @param player Person object of the current player
     * @return Scene object of the categories or midlife crisis scenes.
     */
    public Scene getNewScene(Person player) {
        Scene result;
        int randomNumber = random.nextInt(3);
        if (player.getAge() > 40 && player.isMidLifeCrisis() && randomNumber == 2) {
            result = getMidLifeCrisisScene("true");
            player.setMidLifeCrisis(true);
        } else {
            //A random category is selected , available option: career, children, education, health, partner, privilege
            result = getRandomScene(player);
        }
        return result;
    }

    /**
     * Returns a random Scene object based on random value
     *
     * @param player Using Person object to determine
     * @return Scene object that was randomly selected.
     */
    public Scene getRandomScene(Person player) {

        Scene sceneToUse;
        int attemptsToRandomize = 0;
        final int MAX_ATTEMPTS = 100;
        do {
            int categoryIndex = random.nextInt(getCategories().size()); // Get random number based on size of the categories field
            Map<String, List<Scene>> category = getCategories().get(categoryIndex); // Get the category of scene to use
            String subCategoryKey = player.getCategoryValue(categoryIndex); // Get the subcategory key (the value of the corresponding player variable)
            List<Scene> subCategoryScenes = category.get(subCategoryKey); // Get the list of scenes based on the subCategoryKey
            int sceneIndex = random.nextInt(subCategoryScenes.size()); // Get a random index based on the subcategory size
            sceneToUse = subCategoryScenes.get(sceneIndex);
            attemptsToRandomize++;

            if (attemptsToRandomize > MAX_ATTEMPTS) {
                //System.out.println("Exceeded max attempts; Unlocking all scenes");
                setAllScenesToUnused(); // We have tried to get an unused scene too many times. Unlock them all, makes them all available
            }

        } while (sceneToUse.hasBeenUsed());

        //System.out.println("Picked random scene after " + attemptsToRandomize + " attempts");

        sceneToUse.setHasBeenUsed(true); // Need to set this to true to make sure it is not used
        return sceneToUse;
    }

    /**
     * Method to get Midlife crisis scene
     *
     * @param key 'true' or 'false': If true player encounters midlife crisis for the first time. If false this is the second time.
     * @return Scene object of true or false scene (scene is based on Person's midlifeCrisis field)
     */
    public Scene getMidLifeCrisisScene(String key) {
        return getMidLifeCrisis().get(key).get(0);
    }

    /**
     * Method that randomly selects an investment scene.
     *
     * @return JSONObject the contains the investment scene details.
     */
    public JSONObject getInvestmentScene() {
        int randomInt = random.nextInt(getInvestingScenes().size());
        return getInvestingScenes().get(randomInt);
    }

    /**
     * Setts all Scene objects 'hasBeenUsed' field to false
     */
    private void setAllScenesToUnused() {
        for (Map<String, List<Scene>> category : getCategories())
            for (String subcategory : category.keySet())
                for (Scene scene : category.get(subcategory))
                    scene.setHasBeenUsed(false);
    }

    //Setter and Getters
    public Map<String, Person> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Person> users) {
        this.users = users;
    }

    public String getDataBasePath() {
        return "userStorage.json";
    }

    public Map<String, List<Scene>> getMidLifeCrisis() {
        return midLifeCrisis;
    }

    public void setMidLifeCrisis(Map<String, List<Scene>> midLifeCrisis) {
        this.midLifeCrisis = midLifeCrisis;
    }

    public List<JSONObject> getInvestingScenes() {
        return investingScenes;
    }

    public void setInvestingScenes(List<JSONObject> investingScenes) {
        this.investingScenes = investingScenes;
    }

    public List<Map<String, List<Scene>>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, List<Scene>>> categories) {
        this.categories = categories;
    }
    // Static inner class to write files.
    public static class WriteFile {
        //Fields
        String fileName;
        String content;

        //Constructors
        public WriteFile(String fileName, String content)
        {
            this.fileName = fileName;
            this.content = content;
        }

        //Business Methods

        /*
         * Saves Current players 5-year Summary if file already exits
         */
        public void saveFile()
        {
            try
            {
                BufferedWriter write = new BufferedWriter(new FileWriter(fileName, false));
                write.write(content);
                write.close();
                System.out.println("Game has been updated.");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        /*
         * Makes a check using createNewFile() method to see if abstract file path does not exist and
         * uses existing file to update with new data by calling method saveFile()
         * Saves players 5-year summary
         */
        public void save() {
            File file = new File(fileName);
            try
            {
                if(file.createNewFile())
                {
                    System.out.println("Your game has been saved .");
                    FileWriter writeFile = new FileWriter(fileName);
                    writeFile.write(content);
                    writeFile.close();
                }
                else
                {
                    saveFile();
                }
            }
            catch(Exception e)
            {
                System.out.println("Can't save new file");
            }
        }

    }
}