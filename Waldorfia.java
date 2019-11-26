/****************************************************************
 * Name: Manel Ghorbal 											*
 *																*
 * ID: 002235600 												*
 * 																*
 * CS321 - Assignment 1											*
 * 																*
 * Waldorfia Simualtion											*
 ***************************************************************/

import java.util.Scanner;

public class Waldorfia { 
	//Maximum number of rounds that are allowed in the simulation
	final static int MAXROUND=5;
	
	//size of neighborhood
	private static int n;
	
	//number of omnivores/vegans
	private static int omni;
	private static int veg;
	
	//how dense the neighborhood will be
	private static double density;
	
	//number of occupants occupying the neighborhood
	private static int occupants;
	
	//threshold for neighbors
	private static double t;
	private static Scanner in;
	
	//getter and setter for omni 
	public static int getOmni() {
		return omni;
	}

	public static void setOmni(int omni) {
		Waldorfia.omni = omni;
	}

	//getter and setter for veg 
	public static int getVeg() {
		return veg;
	}

	public static void setVeg(int veg) {
		Waldorfia.veg = veg;
	}

	//getter and setter for occupants
	public static int getOccupants() {
		return occupants;
	}

	public static void setOccupants(int occupants) {
		Waldorfia.occupants = occupants;
	}
    
	//initializes the neighborhood
    static char [][] initialize(char [][] grid, double perVeg, double perOmni){
    	//count total citizens inputted so far - used for comparing & if neighborhood is full later 
    	int totVeg = 0;
    	int totOmni = 0;
    	
    	//randomly initializes neighborhood (based on inputted percent of vegans & omnis wanted in town)
    	for (int i=0; i<n; i++) {
    		for (int j=0; j<n; j++) {
    			//generate random number
    			int rand = (int) ((Math.random() * 100));
    			//if number generated less than percent vegans in the neighborhood, it will place a vegan
    			if (rand <= (perVeg * density * 100)) {
	    			if (totVeg < veg) {
		    			grid [i][j] = 'X';
		    			totVeg++;
	    			}
    			}
    			//if number generated more than %vegans & more than %omnis in the neighborhood, it will place a omnivore
    			if (rand > (perVeg * density * 100) && rand <= (density * 100)) {
    				if (totOmni < omni) {
    					grid [i][j] = 'O';
    					totOmni++;
    				}
    			}
    			//the rest of the time, no citizen is placed
    			else if (rand >= (density * 100)) {
    				grid [i][j] = ' ';
    			}
    		}
    	}

    	//random isn't always accurate -- if we need need to place more citizens to fill neighborhood
	    if (totVeg < veg || totOmni < omni){
	    	//a while loop will ensure the desired number - sometimes loop does not stop
	    	//while (totVeg < veg && totOmni < omni)
    		for (int i = 0; i < n; i++) {
        		for (int j = 0; j < n; j++) {
        			//start placing residents in the first available space, if needed
        			if (grid [i][j] != 'X' && grid [i][j] != 'O') {
        				//if more vegans needed, place a vegan there & continue
    	    			if (totVeg < veg) {
    		    			grid [i][j] = 'X';
    		    			totVeg++;
    		    			continue;
    	    			}
    	    			//if more omnivores are needed, place an omnivore
    	    			if (totOmni < omni) {
    		    			grid [i][j] = 'O';
    		    			totOmni++;
    	    			}
        			}
        		}
    		}
    		//}
	    }
    	
	    //fills in the remaining empty spaces with a ' ', (used to print)
        for (int i=0; i<grid.length; i++) {
    		for (int j=0; j<grid[i].length; j++) {
    			if (grid[i][j] != 'X' && grid[i][j] != 'O') {
    				grid[i][j] = ' ';
    			}
    		}
        }
    
        //prints the initial poplation of the neighborhood
    	System.out.println("\nInitial Population: ");
    	for (int i=0; i<grid.length; i++) {
    		for (int j=0; j<grid[i].length; j++) {
    				System.out.printf( "%3s", grid[i][j] );
    		}
    		//go to the next line
    		System.out.print("\n");
    	}    	
    	
    	return grid;
    	
    }
  
