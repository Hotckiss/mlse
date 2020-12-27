package ru.hse.autocode.utils.trie;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class that represents trie
 */
public class Trie {
    private TrieNode root;

    public Trie(List<String> words) {
        root = new TrieNode();
        for (String word: words) {
            insert(word);
        }
    }

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts word in true
     * @param word
     */
    public void insert(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            current = current.getChildren()
                    .computeIfAbsent(word.charAt(i), c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    /**
     * Search for word in trie
     * @param word word to search
     * @return true if word in trie false otherwise
     */
    public boolean find(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isWord();
    }

    /**
     * Calculate trie-based features
     * @param codeFragment source code
     * @return code features based on trie
     */
    public HashMap<String, Integer> calculate(String codeFragment) {
        HashMap<String, Integer> result = new HashMap<>();
        StringTokenizer st = new StringTokenizer(codeFragment, " }(){\t\n\r");

        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            if (find(word)) {
                Integer cnt = result.get(word);
                result.put(word, (cnt == null) ? 1 : cnt + 1);
            }
        }

        return result;
    }
}
