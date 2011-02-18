package datastructures;

public class TestDataStructures {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		GeneralStack<Integer> gs= new GeneralStack<Integer>(); 
		
		/*for (int i = 0; i < 1001; i++) 
			gq.enQueue(new Integer(i));
		
		for (int i = 0; i < 1001; i++) 
			System.out.println(gq.deQueue());
		
		gq.deQueue(); 
		
		*/
		
		for (int i = 0; i < 1001; i++) 
			gs.push(new Integer(i));
		
		for (int i = 0; i < 1001; i++) 
			System.out.println(gs.pop()); 
			
		gs.pop();
			
		

	}

}
