import controller.Game;
import models.Careers;
import models.Person;
import org.junit.jupiter.api.Test;

public class testerAnvy {


    Person player = new Person();

    @Test
    public void intro() {
//        System.out.println(Game.welcome());
    }

    @Test
    public void career() {
//        System.out.println(Game.runSceneOneCareer(player));
    }

    @Test
    public void salary() {
        player.setCareer(Careers.DANGER);
        player.setEducation(true);
        player.addStrength(4);
        player.addSalary();
    }

    @Test
    public void incomeMultiplier() {
        player.addCreativity(2);
        //System.out.println(player.getIncomeMultiplier());
    }

    @Test
    public void divorce() {
        System.out.println(player.getPartner());
        player.addPartner(1);
        System.out.println(player.getPartner());
        player.marryPartner();
        player.isMarried();
        player.addDivorce();
        System.out.println(player.isMarried());

    }



}