    // Function to create & print the next round 
    static char[][] nextRound(char grid[][]) { 
    	//initialize a 2D array for the next round
        char[][] futureRound = new char[n][n]; 

        //traversing the parametered grid
        for (int i=0; i<n; i++){                                                                           
            for (int j=0; j<n; j++){
            	//if the grid contains a citizen
            	if (grid[i][j] == 'X' || grid[i][j] == 'O') {
            		//count neighbors - if % alike greater than threshold: keep resident in place 
	            	if ((100 * (countNeighbors(grid, i, j, grid [i][j]))) >= (100 * t)) {
	            		futureRound[i][j] = grid[i][j];
	            	}
	            	//count neighbors - if % alike less than threshold: 
	            	if ((100 * t) > 100 * (countNeighbors(grid, i, j, grid [i][j]))){
	            		boolean moved = false;
	            		//traverse the current grid
	            		for (int k=0; k<n; k++) {
	            			for (int l=0; l<n; l++) { 
            					//if location is is empty, count alike neighbors (to the current resident)
	            				if (grid[k][l] != 'X' && grid [k][l] != 'O'){
	            					//if location's alike neighbors % is greater than threshold
	            					if (100*countNeighbors(grid, k, l, grid[i][j]) >= (100 * t)) {
	            						if (futureRound[k][l] != 'X' && futureRound[k][l] != 'O' && moved == false) {
	            							//place citizen there in next round
		            						futureRound[k][l] = grid[i][j];
		            						//remove resident from current grid - allows citizens later to be moved here
		            						grid[i][j] = ' ';
		            						moved = true;
	            						}
	            					}
	            				}
	            			}
	            		}
	            		//if there are no "satisfying" places anywhere
	            		if (moved == false) {
		            		for (int k = 0; k < n; k++) {
		            			for (int l = 0; l < n; l++) {
		            				//place the unhappy resident in the first empty location 
		            				if (grid [k][l] != 'X' && grid [k][l] != 'O') {
		            					if (futureRound[k][l] != 'X' && futureRound[k][l] != 'O' && moved == false){
		            						futureRound[k][l] = grid [i][j];
		            						moved = true;
		                				}                					
		                			}
		                		}
		            		}
	            		}
	            	}
            	}
        	}
        }
        
        //fill unoccupied spaces with ' '
        for (int i=0; i<futureRound.length; i++) {
    		for (int j=0; j<futureRound[i].length; j++) {
    			if (futureRound[i][j] != 'X' && futureRound[i][j] != 'O') {
    				futureRound[i][j] = ' ';
    			}
    		}
        }
        
        //print next round
        for (int i=0; i<futureRound.length; i++) {
    		for (int j=0; j<futureRound[i].length; j++) {
    			System.out.printf( "%3s", futureRound[i][j] );
    		}	
    		//go to the next line
    		System.out.print("\n");
    	}
        return futureRound;
   	}
    
