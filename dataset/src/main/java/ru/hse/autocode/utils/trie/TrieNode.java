package ru.hse.autocode.utils.trie;

import java.util.HashMap;

/**
 * Trie node class
 */
public class TrieNode {
    private final HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Node childs
     * @return node childs
     */
    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    /**
     * Is node terminal
     * @return is node terminal
     */
    public boolean isWord() {
        return isWord;
    }

    /**
     * Make node terminal
     * @param word word stored in node
     */
    public void setEndOfWord(boolean word) {
        isWord = word;
    }


}
