package utils;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<Item> implements Iterable<Item> {
    private int numberOfElements;
    private Node head;
    private Node tail;

    // no args constructor
    public DoublyLinkedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.previous = head;
    }

    // the node class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    public boolean isEmpty() {
        return numberOfElements == 0;
    }

    public int size() {
        return numberOfElements;
    }

    public void add(Item item) {
        Node last = tail.previous;
        Node nodeX = new Node();
        nodeX.item = item;
        nodeX.next = tail;
        nodeX.previous = last;
        tail.previous = nodeX;
        last.next = nodeX;
        numberOfElements++;
    }

    public ListIterator<Item> iterator() {
        return new DoublyLinkedListIterator();
    }

    // The Doubly linked list iterator
    private class DoublyLinkedListIterator implements ListIterator<Item> {
        private Node current = head.next;
        private Node lastAccessed = null;
        private int index = 0;

        public boolean hasNext() {
            return index < numberOfElements;
        }

        public boolean hasPrevious() {
            return index > 0;
        }

        public int previousIndex() {
            return index - 1;
        }

        public int nextIndex() {
            return index;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastAccessed = current;
            Item item = current.item;
            current = current.next;
            index++;
            return item;
        }

        public Item previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            current = current.previous;
            index--;
            lastAccessed = current;
            return current.item;
        }

        public void set(Item item) {
            if (lastAccessed == null) throw new IllegalStateException();
            lastAccessed.item = item;
        }

        public void remove() {
            if (lastAccessed == null) throw new IllegalStateException();
            Node nodeX = lastAccessed.previous;
            Node nodeY = lastAccessed.next;
            nodeX.next = nodeY;
            nodeY.previous = nodeX;
            numberOfElements--;
            if (current == lastAccessed)
                current = nodeY;
            else
                index--;
            lastAccessed = null;
        }

        public void add(Item item) {
            Node nodeX = current.previous;
            Node nodeY = new Node();
            Node nodeZ = current;
            nodeY.item = item;
            nodeX.next = nodeY;
            nodeY.next = nodeZ;
            nodeZ.previous = nodeY;
            nodeY.previous = nodeX;
            numberOfElements++;
            index++;
            lastAccessed = null;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Item item : this)
            stringBuilder.append(item + " ");
        return stringBuilder.toString();
    }
}