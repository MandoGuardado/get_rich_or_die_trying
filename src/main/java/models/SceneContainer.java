package models;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SceneContainer {
    //Fields
    private final Random random = new Random();
    private final List<Map<String, List<Scene>>> categories = new ArrayList<>();
    private  Map<String, List<Scene>> midLifCrisis;
    private Map<String, Person> users;
    private String dataBasePath = "userStorage.json";

    //Constructors
    public SceneContainer() {
        Map<String, List<Scene>> career = loadScenes("career", "danger", "knowledge", "passion");
        Map<String, List<Scene>> education = loadScenes("education", "true", "false");
        Map<String, List<Scene>> partner = loadScenes("partner", "married", "single", "partner");
        Map<String, List<Scene>> privilege = loadScenes("privilege", "true", "false");
        Map<String, List<Scene>> children = loadScenes("children", "true", "false");
        Map<String, List<Scene>> health = loadScenes("health", "true", "false");
        Map<String, List<Scene>> midLifeSceneHolder = loadScenes("midlifeCrisis", "true", "false");



        categories.add(career);
        categories.add(education);
        categories.add(partner);
        categories.add(privilege);
        categories.add(health);
        categories.add(children);
        loadUsers(getDataBasePath());
        setMidLifCrisis(midLifeSceneHolder);

    }

    //Business Methods

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
        JSONObject fileData = readJsonObject("scenes/" + category + ".json");
        Map<String, List<Scene>> tempMap = new HashMap<>();

        for (String subcategory : subcategories) {
            List<Scene> subCategoryScenes = new ArrayList<>();
            for (Object sceneObject : fileData.getJSONArray(subcategory)) {
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
            int categoryIndex = random.nextInt(categories.size()); // Get random number based on size of the categories field
            Map<String, List<Scene>> category = categories.get(categoryIndex); // Get the category of scene to use
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
     * Setts all Scene objects 'hasBeenUsed' field to false
     */
    private void setAllScenesToUnused() {
        for (Map<String, List<Scene>> category : categories)
            for (String subcategory : category.keySet())
                for (Scene scene : category.get(subcategory))
                    scene.setHasBeenUsed(false);
    }

    /**
     * Method will read external file containing previous users. If not file is found then new file is created.
     */
    public void loadUsers(String fileName) {
        //name of file to store users.

        //Temporary Map to hold previous user when they are being read from external file.
        HashMap<String, Person> userLoader = new HashMap<>();
        String name;
        int netWorth, health, age, children, strength, intellect, creativity;
        boolean education, isMarried, hasPrivilege;
        Careers career;
        Person partner;
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
                    //If players partner value is null then a new Person object is create that does not include partner parameter
                    if ("null".equals(player.get("partner").toString())) {
                        userLoader.put(name, new Person(netWorth, health, age, children, strength, intellect, creativity, education, isMarried, hasPrivilege, career, name));

                    }
                    //If partner does exist then a new Person instance is created with the partners name and netWorth as parameters.
                    else {
                        JSONObject playersPartner = player.getJSONObject("partner");
                        String partnerName = playersPartner.getString("name");
                        int partnerNetWorth = Integer.parseInt(playersPartner.getString("netWorth"));
                        partner = new Person(partnerName, partnerNetWorth);
                        userLoader.put(name, new Person(netWorth, health, age, children, strength, intellect, creativity, education, isMarried, hasPrivilege, career, partner, name));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sets users field using userLoader variable
        setUsers(userLoader);

    }

    /**
     * Method used to save the users map field into external file as a Json object
     * @param person Current player object
     */
    public void saveUsers(Person person) {
        //Outer JSONObject is created
        JSONObject jsonObject = new JSONObject();
        //Default content to write to external file
        String result = "{}";
        //When user prompts to quit if the new Person object has not been created then new Object is not included in map of users
        if (person.getName() != null) {
            System.out.println(person);
            getUsers().put(person.getName(), person);
        }
        //If users Map filed is empty then external file is saved with default value
        if (!getUsers().isEmpty()){
            for (String userKey : users.keySet()) {

                Person player = users.get(userKey);

                Person playersPartner;

                String partnerString = "null";
                if (player.getPartner() != null) {
                    playersPartner = player.getPartner();
                    partnerString = new JSONObject()
                            .put("name", String.valueOf(playersPartner.getName()))
                            .put("netWorth", String.valueOf(playersPartner.getNetWorth())).toString();
                }


                String playerString = new JSONObject()
                        .put("name", String.valueOf(player.getName()))
                        .put("netWorth", String.valueOf(player.getNetWorth()))
                        .put("health", String.valueOf(player.getHealthPoints()))
                        .put("age", String.valueOf(player.getAge()))
                        .put("children", String.valueOf(player.getChildren()))
                        .put("strength", String.valueOf(player.getStrength()))
                        .put("intellect", String.valueOf(player.getIntellect()))
                        .put("creativity", String.valueOf(player.getCreativity()))
                        .put("education", String.valueOf(player.hasEducation()))
                        .put("isMarried", String.valueOf(player.isMarried()))
                        .put("hasPrivilege", String.valueOf(player.getHasPrivilege()))
                        .put("career", String.valueOf(player.getCareer()))
                        .put("partner", partnerString).toString();

                jsonObject.put(userKey, playerString);
            }
            result = jsonObject.toString().replace("\"{", "{").replace("\\", "").replace("}\",\"", "},\"").replace("}\"}", "}}");


        }
        //Creates new WriteFile object passing is name of external file and content being written
        WriteFile userWriter = new WriteFile("userStorage.json", result);
        //Save to external file.
        userWriter.save();
    }

    /**
     * Method to get Midlife crisis scene
     * @param key 'true' or 'false': If true player encounters midlife crisis for the first time. If false this is the second time.
     * @return Scene object of true or false scene (scene is based on Person's midlifeCrisis field)
     */
    public Scene getMidLifeCrisisScene(String key){
        return getMidLifCrisis().get(key).get(0);
    }

    /**
     * Method to retrive a midlife crisis scene or a random from other categories
     * @param player Person object of the current player
     * @return Scene object of the categories or midlife crisis scenes.
     */
    public Scene getNewScene(Person player){
        Scene result;
        int randomNumber = random.nextInt(3);
        if (player.getAge() > 40 && player.isMidLifeCrisis() && randomNumber == 2){
            result = getMidLifeCrisisScene("true");
            player.setMidLifeCrisis(true);
        }
        else if (player.getAge() > 40 && randomNumber == 2){
            result = getMidLifeCrisisScene("false");
        }
        else {
            //A random category is selected , available option: career, children, education, health, partner, privilege
            result = getRandomScene(player);
        }
        return result;
    }

    //Setter and Getters
    public Map<String, Person> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Person> users) {
        this.users = users;
    }

    public String getDataBasePath() {
        return dataBasePath;
    }

    public Map<String, List<Scene>> getMidLifCrisis() {
        return midLifCrisis;
    }

    public void setMidLifCrisis(Map<String, List<Scene>> midLifCrisis) {
        this.midLifCrisis = midLifCrisis;
    }
}