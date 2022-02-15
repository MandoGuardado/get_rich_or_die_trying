import controller.EffectsTranslator;
import models.Person;
import models.Scene;
import models.SceneContainer;
import org.junit.jupiter.api.Test;

public class EffectsTranslatorTest {

    @Test
    public void testDoEffect() {
        Person player = new Person();
        SceneContainer sceneContainer = new SceneContainer();
        Scene scene = sceneContainer.getRandomScene(player);
        EffectsTranslator.doEffects(player, scene.getEffects().get(0));
    }

}
