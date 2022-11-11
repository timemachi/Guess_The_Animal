package animals.model;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Animal {
    private final String animal;
    private final Matcher matcher;
    private final ResourceBundle rbPatterns = System.getProperty("user.language").equals("eo") ?
            ResourceBundle.getBundle("patterns", new Locale("eo")) :
            ResourceBundle.getBundle("patterns", Locale.getDefault());

    public Animal(String animal) {
        this.animal = animal.toLowerCase().trim();
        Pattern pattern = Pattern.compile(rbPatterns.getString("animal.isCorrect"), Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(this.animal);
    }

    public String nameWithArticle() {
        if (animal.matches(rbPatterns.getString("animal.1.pattern"))) {
            return animal.replaceFirst(rbPatterns.getString("animal.1.pattern"), rbPatterns.getString("animal.1.replace"));
        } else if (animal.matches(rbPatterns.getString("animal.2.pattern"))) {
            return animal.replaceFirst(rbPatterns.getString("animal.2.pattern"), rbPatterns.getString("animal.2.replace"));
        } else if (animal.matches(rbPatterns.getString("animal.3.pattern"))) {
            return animal.replaceFirst(rbPatterns.getString("animal.3.pattern"), rbPatterns.getString("animal.3.replace"));
        } return animal;
    }
    public boolean isValid() {
        return matcher.find();
    }

    public String definiteName() {
        return nameWithArticle().replaceFirst(rbPatterns.getString("definite.1.pattern"), rbPatterns.getString("definite.1.replace"));
    }

    public String animalName() {
        return animal.replaceFirst(rbPatterns.getString("animalName.1.pattern"), rbPatterns.getString("animalName.1.replace"));
    }

//    public String nameWithoutArticle() {
//        return animal.replaceFirst("(an?|the)\\s+", "");
//    }
}
