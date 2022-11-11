package animals.formatter;

import animals.model.Animal;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactFormatter {

    public boolean isCanFind() {
        return canFind;
    }
    private final boolean canFind;
    private final String fact;

    private final ResourceBundle rb = System.getProperty("user.language").equals("eo") ?
            ResourceBundle.getBundle("patterns", new Locale("eo")) :
            ResourceBundle.getBundle("patterns", Locale.getDefault());

    public FactFormatter(String fact) {

        //https://jenkov.com/tutorials/java-regex/matcher.html how use group() in matcher: group = regular expression in ()
        //Pattern pattern = Pattern.compile("(It)\\s+(can|has|is)\\s+([^.]+)", Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern.compile(rb.getString("statement.isCorrect"), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fact);
        canFind = matcher.find();
        this.fact = fact.toLowerCase();
    }

    public String format(Animal animal, boolean isFact) {
        if (!canFind) {
            return "";
        }
        String statement = positiveFact();
        if (!isFact) {
            statement = negativeFact();
        }

        statement = statement.replaceFirst(rb.getString("animalFact.1.pattern"), rb.getString("animalFact.1.replace"));
        return String.format(statement, animal.definiteName());
    }

    public String negativeFact() {
        if (fact.matches(rb.getString("negative.1.pattern"))) {
            return fact.replaceFirst( rb.getString("negative.1.pattern"),  rb.getString("negative.1.replace"));
        } else if (fact.matches(rb.getString("negative.2.pattern"))) {
            return fact.replaceFirst(rb.getString("negative.2.pattern"), rb.getString("negative.2.replace"));
        } else {
            return fact.replaceFirst(rb.getString("negative.3.pattern"), rb.getString("negative.3.replace"));
        }
    }

    public String positiveFact() {
        return fact.replaceFirst(rb.getString("statement.1.pattern"), rb.getString("statement.1.replace"));
    }

    public String getQuestion() {
        if (!canFind) {
            return "";
        }

        if (fact.matches(rb.getString("question.1.pattern"))) {
            return fact.replaceFirst(rb.getString("question.1.pattern"), rb.getString("question.1.replace"));
        } else {
            return fact.replaceFirst(rb.getString("question.2.pattern"), rb.getString("question.2.replace"));
        }
    }
}
