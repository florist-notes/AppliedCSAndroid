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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> placedTiles = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
            if(word.length() == WORD_LENGTH) {
                words.add(word);
            }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }

            placedTiles.push(tile);


                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {

        LinearLayout word1LinearLayout = (LinearLayout)findViewById(R.id.word1);
        word1LinearLayout.removeAllViews();

        LinearLayout word2LinearLayout = (LinearLayout)findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();

        stackedLayout.clear();


        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");


        int maxLength = words.size();
        int randomIndex = random.nextInt(maxLength - 1) + 1;
        word1 = words.get(randomIndex);

        randomIndex = random.nextInt(maxLength - 1) + 1;
        word2 = words.get(randomIndex);

        String scrambledWord = "";
        boolean scrambled = false;
        int i = 0, j = 0;

        while(!scrambled){
            randomIndex = random.nextInt(3 - 1) + 1;

            if(randomIndex == 1) {

                scrambledWord += word1.charAt(i);
                if (word1.length() <= i+1) {
                    for (; j < word2.length(); j++) {
                        scrambledWord += word2.charAt(j);
                    }
                    scrambled = true;
                } else {
                    i++;
                }
            } else {

                scrambledWord += word2.charAt(j);
                if (word2.length() <= j+1) {
                    for (; i < word1.length(); i++) {
                        scrambledWord += word1.charAt(i);
                    }
                    scrambled = true;
                } else {
                    j++;
                }

            }
        }
        messageBox.setText(scrambledWord);

        for(int index = scrambledWord.length() - 1; index >= 0; index--) {

            stackedLayout.push(new LetterTile(this, scrambledWord.charAt(index)));
        }

        return true;
    }

    public boolean onUndo(View view) {
        if(!placedTiles.empty()) {
            LetterTile popped = placedTiles.pop();
            (popped).moveToViewGroup((ViewGroup) stackedLayout);
            return true;
        }
        return false;
    }
}
