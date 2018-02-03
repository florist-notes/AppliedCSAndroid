package com.google.engedu.ghost;

/**
 * This is the shared interface for the Ghost Dictionary.
 * This interface allows us define multiple implementations of this
 * dictionary (like one that uses an array to store words and another
 * that uses a tree) and to switch easily between these different
 * implementations in our activity.
 */
public interface GhostDictionary {
    public final static int MIN_WORD_LENGTH = 4;
    boolean isWord(String word);
    String getAnyWordStartingWith(String prefix);
    String getGoodWordStartingWith(String prefix);
}
