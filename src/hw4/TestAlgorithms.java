package hw4;

public class TestAlgorithms {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int[] weights = {5, 35, 50};
		int[] values = {60, 70, 99}; 
		KnapsackAlgorithm algo = new KnapsackAlgorithm(3, 89, weights, values);
		
		algo.executeAlgorithm();
		
		algo.printBestNab(2, 89);

	}

}
