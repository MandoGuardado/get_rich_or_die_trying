import models.Careers;
import models.Person;
import models.Scene;
import models.SceneContainer;
import org.junit.jupiter.api.Test;

public class SceneContainerTest {

    @Test
    public void testGetRandomScene(){
        Person player = new Person();
        player.setCareer(Careers.PASSION);
        player.setEducation(true);
        System.out.println("Children: " + player.getChildren());
        Scene scene = new SceneContainer().getRandomScene(player);
        System.out.println(scene.getPrompt());
    }

    @Test
    public void testAge(){
        Person p = new Person();
        p.addAge(1);
        p.addAge(5);
    }
}
