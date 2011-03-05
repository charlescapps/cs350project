package datastructures;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {

    private LinkedListNode<T> head;

    public LinkedList() {
        head = null;
    }

    public LinkedListNode<T> getHead() {
        return head;
    }

    public void setHead(LinkedListNode<T> newHead) {
        head = newHead;
    }

    public void addToFront(LinkedListNode<T> newNode) { //Simple O(1) implementation to add new node to front
        if (head == null)
            head = newNode;
        else {
            newNode.setNext(head);
            head = newNode;
        }
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return new LLIterator<T>(head);
    }

}