    //counts neighbors & returns double of percent of alike neighbors
    public static double countNeighbors (char[][] grid, int i, int j, char citizen) {
    	//initilize likable & total neighbors variables
    	double alikeNeighbors = 0;
    	double totNeighbors = 0;
    	
    	if (citizen == 'X' || citizen == 'O'){
    		//if not in the first row
    		if (i != 0) {
    			//check above neighbor is the same
            	if (citizen == grid [i-1][j]) {
            		alikeNeighbors ++;
            		totNeighbors++;
            	}
            	//check if unlikable above neighbor exists
            	if (citizen != grid [i-1][j] && grid [i-1][j] != ' ') {
            		totNeighbors++;
            	}
    		}
    		//if not in the last row
    		if (i != n-1) {
	    		//check if below neighbor is the same
	        	if (citizen == grid [i+1][j]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable below neighbor exists
	        	if (citizen != grid [i+1][j] && grid[i+1][j] != ' ' ) {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the first column
    		if (j != 0) {
	        	//check if left neighbor is the same
	        	if (grid[i][j] == grid[i][j-1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable left neighbor exists
	        	if (citizen != grid[i][j-1] && grid[i][j-1] != ' ') {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the last column
    		if (j != n-1) {
	        	//check if right neighbor is the same
	        	if (citizen == grid [i][j+1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable right neighbor exists
	        	if (citizen != grid [i][j+1] && grid [i][j+1] != ' ' ) {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the upper left cell
    		if (i != 0 && j != 0) {
	        	//check if upper left neighbor is the same
	        	if (grid[i][j] == grid[i-1][j-1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable upper left neighbor exists
	        	if (citizen != grid[i-1][j-1] && grid[i-1][j-1] != ' ') {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the upper right cell
    		if (i != 0 && j != n-1) {
	        	//check if upper right neighbor is the same
	        	if (citizen == grid [i-1][j+1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable upper right neighbor exists
	        	if (citizen != grid [i-1][j+1] && grid [i-1][j+1] != ' ' ) {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the lower right cell
    		if (i != n-1 && j != 0) {
	        	//check if lower left neighbor is the same
	        	if (citizen == grid [i+1][j-1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable lower left neighbor exists
	        	if (citizen != grid [i+1][j-1] && grid [i+1][j-1] != ' ' ) {
	        		totNeighbors++;
	        	}
    		}
    		//if not in the lower left cell
    		if (i != n-1 && j != n-1) {
	        	//check if lower right neighbor is the same
	        	if (citizen == grid [i+1][j+1]) {
	        		alikeNeighbors ++;
	        		totNeighbors++;
	        	}
	        	//check if unlikable lower right neighbor exists
	        	if (citizen != grid [i+1][j+1] && grid [i+1][j+1] != ' ' ) {
	        		totNeighbors++;
	        	}
    		}
    		
    	}
    	
    	//return the percent of alike neighbors the citizen has
    	return (alikeNeighbors/totNeighbors);
    }
    
    //returns boolean - checks if all citizens are satisfied 
    public static boolean allSatisfied(char [][] grid) {
    	//loop through neighborhood
    	for (int i=0; i<n; i++){                                                                           
            for (int j=0; j<n; j++){
            	if (grid[i][j] == 'X' || grid[i][j] == 'O') {
            		//counts neighbors
	            	if ((100 * t) > (100 * (countNeighbors(grid, i, j, grid [i][j])))) {
	            		//if one citizen is unsatisfied, return false
	            		return false;
	            	}
            	}
            }
    	}
    	//if entire grid is satisfied, return true
    	return true;
    }
    
   //main function
    public static void main(String[] args) { 
    	in = new Scanner(System.in);
    	
    	System.out.println("----------- Welcome to the neighborhood of Waldorfia! -----------\n\n");
    	
    	//getting size of neighborhood
    	System.out.println("\nEnter the dimension n of the neighborhood(nxn) ex. 5: ");
    	n = in.nextInt();

    	//getting density of total citizens 
    	System.out.println("\nEnter the density of total citizens in the neighborhood. ex. 0.80: ");
    	density = in.nextDouble();

    	//getting percentage of vegans
    	System.out.println("\nEnter the percentage of vegans in neighborhood. ex. 0.50: ");
    	double perVeg = in.nextDouble();

       	setOccupants((int) (n * n * density));
    	//number of Vegs/omnis in the neighborhood
    	setVeg((int) (occupants * perVeg )); 
    	setOmni((int) (occupants * (1 - perVeg)));
    	
    	//setting percentage of omnivores
    	System.out.println("There will be " + (veg + omni) + " residents living in Waldorfia.");
    	System.out.println(((int)(perVeg * 100)) + "% of residents will be Vegan. " + ((int)((1-perVeg) * 100)) + "% of residents will be Omnivores. ");
    	System.out.println("There will be " + veg + " vegans and " + omni + " omnivores in Waldorfia.");

    	//getting threshold
    	System.out.println("\nEnter the threshold. ex. 0.50: ");
    	t = in.nextDouble();

    	//create a 2D array for neighborhood
    	char [][] grid = new char [n][n];
    	//initialize neighborhood with warrented citizens
    	grid = initialize ( grid, perVeg, (1 - perVeg));
  
    	//loop: checks if all citizens are satisfied
    	int round = 1;
    	//if all citizens are not satisfied, move citizens around until they are
    	while (allSatisfied(grid) == false && round <=MAXROUND) {
    		System.out.println ("\nRound " + round + ": ");
    		//if not all citizens are satisfied, then go another round and move citizens around
    		grid = nextRound(grid); 
    		round++;
    	}
   
    }
    
}

 