package datastructures;

import java.util.Iterator;

public class LLIterator<T> implements Iterator<T> {

    private LinkedListNode<T> currentNode;
    private boolean beforeFirst;

    public LLIterator(LinkedListNode<T> startNode) {
        currentNode = startNode;
        beforeFirst = true;
    }

    @Override
    public boolean hasNext() {
        if (currentNode == null)
            return false;

        if (beforeFirst && currentNode != null)
            return true;

        if (currentNode.getNext() != null)
            return true;

        return false;
    }

    @Override
    public T next() {
        if (beforeFirst) {
            beforeFirst = false;
            return currentNode.getData();
        } else {
            currentNode = currentNode.getNext();
            return currentNode.getData();
        }
    }

    @Override
    public void remove() {
        // not implemented!! ?? Lulz

    }

}
