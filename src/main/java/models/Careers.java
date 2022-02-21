package models;


import java.util.List;
import java.util.Map;

public enum Careers {

    DANGER("Dangerous", "These job fields usually come with some level of danger and can at times be low paying professions."),
    KNOWLEDGE("Knowledge", "These job fields ofter require a College degree and are well paid but can be stressful environments."),
    PASSION("Passionate", "These job fields are very satisfying but require a great deal of passion and dedication.");

    private final String careerName;
    private final String description;

    Careers(String name, String description) {
        this.careerName = name;
        this.description = description;
    }

    /*
     * Creates a field that holds a Map of college careers using Careers ENUM value as key and List<String> as value
     */
    private static final Map<Careers, List<String>> collegeCareers = Map.of(
            DANGER, List.of("ANTARCTIC EXPLORER", "ASTRONAUT", "FIGHTER PILOT"),
            KNOWLEDGE, List.of("DOCTOR", "PROFESSOR", "ACCOUNTANT"),
            PASSION, List.of("CHARITY WORKER", "MUSEUM CURATOR", "PHILOSOPHER")
    );

    /*
     * Creates a field that holds a Map of non-college careers using Careers ENUM value as key and List<String> as value
     */
    private static final Map<Careers, List<String>> nonCollegeCareers = Map.of(
            DANGER, List.of("UNDERWATER SEA WELDER", "TREE CUTTER", "MARINE"),
            KNOWLEDGE, List.of("DATA ENTRY", "PLUMBER", "SOFTWARE ENGINEER"),
            PASSION, List.of("SCULPTOR", "MUSICIAN", "PERFORMER")
    );

    /*
     * Creates a field that holds a Map of salaries using Careers ENUM value as key and Integer as value
     */
    private static final Map<Careers, Integer> salaries = Map.of(
            DANGER, 20000,
            KNOWLEDGE, 15000,
            PASSION, 10000
    );


    //Getter and Setters
    public static Map<Careers, List<String>> getCollegeCareers() {
        return collegeCareers;
    }

    public static Map<Careers, List<String>> getNonCollegeCareers() {
        return nonCollegeCareers;
    }

    public int getSalaryAmount() {
        return salaries.get(this);
    }

    public String getCareerName() {
        return careerName;
    }

    public String getDescription() {
        return description;
    }
}

