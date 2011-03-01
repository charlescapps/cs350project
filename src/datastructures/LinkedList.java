package datastructures;

public class LinkedList<T>  {
	
	private LinkedListNode<T> head; 
	
	public LinkedList() {
		head = null; 
	}
	
	public LinkedListNode<T> getHead() {return head;}
	public void setHead(LinkedListNode<T> newHead) {head = newHead;}
	
	public void addToFront(LinkedListNode<T> newNode) {  //Simple O(1) implementation to add new node to front
		if (head==null)
			head = newNode; 
		else {
			newNode.setNext(head);
			head = newNode; 
		}
	}

}
