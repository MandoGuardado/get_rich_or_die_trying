package controller;

import models.Person;
import java.util.Map;

public class EffectsTranslator {

    /**
     * No Return value method completes a player action based on effects in Scene instance.
     * @param player Person instance
     * @param effect Scene instance 'effects' field Map values.
     */
    public static void doEffects(Person player, Map<String, Object> effect) {

        for (String action : effect.keySet()) {
            String message="";
            action = action.trim();
            int value = (int) effect.get(action);

            switch (action) {
                case "money":
                    message = player.adjustNetWorth(value);
                    break;
                case "health":
                    message = player.adjustHealth(value);
                    break;
                case "dating":
                    message = player.addPartner(value);
                    break;
                case "breakup":
                    message = player.breakUp();
                    break;
                case "marry":
                    message = player.marryPartner();
                    break;
                case "divorce":
                    message = player.addDivorce();
                    break;
                case "addchild":
                    message = player.addChild(value);
                    break;
                case "career":
                    message = player.changeCareer(value);
                    break;
                default:
                    System.out.println("There is no valid action for the following effect: " + action + " with value " + value);

            }
            System.out.println(message);
        }

    }

    /**
     * Parameter (attribute) determine which field in the Person instance is incremented.
     * @param player parameter(Player Object)
     * @param attribute parameter(String) Options: 'strength', 'intellect', 'creativity'
     */
    public static void getAttribute(Person player, String attribute) {

        switch (attribute) {
            case "strength":
                player.addStrength(1);
                break;
            case "intellect":
                player.addIntellect(1);
                break;
            case "creativity":
                player.addCreativity(1);
                break;
            default:
                System.out.println("some error occurred");
                break;
        }

    }
}


