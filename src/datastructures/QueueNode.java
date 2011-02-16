package datastructures; 

public class QueueNode<T>
{
	private T data; 
	private QueueNode<T> inFront; 
	private QueueNode<T> inRear; 

	public QueueNode(T _data) {
		data = _data; 
		inFront = inRear = null; 
	}
	
	public QueueNode<T> getFront() {
		return inFront; 
	}
	
	public QueueNode<T> getRear() {	
		return inRear; 
	}
	
	public void setFront(QueueNode<T> n) {
		inFront = n; 	
	}
	
	public void setRear(QueueNode<T> p) {
		inRear = p; 	
	}
	
	public T getData() {
		return data; 	
	}
	
	
}