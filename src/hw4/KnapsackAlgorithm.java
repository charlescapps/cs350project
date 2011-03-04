package hw4;

public class KnapsackAlgorithm {
	
	private final int numItems; 
	private final int maxWeight; 
	private final int itemWeights[];
	private final int itemValues[];
	
	private int robberies[][];
	private boolean ranAlgorithm;
	
	public KnapsackAlgorithm(int numItems, int maxWeight, int itemWeights[], int itemValues[]) throws Exception {
		if (itemWeights.length != itemValues.length || itemWeights.length != numItems)
			throw new Exception("Array of Item Weights isn't the same size as the array of item values!!");
		
		this.numItems = numItems; 
		this.maxWeight = maxWeight; 
		this.itemWeights = itemWeights;
		this.itemValues = itemValues;
		
		this.robberies = new int[maxWeight+1][itemWeights.length];
		
		this.ranAlgorithm = false; 
		
		
	}
	
	public void executeAlgorithm() {
	
		for (int i = 0; i < numItems; i++) {
			fillRobberiesColumn(i);
		}
		this.ranAlgorithm = true;
	}
	
	private void fillRobberiesColumn(int col) { 
		int nab_1; 
		int nab_2;
		int colWeight;
		
		for (int row = 0; row <= maxWeight; row++) {
			if (col == 0 ) { //If we are at the first column, just check if we can pick up the first item
				robberies[row][col] = (row >= itemWeights[col] ? itemValues[col] : 0);
				continue; 
			}
			
			//Otherwise, see if it's best to consider the current item + previous items, or just previous items
			colWeight = itemWeights[col];
			//Optimal Strategy if we nab this item + take an item from the previous column
			nab_1 = (row >= colWeight ? (itemValues[col] + robberies[row - colWeight][col - 1])
						: 0);
			
			nab_2 = robberies[row][col - 1]; //Optimal strategy only considering previous items
			
			robberies[row][col] = Math.max(nab_1, nab_2); //Fill in table with the max of the two values
		}
	}
	
	public void printBestNab(int maxItemConsidered, int maxWeightConsidered) {
		System.out.println("Num items considered: " + (maxItemConsidered + 1));
		System.out.println("Max weight: " + maxWeightConsidered + "\n"); 
		System.out.println("The best value you can grab is: $" + robberies[maxWeightConsidered][maxItemConsidered] + " dollars!\n");
		System.out.println("The best choice of item nabbing is (items indexed starting at 0):\n");
		
		recursivePrintItemsNabbed(maxWeightConsidered, maxItemConsidered);
		
	}
	
	private void recursivePrintItemsNabbed(int row, int col) { 
		if (col == 0 && robberies[row][col] > 0) { //Base case: we hit column 0, so we nabbed it if it's non-zero
			System.out.println("Nab item #" + col);
			return;
		}
		else if (col == 0)
			return; 
		
		if (robberies[row][col] == robberies[row][col - 1]) { //If the previous column = the current column, 
			recursivePrintItemsNabbed(row, col - 1);		  //We actually nabbed the previous one
		}
		else if (robberies[row][col] > robberies[row][col - 1]){ //If the previous column is less, we nabbed this column
			recursivePrintItemsNabbed(row - itemWeights[col], col - 1);
			System.out.println("Nab item #" + col); 
		}
	}
	
	public int[][] getRobberies() {return robberies;}
	

}
