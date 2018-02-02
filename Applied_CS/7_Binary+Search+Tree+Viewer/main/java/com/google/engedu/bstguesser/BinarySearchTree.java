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

package com.google.engedu.bstguesser;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.Pack200;

public class BinarySearchTree {
    private TreeNode root = null;

    public BinarySearchTree() {
    }

    public void insert(int value) {
        if (root == null) {
            root = new TreeNode(value);
            return;
        } else {
            root.insert(value);
        }
    }

    public void positionNodes(int width) {
        if (root != null)
            root.positionSelf(0, width, 0);
    }

    public void draw(Canvas c) {
        root.draw(c);
    }

    public int click(float x, float y, int target) {
        return root.click(x, y, target);
    }

    private TreeNode search(int value) {
        TreeNode current = root;

        //find the node that we are supposed to click

        while(current.getValue()!=value)
        {
            if(value > current.getValue())
            {
                current = current.right; // right child
            }
            else{
                current = current.left; // left child
            }
        }
        return current;
    }

    public void invalidateNode(int targetValue) {
        TreeNode target = search(targetValue);
        target.invalidate();
    }
}
