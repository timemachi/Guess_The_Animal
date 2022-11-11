package animals.controller;

import animals.formatter.FactFormatter;
import animals.model.Animal;
import animals.model.KnowledgeTree;
import animals.model.Node;
import animals.service.DatabaseService;
import animals.service.DialogueManager;

import java.util.Scanner;

public class AnimalGuess {
    private static final DialogueManager dm = new DialogueManager();
    private static final DatabaseService dbs = new DatabaseService();
    private Node root;
    private KnowledgeTree kt;
    private Scanner scanner;

    public void run(String format) {
        dm.sayHello();

        String fileName = System.getProperty("user.language").equals("eo") ? "./animals_eo." : "./animals.";
        String FILE_NAME = fileName + format;
        boolean exist = dbs.checkFile(FILE_NAME);
        if (exist) {
            root = dbs.load(format, FILE_NAME);
        } else {
            root = dm.createNewRoot();
        }
        scanner = new Scanner(System.in);
        kt = new KnowledgeTree(root, dm);
        dm.welcome();

        boolean gaming = true;
        do {
            int choice = Integer.parseInt(dm.getMenu());
            kt.reset();
            switch (choice) {
                case 0 -> gaming = false;
                case 1 -> playGame();
                case 2 -> kt.printAnimals();
                case 3 -> kt.printFactAbout();
                case 4 -> kt.printStatistics();
                case 5 -> kt.printKnowledgeTree();
            }
            root = kt.getRoot();
        } while (gaming);

        dm.sayBye();
        dbs.save(root, format, FILE_NAME);

    }

    private void playGame() {
        boolean gameContinue;
        do {
            kt.reset();
            dm.thinkAndEnter();
            scanner.nextLine();
            while (kt.isStatement()) {
                kt.next(dm.query(new FactFormatter(kt.getQuestion()).getQuestion()));
            }
            if (kt.isStatement()) {
                throw new RuntimeException("Can't be statement!");
            }
            Animal guessAnimal = new Animal(kt.getData());
            boolean isCorrect = dm.query(dm.guessAnimal(guessAnimal));
            if (!isCorrect) {
                dm.giveUp();
                updateKt(guessAnimal);
            } else {
                dm.win();
            }
            gameContinue = dm.query(dm.tryAgain());
        } while (gameContinue);
    }

    private void updateKt(Animal guessAnimal) {
        Animal newAnimal = new Animal(scanner.nextLine());
        dm.promptStatement(guessAnimal.nameWithArticle(), newAnimal.nameWithArticle());

        String fact = scanner.nextLine();
        FactFormatter ff = new FactFormatter(fact);
        if (!ff.isCanFind()) {
            dm.animalFormatError();
            updateKt(guessAnimal);
            return;
        }
        boolean isFact = dm.query(dm.isStatementCorrect(newAnimal.nameWithArticle()));
        dm.learned();
        System.out.println(ff.format(newAnimal, isFact));
        System.out.println(ff.format(guessAnimal, !isFact));

        kt.addAnimal(newAnimal.animalName(), fact, isFact);

        dm.niceLearnedMuch();

    }






}
