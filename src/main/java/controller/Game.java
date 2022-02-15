package controller;

import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game {
    //Fields - STATIC FINAL VARIABLES
    private static final String os = System.getProperty("os.name").toLowerCase();

    //Fields
    SceneContainer scenes;
    static Person player = new Person();
    Map<String,Person> listPlayer;
    private boolean doesPlayerExist = false;
    boolean isWindows = System.getProperty("os.name").contains("Windows");

    //constructors

    /**
     * Method to start game
     */
    public void execute() {
        //creates a Map Filed for each external category file. Using Scene instances fill in various scenes(Scene Instances)
        scenes = new SceneContainer();
        listPlayer = scenes.getUsers();
        //Prints Welcome ASCII art banner
        welcome();
        //Checks to see if there is a saved file to read in previous score.
//        checkSaveFile();
        String result = retrievePreviousSession();
        System.out.println(result);

        if (!doesPlayerExist) {
            //Initializes Person instance with various prompts to obtain values.
            //It also runs through the backstory sequence
            getPlayerBasicData();
            clearScreen();
            //Runs through Career scene
            runSceneOneCareer(player);
        }


        while (shouldPlay()) {
            clearScreen();

            //Checks to see if player is going through midlife crisis, it has already happened, if two are not true,
            //then it continues with a random scene.
            Scene currentScene = scenes.getNewScene(player);

            //prints out the ASCII art of the specific category of the random scene.
            System.out.println(currentScene.getArt());
            System.out.println("\n+++++++ 5 years later +++++++");
            //For every scene the user goes through age is increased by 5
            System.out.println(player.addAge(5));
            System.out.println(player.getPlayerInformation());
            // handles the individual scene prompting, handling user response and returning index of response.
            int input = prompt(currentScene);
            clearScreen();
            System.out.println(player.getPlayerInformation());
            // display outcome of scene based on user response.
            displayOutcome(input, currentScene);
            // applies effect based on user response
            runEffect(input, currentScene);
            // prints out users net-work, and return string of 5 year summary.
            String salaryReport = player.addSalary();
            System.out.println("\nEnter any key to see your 5-year summary");
            getInput();
            //Display's value of salaryReport
            displaySceneSummary(salaryReport);
            //prompts user to determine if user want to continue game, save game, or quit game
            nextTurnPrompt();
        }
        playAgainOrExit();
    }

    /**
     * Prints out Welcome banner
     *
     * @return Empty String
     */
    private String welcome() {
        String art = "\n" +
                "  /$$$$$$              /$$           /$$$$$$$  /$$           /$$              /$$$$$$                  /$$$$$$$  /$$                 /$$$$$$$$                  /$$                    \n" +
                " /$$__  $$            | $$          | $$__  $$|__/          | $$             /$$__  $$                | $$__  $$|__/                |__  $$__/                 |__/                    \n" +
                "| $$  \\__/  /$$$$$$  /$$$$$$        | $$  \\ $$ /$$  /$$$$$$$| $$$$$$$       | $$  \\ $$  /$$$$$$       | $$  \\ $$ /$$  /$$$$$$          | $$  /$$$$$$  /$$   /$$ /$$ /$$$$$$$   /$$$$$$ \n" +
                "| $$ /$$$$ /$$__  $$|_  $$_/        | $$$$$$$/| $$ /$$_____/| $$__  $$      | $$  | $$ /$$__  $$      | $$  | $$| $$ /$$__  $$         | $$ /$$__  $$| $$  | $$| $$| $$__  $$ /$$__  $$\n" +
                "| $$|_  $$| $$$$$$$$  | $$          | $$__  $$| $$| $$      | $$  \\ $$      | $$  | $$| $$  \\__/      | $$  | $$| $$| $$$$$$$$         | $$| $$  \\__/| $$  | $$| $$| $$  \\ $$| $$  \\ $$\n" +
                "| $$  \\ $$| $$_____/  | $$ /$$      | $$  \\ $$| $$| $$      | $$  | $$      | $$  | $$| $$            | $$  | $$| $$| $$_____/         | $$| $$      | $$  | $$| $$| $$  | $$| $$  | $$\n" +
                "|  $$$$$$/|  $$$$$$$  |  $$$$/      | $$  | $$| $$|  $$$$$$$| $$  | $$      |  $$$$$$/| $$            | $$$$$$$/| $$|  $$$$$$$         | $$| $$      |  $$$$$$$| $$| $$  | $$|  $$$$$$$\n" +
                " \\______/  \\_______/   \\___/        |__/  |__/|__/ \\_______/|__/  |__/       \\______/ |__/            |_______/ |__/ \\_______/         |__/|__/       \\____  $$|__/|__/  |__/ \\____  $$\n" +
                "                                                                                                                                                      /$$  | $$               /$$  \\ $$\n" +
                "                                                                                                                                                     |  $$$$$$/              |  $$$$$$/\n" +
                "                                                                                                                                                      \\______/                \\______/ \n";
        System.out.println(art);
        System.out.println("Welcome to Get Rich Or Die Trying.\nAt a young age you realize that you want to be a millionaire.\nYour mission is to make $1 million before all your health points run out.\nEach choice you make will affect your net worth and health levels.");
        System.out.println("\nPress any key to continue.");
        getInput();
        clearScreen();
        return "";
    }

    /**
     * Method used to determine if user inputs any of the keywords 'help' or 'quit'
     * In addition, it used do capture other user input throughout the game.
     * Optional: if Selections Array is passed in then input can also be compared with these values.
     *
     * @param selections a list of valid selections (Empty Array)
     * @return lower case version of user input
     */
    private String getInput(String... selections) {
        Scanner scanner = new Scanner(System.in);
        //While loop runs until a return is triggered
        while (true) {
            String userInput = scanner.nextLine().trim().toLowerCase();
            //Scenario one: user types 'help' (Case does not matter).
            if (userInput.equalsIgnoreCase("Help"))
                this.helpMenu();
            //Scenario two: User types 'quit' (Case does not matter).
            if (userInput.equalsIgnoreCase("quit")) {
                scenes.saveUsers(player);
                System.out.println("Quitting game.!");
                System.exit(1);
                return "";
            }
            //Scenario three: If no parameter value is passed in then input is simply returned.
            if (selections.length == 0)
                return userInput;
            //Scenario four: If parameter (selection) is passed in then input is compared with selection array options.
            //If match is found then input is sent back
            for (String selection : selections)
                if (userInput.equalsIgnoreCase(selection))
                    return userInput;
            //Scenario five: User input does not match parameter array values, parameter value are printed to provide
            //user with valid options.
            System.out.println("\nInvalid input. Valid options are:\n");
            for (String selection : selections)
                System.out.println(selection);
        }
    }

    /**
     * Method that looks for current users previous session using name as the key and returning a result message.
     *
     * @return String message that defines the result of the search.
     */
    public String retrievePreviousSession() {
        String resultString;
        System.out.println("Enter previous user name...:");
        String playerSavedName = getInput();
        if (listPlayer.containsKey(playerSavedName)) {
            resultString = "\n\nPlayer Found! You will continue where you left off...";
            player = listPlayer.get(playerSavedName);
            //Sets the boolean variable to true, so that player can continue with previous session.
            doesPlayerExist = true;
        } else {
            resultString = "\nPlayer name was not found! New player record will be created.";
        }
        return resultString;
    }

    /**
     * Method used to validate if saveFile.txt exits in local machine.
     */
    public void checkSaveFile() {
        File checkFile = new File("saveFile.txt");
        try {
            if (checkFile.exists()) {
                System.out.println("Enter name of player...");
                String playerSavedName = getInput();
                System.out.println(playerSavedName);
                ReadFile read = new ReadFile("saveFile.txt");
                StringBuilder info = new StringBuilder();
                for (String str : read.getStringArray()) {
                    int i = 0;
                    if (str.toUpperCase().contains(playerSavedName.toUpperCase())) {
                        System.out.println("Found name");
                        for (String str1 : read.getStringArray()) {
                            info.append(str1);
                            info.append("\n");
                            if (str.contains("+") && i > 0) {
                                break;
                            }
                            i++;
                        }
                    }
                }
                String[] infoArray = info.toString().split("\n");
                for (String s : infoArray) {
                    System.out.println(s);
                }
                //System.out.println(read.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method is used to prompt user for name, privilege status, and if they want to go to college.
     * Values are set to the Person Object.
     */
    private void getPlayerBasicData() {

        String printBackstoryArt = Art.getArt("backstory");
        System.out.println(printBackstoryArt);
        System.out.println("Enter your Name: ");
        String playerName = getInput();

        while (playerName.isEmpty()) {
            System.out.println("Name is required. Please enter your name.");
            playerName = getInput();
        }

        if (playerName.equalsIgnoreCase("DEV")) {
            player.setName("DEV");
            player.setPrivilege(true);
            player.setEducation(true);
            player.addStrength(5);
            player.addIntellect(5);
            player.addCreativity(5);
            System.out.println("Playing the game in DEV mode");
            return;
        }
        System.out.println("Select your privilege status (Working Class)/(Middle Class): ");
        //Will continue to prompt user to select one of the options available.
        String getChoice = getInput("working class", "middle class");

        if (getChoice.equalsIgnoreCase("working class")) {
            this.player.setNetWorth(player.getNetWorth() - 25000);
        } else if (getChoice.contains("middle class")) {
            this.player.setNetWorth(player.getNetWorth() + 25000);
        }
        System.out.println("" +
                "You chose: " + getChoice + "\n" +
                "Your Net Worth is: " + player.getPrettyNetWorth() + "\n\n");

        clearScreen();

//        System.out.println(printBackstoryArt);
        //Creates List data structure of the various scenes in the backstory.json file.
        List<Backstory> backstories = getBackStoryScenes();
        //Process each scene in the backstory file and uses user input to update players (Person instance) fields
        processBackstories(backstories);
        System.out.println();
        // TODO: Make this better narrative
        System.out.println("Do you want to go to college? (Y/N): ");
        String educationChoice = getInput("y", "n");

        boolean userWantsCollege = educationChoice.equalsIgnoreCase("y");
        System.out.printf("Your name is %s. You chose to %s college.", playerName, userWantsCollege ? "go to" : "skip");

        if (userWantsCollege)
            player.adjustNetWorth(-100000);

        player.setName(playerName);
        player.setEducation(userWantsCollege);
    }

    /**
     * Method used to read backstory.json (external file). Creates (Backstory class) object for each individual JSONObject and
     * insert them to a List<Backstory> type.
     *
     * @return ArrayList<type = Backstory>
     */
    static List<Backstory> getBackStoryScenes() {
        List<Backstory> backstories = new ArrayList<>();
        JSONArray fileData = readJsonArray("scenes/backstory.json");
        for (Object jsonBackstory : fileData) {
            Backstory backstory = Backstory.fromJson((JSONObject) jsonBackstory);
            backstories.add(backstory);
        }
        return backstories;
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
     * Methods runs through the sequence of Backstory events (read from external file), based on response
     * from each backstory event, player's (Player Object) strength, intellect, or creativity field is updated.
     *
     * @param backstories List datastructures holding Backstory instances.
     */
    private void processBackstories(List<Backstory> backstories) {
        //For loop the handle every backstory scene
        for (Backstory backstory : backstories) {
            //Print out the ASCII art for 'backstory'.
            System.out.println(Art.getArt("backstory"));
            //Print prompt field in Backstory instance
            System.out.println(backstory.getPrompt());
            System.out.println();
            //Print out every option available for backstory scene.
            for (BackstoryOption option : backstory.getOptions())
                System.out.println(option.getText());

            //prompt user to type in one of the selections and returns value of the typed in input
            String resp = getInput(backstory.getBackstoryOptionsText());
            BackstoryOption selectedBackstoryOption = null;
            // checks the List of BackstoryOption instance to see if user response matches text field in instance.
            for (BackstoryOption option : backstory.getOptions()) {
                if (option.getText().contains(resp)) {
                    //if match is found then BackstoryOption instance is assigned to selectedBackstoryObject variable
                    selectedBackstoryOption = option;
                    break;
                }
            }
            System.out.println();
            //Prints out the value in the 'outcome' field inside selectedBackstoryOption (BackstoryOption instance) variable.
            if (selectedBackstoryOption != null) {
                System.out.println(selectedBackstoryOption.getOutcome());
            }
            //Using selectedBackstoryOption (BackstoryOption instance) to obtain 'attribute' field then calling
            //getAttribute() of EffectsTranslator class.
            if (selectedBackstoryOption != null) {
                EffectsTranslator.getAttribute(player, selectedBackstoryOption.getAttribute());
            }
            System.out.println("\nPress any key to continue or help for additional instructions");
            getInput();
            clearScreen();
        }
        //Prints out the current values of each of these field in the Person instance.
        System.out.println("Your character's stats:");
        System.out.println("Strength: " + player.getStrength());
        System.out.println("Intellect: " + player.getIntellect());
        System.out.println("Creativity: " + player.getCreativity());
    }

    /**
     * Method that run through the second phase of events. If player decided to go to college or not.
     * Different career option are presented
     *
     * @param player Person instance.
     */
    private void runSceneOneCareer(Person player) {
        //Using hasEduction field in player (Player Object) to determine which Enum Map from Careers will be returned and assigned to available careers
        Map<Careers, List<String>> availCareers = player.hasEducation() ? Careers.getCollegeCareers() : Careers.getNonCollegeCareers();
        String collegeSummary = player.hasEducation() ? "Congratulations!\nYou finished college." : "You decided to skip the college route.";
        System.out.println(collegeSummary);
        System.out.println("What career do you want?");

        //Cycles through one of the Map fields in Careers class based on hadEducation field in player (Person Object).
        List<String> allValidCareers = new ArrayList<>();
        for (Careers career : availCareers.keySet()) {
            for (String specialty : availCareers.get(career)) {
                System.out.println(specialty);
                allValidCareers.add(specialty);
            }
        }

        String selectedCareer = getInput(allValidCareers);
        //Setts the ENUM (DANGER, KNOWLEDGE, PASSION) value based on user input
        topLoop:
        for (Careers career : availCareers.keySet()) {
            for (String specialty : availCareers.get(career)) {
                if (selectedCareer.equalsIgnoreCase(specialty)) {
                    player.setCareer(career);
                    break topLoop;
                }
            }
        }

        System.out.println("\nYou chose a " + player.getCareer() + " job");

    }

    /**
     * Method used to cycle through a randomly selected scene and prompting user for selection
     * on various options and then returning index of chosen option.
     *
     * @param currentScene Scene instance.
     * @return index of user selected option value.
     */
    private int prompt(Scene currentScene) {
        System.out.println();
        System.out.println(currentScene.getPrompt());
        System.out.println();
        for (String option : currentScene.getOptions())
            System.out.println(option);
        //Using overloading to first pass in List<String> of option and then converting to
        //array to use alternative getInput(varargs)
        String input = getInput(currentScene.getOptions());

        int selectedIndex = 0;

        // currentScene.getOptions.indexOf(input) is case-sensitive and the user might not enter the correct case
        // doing it this way ignores case and still gets the index
        for (String option : currentScene.getOptions()) {
            if (option.equalsIgnoreCase(input))
                break;

            selectedIndex++;
        }


        return selectedIndex;
    }

    /**
     * Method to convert Collection type parameter to Array format, using overloading to
     * call alternative method and passing in array format of options.
     *
     * @param options A List <String> that is holding the options value of a Scene Instance.
     * @return options value from Scene options selection that user selected.
     */
    private String getInput(Collection<String> options) {
        String[] optionsArray = options.toArray(new String[0]);
        return getInput(optionsArray);
    }

    /**
     * Method to display Scent instance outcome based on users options selection.
     *
     * @param index        Index value representing the options selected by user when answering currentScene prompt
     * @param currentScene Scene instance.
     */
    private void displayOutcome(int index, Scene currentScene) {
        System.out.println(currentScene.getOutcomes().get(index));
        System.out.println();
    }

    /**
     * Method that performs effects in Scene object based on options index response by user.
     *
     * @param index        Index value of user input.
     * @param currentScene Scene instance.
     */
    private void runEffect(int index, Scene currentScene) {
        //Using static method to doEffects() to cycle throughout the effect field values in the Scene instance.
        EffectsTranslator.doEffects(player, currentScene.getEffects().get(index));
    }

    /**
     * Method prints a 5-year summary of the player's (Person instance) fields.
     *
     * @param salaryBreakdown net-worth breakdown(String)
     * @return 5-year Summary
     */
    private String displaySceneSummary(String salaryBreakdown) {
        String values = "";
        System.out.println("\n++++++ 5-Year Summary ++++++");
        System.out.println("Player: " + player.getName());
        System.out.println("Age: " + player.getAge());
        System.out.println("Net Worth: " + player.getPrettyNetWorth());
        System.out.println("Health: " + player.getHealthPoints());
        System.out.println("Children: " + player.getChildren());
        if (player.isMarried()) {
            System.out.println("Spouse: Sam");
        } else {
            System.out.println("Partner: " + (player.getPartner() == null ? "none" : "Sam"));
        }
        System.out.println(salaryBreakdown);
        // This is currently being used to output the summary.
        // This can go away when serialization is implemented
        values += ("++++++ 5-Year Summary ++++++");
        values += ("\nPlayer: " + player.getName());
        values += ("\nNet Worth: " + player.getPrettyNetWorth());
        values += ("\nChildren: " + player.getChildren());
        if (player.isMarried()) {
            values += ("\nSpouse: " + player.getPartner());
        } else {
            values += ("\nPartner: " + (player.getPartner() == null ? "none" : player.getPartner().getName()));
        }
        return values;
    }

    /**
     * After random scene is completed then user is prompted to either quit game, save game , or continue with game.
     */
    private void nextTurnPrompt() {
        System.out.println("\nEnter any key to continue or type 'save' to save the game. Or 'quit' to end the game.");
        String askToSave = getInput();

        if (askToSave.equalsIgnoreCase("quit")) {
            System.out.println("Quitting game");
            System.exit(1);
        }

        if (askToSave.equalsIgnoreCase("save")) {
//            WriteFile saveGame = new WriteFile("saveFile.txt", displaySceneSummary(""));
            scenes.saveUsers(player);
//            saveGame.save();
        }
    }

    /**
     * Empty method, It is not doing anything.
     */
    private void playAgainOrExit() {
    }

    /**
     * Method to determine if user can continue with another scene,
     * if user's (Person instance: health field) health value is less than ZERO then player looses
     * If user's netWorth field value is over 1 Million then player wins.
     * If none of these are true player can then go to another scene.
     *
     * @return boolean to determine if user can continue to another scene.
     */
    private boolean shouldPlay() {
        if (player.getHealthPoints() <= 0) {
            System.out.println("Game Over. You died because you ran out of health points: " + player.getHealthPoints());
            return false;
        }

        if (player.getNetWorth() >= 1000000) {
            System.out.println("You win. You have: " + player.getPrettyNetWorth());
            return false;
        }

        return true;
    }

    /**
     * Method used to print out the goals of the game. At any user input request, if user type in help then menu will print out.
     */
    public void helpMenu() {
        System.out.println("Game is meant to simulate life." +
                "\nThe intent of the game is to have 1 million dollars by the end of the game" +
                "\nChoices will change how much money you have, as well as health points." +
                "\nEx: choosing education will grant you an extra money to your salary" +
                "\nbut skipping college will start you out with less debt." +
                "\nChoose carefully, your life depends on it" +
                "\nIf you're done with the help section, press any key to continue.");
        try {
            System.in.read();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    //doesn't clear the scroll bar
    public void clearScreen() {
        ProcessBuilder var0 = os.contains("windows") ? new ProcessBuilder("cmd", "/c", "cls") : new ProcessBuilder("clear");

        try {
            var0.inheritIO().start().waitFor();
        } catch (InterruptedException var2) {
            System.out.println(var2.getMessage());
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public static Person getPlayer() {
        return player;
    }
}


