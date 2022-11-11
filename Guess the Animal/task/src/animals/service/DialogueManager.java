package animals.service;


import animals.model.Animal;
import animals.model.Node;


import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DialogueManager {
    private final Scanner scanner;
    private final ResourceBundle rbPatterns;
    private final ResourceBundle rbMessages;
    private final Pattern patYes;
    private final Pattern patNo;

    private final String[] askAgain;

    private final Random random = new Random();



    public DialogueManager() {
        scanner = new Scanner(System.in);
        rbPatterns = System.getProperty("user.language").equals("eo") ?
                ResourceBundle.getBundle("patterns", new Locale("eo")) :
                ResourceBundle.getBundle("patterns", Locale.getDefault());
        rbMessages = System.getProperty("user.language").equals("eo") ?
                ResourceBundle.getBundle("messages", new Locale("eo")) :
                ResourceBundle.getBundle("messages", Locale.getDefault());
        patYes = Pattern.compile(rbPatterns.getString("positiveAnswer.isCorrect"), Pattern.CASE_INSENSITIVE);
        patNo = Pattern.compile(rbPatterns.getString("negativeAnswer.isCorrect"), Pattern.CASE_INSENSITIVE);
        askAgain = rbMessages.getString("ask.again").split("\\f");
    }

    public void sayHello() {
        LocalTime curr = LocalTime.now();
        String greetWord;
        LocalTime Morning = LocalTime.parse("05:00");
        LocalTime Afternoon = LocalTime.parse("12:00");
        LocalTime evening = LocalTime.parse("18:00");
        LocalTime night = LocalTime.parse("03:00");

        if (curr.isAfter(Morning) && curr.isBefore(Afternoon)) {
            greetWord = rbMessages.getString("greeting.morning");
        } else if (curr.isAfter(Afternoon) && curr.isBefore(evening)) {
            greetWord = rbMessages.getString("greeting.afternoon");
        } else if (curr.isAfter(evening) && curr.isBefore(LocalTime.MIDNIGHT)) {
            greetWord = rbMessages.getString("greeting.evening");
        } else if (curr.isAfter(LocalTime.MIDNIGHT) && curr.isBefore(night)) {
            greetWord = rbMessages.getString("greeting.night");
        } else {
            greetWord = rbMessages.getString("greeting.early");
        }
        System.out.println(greetWord);
        System.out.println();
    }

    public void sayBye() {
        String[] farewell = rbMessages.getString("farewell").split("\\f");
        System.out.println(farewell[random.nextInt(farewell.length)]);
    }

    public void welcome() {
        System.out.println(rbMessages.getString("welcome"));
    }

    public void thinkAndEnter() {
        System.out.println(rbMessages.getString("game.think"));
        System.out.println(rbMessages.getString("game.enter"));
    }

    public void giveUp() {
        System.out.println(rbMessages.getString("game.giveUp"));
    }

    public void win() {
        String[] win = rbMessages.getString("game.win").split("\\f");
        System.out.println(win[random.nextInt(win.length)]);
    }

    public String tryAgain() {
        String[] tryAgain = rbMessages.getString("game.again").split("\\f");
        return tryAgain[random.nextInt(tryAgain.length)];
    }

    public String isStatementCorrect(String animal) {
        MessageFormat mf = new MessageFormat(rbMessages.getString("game.isCorrect"));
        return mf.format(new Object[]{animal});
    }

    public void niceLearnedMuch() {
        String[] nice = rbMessages.getString("animal.nice").split("\\f");
        System.out.print(nice[random.nextInt(nice.length)] + rbMessages.getString("animal.learnedMuch"));
    }

    public void learned() {
        System.out.println(rbMessages.getString("game.learned"));
    }

    public void animalFormatError() {
        System.out.println(rbMessages.getString("animal.error"));
    }

    public Animal animalPrompt() {
        System.out.println(rbMessages.getString("animal.prompt"));
        String animal = scanner.nextLine();
        Animal animal1 = new Animal(animal);
        if (!animal1.isValid()) {
            System.out.println(rbMessages.getString("animal.error"));
            return animalPrompt();
        }
        return animal1;
    }

    public void noFacts(String animal) {
        MessageFormat mf = new MessageFormat(rbMessages.getString("tree.search.noFacts"));
        System.out.println(mf.format(new Object[]{animal}));
    }

    public void factsAbout(String animal) {
        MessageFormat mf = new MessageFormat(rbMessages.getString("tree.search.facts"));
        System.out.println(mf.format(new Object[]{animal}));
    }

    public void printAsList(String info) {
        String format = rbMessages.getString("tree.search.printf");
        System.out.printf(format, info);
    }
    public void promptStatement(String guessAnimal, String newAnimal) {
        MessageFormat mf = new MessageFormat(rbMessages.getString("statement.prompt"));
        System.out.println(mf.format(new Object[]{guessAnimal, newAnimal}));
    }

    public void treeStateTitle() {
        System.out.println(rbMessages.getString("tree.stats.title"));
    }

    public void printRootNode(String root) {
        MessageFormat mf = new MessageFormat(rbMessages.getString("tree.stats.root"));
        System.out.println(mf.format(new Object[]{root}));
    }

    public void printStatistic(int n, String keyName) {
        MessageFormat mf = new MessageFormat(rbMessages.getString(keyName));
        System.out.println(mf.format(new Integer[]{n}));
    }

    public String guessAnimal(Animal animal) {
        String guess = animal.nameWithArticle();
        return guess.replaceFirst(rbPatterns.getString("guessAnimal.1.pattern"), rbPatterns.getString("guessAnimal.1.replace"));
    }

    public void printStatistic(double n, String keyName) {
        MessageFormat mf = new MessageFormat(rbMessages.getString(keyName));
        System.out.println(mf.format(new Double[]{n}));
    }

    public void allAnimals() {
        System.out.println(rbMessages.getString("tree.list.animals"));
    }

    public boolean query(String prompt) {
        System.out.println(prompt);
        return query();
    }

    private boolean query() {
        String response = scanner.nextLine().trim();
        if (patYes.matcher(response).matches()) {
            return true;
        }
        if (patNo.matcher(response).matches()) {
            return false;
        }
        System.out.println(askAgain[random.nextInt(askAgain.length)]);
        return query();
    }

    public Node createNewRoot() {
        System.out.println(rbMessages.getString("animal.wantLearn"));
        System.out.println(rbMessages.getString("animal.askFavorite"));
        Animal animal =new Animal(scanner.nextLine());
        return new Node(animal.animalName());
    }


    public String getMenu() {
        System.out.printf("""
                        %s

                        1. %s
                        2. %s
                        3. %s
                        4. %s
                        5. %s
                        0. %s
                        """,
                rbMessages.getString("menu.property.title"),
                rbMessages.getString("menu.entry.play"),
                rbMessages.getString("menu.entry.list"),
                rbMessages.getString("menu.entry.search"),
                rbMessages.getString("menu.entry.statistics"),
                rbMessages.getString("menu.entry.print"),
                rbMessages.getString("menu.property.exit"));

        String ans = scanner.nextLine();
        if (ans.matches("[0-5]")) {
            return ans;
        } else {
            MessageFormat mf = new MessageFormat(rbMessages.getString("menu.property.error"));
            System.out.println(mf.format(new Object[]{5}));
            return getMenu();
        }
    }


}
