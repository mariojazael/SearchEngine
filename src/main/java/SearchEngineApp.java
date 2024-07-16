import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SearchEngineApp {
    private final Scanner Scanner = new Scanner(System.in);
    private String[] lines = null;
    private boolean flag = true;
    private File inputFile = null;
    private final HashMap<String, String> invertedIndexMap = new HashMap<>();
    private Strategies strategy = Strategies.ALL;

    public void start(String[] args) throws IOException {
        readUserInput(args);
        fillIndexMap();
        while(flag) {
            showMenu();
            flag = switchOption(Scanner.nextLine());
        }
    }

    private void fillIndexMap() {
        int i = 0;
        for(String line : lines) {
            for(String word : line.split(" ")) {
                if(!invertedIndexMap.containsKey(word)) invertedIndexMap.put(word, i + " ");
                else invertedIndexMap.put(word, invertedIndexMap.get(word) + i + " ");
            }
            i++;
        }
    }

    private void readUserInput(String[] args) throws IOException {
        if(args.length > 0 && args[0].equals("--data")) inputFile = new File(args[1]);
        else lines = new String[Integer.parseInt(Scanner.nextLine())];

        if(lines != null) {
            for(int i = 0; i < lines.length; i++) {
                lines[i] = Scanner.nextLine();
            }
        } else {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            lines = result.toString().split("\n");
        }
    }

    private String getLines(String searchWord) {
        String indexes = invertedIndexMap.get(searchWord);
        List<String> linesList = new ArrayList<>(Arrays.asList(lines));
        StringBuilder result = new StringBuilder();
        for(String index : indexes.split(" ")) {
            result.append(linesList.get(Integer.parseInt(index))).append("\n");
        }
        return result.toString();
    }

    private void askStrategy() {
        strategy = Strategies.valueOf(Scanner.nextLine().toUpperCase());
    }

    private void search() {
        askStrategy();
        String searchWord = Scanner.nextLine();
        String[] searchWords = null;
        if(searchWord.contains(" ")) searchWords = searchWord.split(" ");
        else if(invertedIndexMap.containsKey(searchWord)) {
            String result = getLines(searchWord);
            System.out.println(result);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            int numberOfMatchingWords = 0;
            wordsLoop:
            for (String inputWord : line.split(" ")) {
                // In case searchWords is null, it means there is only one search word, so
                // searchWord will be compared against the inputWord only.
                if(searchWords != null) {
                    for(String word : searchWords) {
                        if(inputWord.equalsIgnoreCase(word)){
                            if(strategy.equals(Strategies.ANY)) {
                                stringBuilder.append(line).append("\n");
                                break wordsLoop;
                            } else if(strategy.equals(Strategies.ALL) || strategy.equals(Strategies.NONE)) {
                                numberOfMatchingWords++;
                                break;
                            }
                        }

                    }
                } else if (strategy != Strategies.NONE && inputWord.matches(searchWord)) {
                    stringBuilder.append(line).append("\n");
                    break;
                }
            }
            if(numberOfMatchingWords == 0 && strategy.equals(Strategies.NONE)) stringBuilder.append(line).append("\n");
            else if(searchWords != null && numberOfMatchingWords == searchWords.length && strategy == Strategies.ALL) stringBuilder.append(line).append("\n");
        }
        if(!stringBuilder.isEmpty()) System.out.println(stringBuilder);
        else System.out.println("Not matching people found.");
    }

    private void showMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    private boolean switchOption(String option) {
        switch(option) {
            case "1" : {
                search();
                break;
            }
            case "2" : {
                printAllPeople();
                break;
            }
            case "0" : return false;
            default: {
                System.out.println("Not a valid option.");
                break;
            }
        }
        return true;
    }

    private void printAllPeople() {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
