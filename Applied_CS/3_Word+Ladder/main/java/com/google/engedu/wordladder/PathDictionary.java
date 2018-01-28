/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PathDictionary {

    private static final int MAX_WORD_LEN = 4;
    private static final int MIN_WORD_LEN =3;

    private static HashSet<String> words = new HashSet<>();
    public HashMap<String,GNode> word_Node = new HashMap<>(); //key = Word & Value = All neighbours with one diff letter.

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LEN || word.length()< MIN_WORD_LEN) {
                continue;
            }

            // implementing graph here
            GNode newNode = new GNode(word);
            word_Node.put(word,newNode);
            for(String str:words)
            {
                if(diff(word,str))
                {
                    newNode.neighbours.add(str);
                    word_Node.get(str).neighbours.add(word);
                }
            }
            words.add(word);
        }
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String s) {
        return word_Node.get(s).neighbours; // word_Node is the Hashmap , key is the word & value = neighbours
    }

    public boolean diff(String s1,String s2) // helper to find words with one diff alphabet
    {
        if(s1.length()!=s2.length())
            return false;
        int j = 0;
        for(int i=0;i<s1.length();i++)
        {
            if(s1.charAt(i)!=s2.charAt(i))
            {
                j++;   // we are checking how many characters are different in the two words
            }
        }
        if(j==1) //if differ by one alphabet, return true
            return true;
        else
            return false;
    }

    // breadth-first search (BFS) over a graph
    public String[] findPath(String start, String end) {

        ArrayDeque<ArrayList<String>> deque = new ArrayDeque<>(); //queue to store the paths explored so far
        HashSet<String> visited = new HashSet<>();
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(start);
        deque.addLast(pathList);

        while(!deque.isEmpty())
        {
            ArrayList<String> pathFinder = deque.pollFirst();
            if(pathFinder.size()>8) //avoiding going very deep
            {
                continue;
            }
            String tempEnd = pathFinder.get(pathFinder.size()-1);
            ArrayList<String> neighbours = word_Node.get(tempEnd).neighbours;
            for(int j=0;j<neighbours.size();j++)
            {

                String newWord = neighbours.get(j);
                ArrayList<String> tempPath = new ArrayList<>();
                for(int k=0;k<pathFinder.size();k++)
                {
                    tempPath.add(pathFinder.get(k));
                }
                if(newWord.equals(end)) //checks end of path
                {
                    tempPath.add(end);
                    return tempPath.toArray(new String[tempPath.size()]);
                }

                if(visited.contains(newWord))
                {
                    continue;
                }
                tempPath.add(newWord);
                deque.addLast(tempPath);
            }
        }
        return null;
    }
}
