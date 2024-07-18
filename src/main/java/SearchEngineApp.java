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
    private static final String DATA = "--data";

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
        if(args.length > 0 && args[0].equals(DATA)) inputFile = new File(args[1]);
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

        StringBuilder results = new StringBuilder();
        linesLoop:
        for(String line : lines) {
            int numberOfMatchingWords = 0;
            for(String keyWord : invertedIndexMap.keySet()) {
                if(line.contains(keyWord) || line.matches(keyWord)) {
                    if(strategy == Strategies.ANY) {
                        results.append(line).append("\n");
                        continue linesLoop;
                    }
                    else if(strategy == Strategies.ALL) numberOfMatchingWords++;
                    else continue linesLoop;
                }
            }
            if(numberOfMatchingWords == invertedIndexMap.keySet().size()) results.append(line).append("\n");
            else if(numberOfMatchingWords == 0 && strategy.equals(Strategies.NONE)) results.append(line).append("\n");
        }
        if(!results.isEmpty()) System.out.println(results);
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
