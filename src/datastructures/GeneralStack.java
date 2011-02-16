package datastructures; 

public class GeneralStack<T>
{
		private QueueNode<T> topOfStack;  
		
		public GeneralStack() {
			topOfStack = null; 
			
		}
		
		void push(T newData) {
			if (topOfStack == null ) { //If list is empty, set top to new node
				topOfStack = new QueueNode<T>(newData); 
				
			}
			else { //If list isn't empty, add new node to top
				topOfStack.setFront(new QueueNode<T>(newData)); 
				topOfStack.getFront().setRear(topOfStack); 
				topOfStack = topOfStack.getFront(); 
			}
			
		}
		
		T pop() throws Exception {
			if (topOfStack==null) 
				throw new Exception("Attempted to remove node from empty stack!");
			
			T topData = topOfStack.getData(); 
			
			if (topOfStack.getRear() != null) { //Stack had > 1 node
				topOfStack = topOfStack.getRear(); //Set new topOfStack to its rear
				topOfStack.getFront().setRear(null); //Set reference from Old Front to New Front to null
				topOfStack.setFront(null); //Set reference from New Front to to old Front to null	
			}
			else {
				topOfStack = null; 
			}
			
			return topData; 
		}
		
}
