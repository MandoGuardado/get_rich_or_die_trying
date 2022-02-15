import models.Person;
import models.Scene;
import models.SceneContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Tester {

    @Test
    public void testWhatever() {
        Person p = new Person();
        for (int i = 0; i < 20; i++) {
            p.addAge(5);
        }

    }

    @Test
    public void testSceneRandomizer() {
        SceneContainer scenes = new SceneContainer();
        Person player = new Person();
        player.addChild(1);
        Map<Integer, Integer> results = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("Test " + i);
            Scene scene = scenes.getRandomScene(player);

            // Count the number of times each scene appears
            int hash = scene.getPrompt().hashCode();
            if (results.containsKey(hash)) {
                int count = results.get(hash);
                count++;
                results.put(hash, count);
            } else {
                results.put(hash, 1);
            }
        }
        System.out.println("Results ------");
        for (Integer key : results.keySet())
            System.out.println(key + ": " + results.get(key));

    }


}
