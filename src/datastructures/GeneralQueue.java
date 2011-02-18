package datastructures; 

public class GeneralQueue<T extends Comparable<T>>
{
		private QueueNode<T> frontOfQueue; 
		private QueueNode<T> rearOfQueue; 
		
		private int size;
		
		public GeneralQueue() {
			frontOfQueue = rearOfQueue = null; 
			size = 0; 
		}
		
		public void enQueue(T newData) {
			if (frontOfQueue == null ) { //If list is empty, set front and rear to new QueueNode
				frontOfQueue = rearOfQueue = new QueueNode<T>(newData); 
				
			}
			else { //If list isn't empty, add new node to rear
				rearOfQueue.setRear(new QueueNode<T>(newData)); 
				rearOfQueue.getRear().setFront(rearOfQueue); 
				rearOfQueue = rearOfQueue.getRear(); 
			}
			size++;
		}
		
		public T deQueue() throws Exception {
			if (frontOfQueue==null) 
				throw new Exception("Attempted to remove node from empty queue!");
			
			T frontData = frontOfQueue.getData(); 
			
			if (frontOfQueue.getRear() != null) { //Queue had > 1 node
				frontOfQueue = frontOfQueue.getRear(); //Set new frontOfQueue to its rear
				frontOfQueue.getFront().setRear(null); //Set reference from Old Front to New Front to null
				frontOfQueue.setFront(null); //Set reference from New Front to to new Front to null	
			}
			else {
				frontOfQueue = rearOfQueue = null; //Queue only had 1 node
			}
			
			size--;
			
			return frontData; 
		}
		
		public boolean contains(T item) {
			QueueNode<T> n = frontOfQueue; 
			while (n!=null) {
				if (n.getData().compareTo(item)==0)
					return true; 
				
				n = n.getRear();
			}
			return false; 
		}
		
		public boolean isEmpty() {
			if (frontOfQueue == null || rearOfQueue == null)
				return true; 
			else
				return false;
		}
		
		public int getSize() {
			return size;
		}
}
