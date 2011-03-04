package hw4;

public class FindMinParenthesization {

	private static final int INFINITY  = Integer.MAX_VALUE;
	
	public static void main(String[] args) {
		
		solveProblem15dot2dash1();
	}
	
	private static void solveProblem15dot2dash1() {
		//Values for problem 15.2-1
		int[] rows_cols = {5,10,3,12,5,50,6};
		int[][] best_cost = new int[6][6];
		int[][] best_split = new int[6][6];
		
		minMult(0, 5, rows_cols, best_cost, best_split);
		
		printBestSplits(0, 5, best_split);
		
		System.out.println("\nBest Cost: " + best_cost[0][5]);
		
	}
	
	private static void printBestSplits(int i, int j, int[][] split) { //Function to print out the results in human-readable form!
		if (Math.abs(i-j) <= 1)  //No point in outputting best way to multiply 2 matrices! 
			return; 
		
		int best = split[i][j]; 
		System.out.println("Best split from " + i + " to " + j + " = " + best);
		
		printBestSplits(i, best, split);
		printBestSplits(best + 1, j, split);
		
	}
	
	public static int minMult(int i, int j, int rows_cols[], int best[][], int split[][]) { 
		int cost = INFINITY;
		if (i==j) 
			return 0; 
	
		best[i][j] = INFINITY;  //Constant equal to Integer.MAX_VALUE = 2^31 - 1 
		
		for (int k=i; k<j; k++) { 
			cost = minMult(i,k, rows_cols, best, split) + minMult(k+1,j, rows_cols, best, split) + rows_cols[i]*rows_cols[k+1]*rows_cols[j+1]; 
			if (cost < best[i][j]) { 
				best[i][j]  = cost; 
				split[i][j] = k; 
			}
		}
		return best[i][j]; 
	}
}
