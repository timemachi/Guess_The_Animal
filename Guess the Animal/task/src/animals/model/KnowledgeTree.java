package animals.model;

import animals.formatter.FactFormatter;
import animals.service.DialogueManager;

import java.util.*;

public class KnowledgeTree {
    private final Node root;
    private final DialogueManager dm;
    private Node current;

    private final Map<String, List<String>> animals = new HashMap<>();
    private final Scanner scanner;

    public KnowledgeTree(Node root, DialogueManager dm) {
        this.root = root;
        this.current = root;
        this.scanner = new Scanner(System.in);
        this.dm = dm;
    }

    public Node getRoot() {
        return root;
    }

    public void reset() {
        current = root;
    }

    public String getQuestion() {
        return current.getData();
    }

    public boolean isStatement() {
        return !current.isLeaf();
    }

    public void next(boolean isYes) {
        current = isYes ? current.getYes() : current.getNo();
    }

    public String getData() {
        return current.getData();
    }

    public void printStatistics() {
        dm.treeStateTitle();

        getAnimals();
        IntSummaryStatistics statistics = animals.values().stream().mapToInt(List::size).summaryStatistics();

        dm.printRootNode(root.getData());
        dm.printStatistic(root.getNumberOfNode(), "tree.stats.nodes");
        dm.printStatistic(root.getNumberOfLeaf(), "tree.stats.animals");
        dm.printStatistic(root.getNumberOfNode() - root.getNumberOfLeaf(), "tree.stats.statements");
        dm.printStatistic(statistics.getMax(), "tree.stats.height");
        dm.printStatistic(statistics.getMin(), "tree.stats.minimum");
        dm.printStatistic(statistics.getAverage(), "tree.stats.average");

//        System.out.printf("- root node                    %s\n" +
//                "- total number of nodes        %d\n" +
//                "- total number of animals      %d\n" +
//                "- total number of statements   %d\n" +
//                "- height of the tree           %d\n" +
//                "- minimum animal's depth       %d\n" +
//                "- average animal's depth       %f\n",
//                root.getData(),
//                root.getNumberOfNode(),
//                root.getNumberOfLeaf(),
//                root.getNumberOfNode() - root.getNumberOfLeaf(),
//                statistics.getMax(),
//                statistics.getMin(),
//                double d = statistics.getAverage();
    }

    public void addAnimal(final String animal, final String statement, final boolean isRight) {
        Node newAnimal = new Node(animal);
        Node oldAnimal = new Node(current.getData());
        current.setData(statement);
        current.setYes(isRight ? newAnimal : oldAnimal);
        current.setNo(isRight ? oldAnimal : newAnimal);
    }
    public void printFactAbout() {
        Animal animal = dm.animalPrompt();

        getAnimals();
        if (!animals.containsKey(animal.animalName())) {
            dm.noFacts(animal.animalName());
        } else {
            dm.factsAbout(animal.animalName());
            for (String fact : animals.get(animal.animalName())) {
                dm.printAsList(fact);
            }
            System.out.println();
        }
    }

    public void printAnimals() {
        dm.allAnimals();
        getAnimals();
        animals.keySet().stream()
                .sorted()
                .map(name -> " - " + name)
                .forEach(System.out::println);
    }

    private void getAnimals() {
        animals.clear();
        collectAnimals(root, new LinkedList<>());
    }
    private void collectAnimals(final Node node, final Deque<String> facts) {
        if (node.isLeaf()) {
            animals.put(node.getData(), List.copyOf(facts));
            return;
        }
        String statement = node.getData();

        FactFormatter factFormatter = new FactFormatter(statement);

        facts.add(statement);
        collectAnimals(node.getYes(), facts);
        facts.removeLast();
        facts.add(factFormatter.negativeFact());
        collectAnimals(node.getNo(), facts);
        facts.removeLast();
    }

    public void printKnowledgeTree() {
        System.out.println();
        printNode(root, false, " ");
    }
    private void printNode(Node node, boolean isYes, String prefix) {
        final String symbol;
        symbol = isYes ? "├" : "└";
        if (node.isLeaf()) {
            System.out.printf("%s%s %s%n", prefix, symbol, node.getData());
            return;
        }
        System.out.printf("%s%s %s%n", prefix, symbol, node.getData());
        prefix += isYes ? "│" : " ";
        printNode(node.getYes(), true, prefix);
        printNode(node.getNo(), false, prefix);
    }
}
