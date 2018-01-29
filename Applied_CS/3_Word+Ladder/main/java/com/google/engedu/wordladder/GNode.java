package com.google.engedu.wordladder;

// http://www.dreamincode.net/forums/topic/377473-graph-data-structure-tutorial/
// This class is a Graph node
// implementing this node in a different class helps in using it smoothly

import java.util.ArrayList;

public class GNode {
    String word;
    ArrayList<String> neighbours;
    GNode(String str)
    {
        word = str;
        neighbours = new ArrayList<>();
    }
}
