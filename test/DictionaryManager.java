package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DictionaryManager {
    private static DictionaryManager instance;
    private final Map<String, Dictionary> dictionaryMap;

    private DictionaryManager() {
        dictionaryMap = new LinkedHashMap<>();
    }

    public static DictionaryManager get() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public boolean query(String... args) {
        String wordToSearch = args[args.length - 1];
        String[] bookNames = Arrays.copyOf(args, args.length - 1);
        return searchDictionary(wordToSearch, "Q", bookNames);
    }

    public boolean challenge(String... args) {
        String wordToSearch = args[args.length - 1];
        String[] bookNames = Arrays.copyOf(args, args.length - 1);
        return searchDictionary(wordToSearch, "C", bookNames);
    }

    private boolean searchDictionary(String wordName, String ref, String... bookNames) {
        boolean existingFlag = false;

        for (String bookName : bookNames) {
            if (!dictionaryMap.containsKey(bookName)) {
                dictionaryMap.put(bookName, new Dictionary(bookName));
            }
            Dictionary dictionary = dictionaryMap.get(bookName);
            boolean isExist = ("Q".equals(ref)) ? dictionary.query(wordName) : dictionary.challenge(wordName);
            if (isExist) {
                existingFlag = true;
            }
        }
        return existingFlag;
    }

    public int getSize() {
        return dictionaryMap.size();
    }
}

