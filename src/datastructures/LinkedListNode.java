package datastructures;

public class LinkedListNode<T> {

	private T data; 
	private LinkedListNode<T> next;
	
	public LinkedListNode(T newData) {
		data = newData;
		next = null; 
	}
	
	public T getData() {
		return data; 
	}
	
	public void setNext(LinkedListNode<T> node) {
		next = node; 
	}
	
	public LinkedListNode<T> getNext() {return next; }
	
}
