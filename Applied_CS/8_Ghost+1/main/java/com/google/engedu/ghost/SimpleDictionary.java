package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random r = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix == null || prefix.equals("")) {
            return words.get(r.nextInt(words.size()));
        } else {
            int low = 0, high = words.size() - 1;
            int mid;
            String t;

            while(low < high) {
                mid = (low + high) / 2;
                t = words.get(mid);
                if(t.startsWith(prefix)) {
                    return t;
                } else if(prefix.compareTo(t) > 0) {
                    // LHS is bigger
                    low = mid + 1;
                } else {
                    // RHS is bigger
                    high = mid - 1;
                }
            }

            return null;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
