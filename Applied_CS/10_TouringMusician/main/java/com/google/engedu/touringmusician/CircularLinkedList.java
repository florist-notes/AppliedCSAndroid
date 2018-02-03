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

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point point,Node prev,Node next)
        {
            this.point=point;
            this.prev=prev;
            this.next=next;
        }
    }

    Node head;

    public void insertBeginning(Point p) {

        if(head==null)
        {
            Node node=new Node(p,null,null);
            head=node;
            node.prev=node;
            node.next=node;
        }
        else
        {
            Node previousNode=head.prev;
            Node temp=head;
            Node node=new Node(p,previousNode,head);
            previousNode.next=node;
            temp.prev=node;

        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;

        if(head!=null)
        {
            Node temp=head;
            while(temp.next!=head)
            {
                total+=distanceBetween(temp.point,temp.next.point);
                temp=temp.next;
            }
        }

        return total;
    }

    public void insertNearest(Point p) {

        Node temp=head;

        //Case 1 : No Node at All
        if(temp==null)
        {
            insertBeginning(p);
        }

        //Case 2 : Only One Node
        else if(temp.next==head)
        {
            insertBeginning(p);
        }
        //Case 3 : More than 2 nodes
        else
        {
            Node minNode=head;
            while(temp.next!=head)
            {
                float min=Float.MAX_VALUE;
                float distance=distanceBetween(temp.point,p);
                if(min<distance)
                {
                    minNode=temp;
                }
                temp=temp.next;
            }
            Node node=new Node(p,minNode,minNode.next);
            minNode.next=node;
            minNode=minNode.next;
            minNode.prev=node;

        }
    }

    public void insertSmallest(Point p) {

        Node temp=head;

        // Case 1 : No Node at All
        if(temp==null)
        {
            insertBeginning(p);
        }
        // Case 2 : Only One Node
        else if(temp.next==head)
        {
            insertBeginning(p);
        }
        // Case 3 : More than 2 nodes
        else
        {
            Node prevNode=temp;
            temp=temp.next;

            float prevDist=distanceBetween(prevNode.point,p);
            float nextDist=distanceBetween(p,temp.point);


            while(temp!=head)
            {
                float presentTotalDistance=distanceBetween(temp.point,p)+distanceBetween(p,temp.next.point);
                if(presentTotalDistance<(prevDist+nextDist))
                {
                    prevNode=temp;
                }
                temp=temp.next;
            }

            Node node=new Node(p,prevNode,prevNode.next);
            prevNode.next=node;
            prevNode=prevNode.next;
            prevNode.prev=node;
        }


    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
