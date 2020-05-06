package team16.hw6;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * This class creates an object called Simulate.
 * Simulates the pandemic by using the data that the user gives, uses class Grid to control the environment
 * 
 * @author Zoe Passiadou
 * @author Sohaib Nassar 
 *
 */
public class Simulate {
	
	private static int maskUsePers;               //  represents the percentage of mask users %100
	private static int immunePers;                //  represents the percentage of immune people %100
	private static double humanInfP;              //  represents the possibility of a human infecting another 0-1
	private static double humanSpaceP;            //  represents the possibility of a human to infect a space 0-1
	private static double spaceHumanP;            //  represents the possibility of a space infecting a human 0-1
	private static double movingP;                //  represents the possibility of moving 0-1
	private static int timeForSpaceToBeSafe;      //  represents the time needed for a space to be free of infection
	private static int time;                      //  time of the simulation
	private static int timeForSquareToGetInfected;//  time needed for a space to get infected
	private static int maskProtection;            //  how much the mask protects %100 (100 being full protection)
	private int amountOfAreas;
	private int cnt=0;                            //  counter for counting how many people got infected
	private static Random randomizer = new Random();
	private static Scanner in = new Scanner(System.in);
	
	
	//constructor

	public Simulate(int mask, int immune, double humanInf, double spaceInf,double spacetoHuman, double moving, int timespace, int time, int timespacegettinginfected, int maskProtection, int amountOfAreas){
		maskUsePers=mask;
		immunePers=immune;
		humanInfP=humanInf;
		humanSpaceP=spaceInf;
		spaceHumanP=spacetoHuman;
		movingP=moving;
		this.amountOfAreas=amountOfAreas;
		timeForSpaceToBeSafe=timespace;
		timeForSquareToGetInfected=timespacegettinginfected;
		this.time=time;
		this.maskProtection=maskProtection;
	}

	public Simulate(int amountOfAreas) {
		this(20,10,0.7,0.6,0.4,0.6,8,60,3,20,amountOfAreas);
	}
	
	/**
	 * This method read the border squares for of grid.
	 * 
	 * @param g the grid
	 * @param n the number of borders
	 */
	public void readBorder(Grid g, int n) {
		int x=0,y=0;
		int dest=0;
		for(int i=0; i<n; i++) {  //for each border
			boolean flag=false;
			do {
				try {
					flag=false;
					System.out.println("\nCoordinates of border ("+(i+1)+") >>"); //give the coordinates of the position
					System.out.print("\t\t\t   Give x coordinate: ");
					 x=in.nextInt();
					 System.out.print("\t\t\t   Give y coordinate: ");
					 y=in.nextInt();
					if(x!=0 && x!=g.getHeight()-1 && y!=0 && y!=g.getWidth()-1)
						throw new Exception("This position can not ba a border"); //throw exception if that position can not be a border
					
					System.out.print("\nWhere does this border lead?, (1-> for first area, 2-> for second area..): ");
					dest=in.nextInt();       //give the grid which the border leads to
					if(dest<=0 || dest>amountOfAreas)
						throw new Exception("Destination must be bigger >1 or <= than number of areas!"); //throw exception if the input was wrong
				}
				catch(InputMismatchException e) {   //catch exceptions
					flag=true;
					System.out.println(e.getMessage());
					in.nextLine();
				}
				catch(Exception e) {
					flag=true;
					System.out.println(e.getMessage());
					in.nextLine();
				}
				
				
			}while(flag);
			g.setAsBorder(x, y, dest);   // sets the coordinates and destination of border in the grid 
		}
	}
	
	
	/**
	 * This method creates all the humans in a 1D array.
	 * Gives them random positions an sets the first human as infected
	 * 
	 * @return Human[], array of humans.
	 */
	private Human[] makeHumans(int pop) {
		
		Human[] h=new Human[pop];
		for(int i=0; i<pop; i++) {
			boolean mask= randomizer.nextInt(100)<maskUsePers; //to determine if the human will be using mask
			
			if(i==0) //make the first human sick
				h[i]=new Sick(mask, humanInfP, humanSpaceP, maskProtection);
			else {
				boolean im= randomizer.nextInt(100)<immunePers;  //to determine if he will be immune
				h[i]=new Healthy(im, mask, maskProtection); //create human
			}
		}
		return h;
	}
	
	/**
	 * This method creates a 2d array with humans in random positions, representing the position that they are in.
	 * 
	 * @param human array of humans.
	 * @return 2d array of humans.
	 */
	private Human[][] make2DHuman(Human[] human, int pop, int height, int width) {
		Human[][] h = new Human[height][width];
		for(int i=0; i<pop; i++) {
			int[] pin=randomPos(height, width);  //get a random position
			while(h[pin[0]][pin[1]]!=null) {  //if that position is not null, get a new position
				pin=randomPos(height, width);
			}
			h[pin[0]][pin[1]]=human[i];
		}
		return h;
	}
	
    /**
     * This method returns 2 random numbers that represents a position.
     * 
     * @return an int [2] array with the position.
     */
    private int[] randomPos(int height, int width) {
		int[] pin= {randomizer.nextInt(height),randomizer.nextInt(width)};
		return pin;
	}
	
    /**
     * This method turns a human sick.
     * Makes a new Sick object and returns it
     * 
     * @param hu represents the human that will become sick.
     * @return returns a sick human if the human is not a immune, else returns the human healthy.
     */
    private Human makeSick(Human hu) {
    	Healthy healthy = (Healthy) hu;
    	if(!healthy.getImmune())
    	   return new Sick(hu.getMask(),humanInfP ,humanSpaceP, maskProtection);
    	return hu;
    }
    
	/**
	 * This method runs the full simulation.
	 */
	public void runSimulation() {
		Grid[] grids =new Grid[amountOfAreas];    // aray of areas as objects grid
		int pop=0,width=0 ,height=0,numOfBorders=0;
		for(int k=0; k<amountOfAreas; k++) {
			boolean error=false;
			do {
				try {
					error=false;
					System.out.println("\nSize of ("+(k+1)+") area >>");
					System.out.print("\t\t   Give the height of area ("+(k+1)+") :");  //give the height of the grid
					height=in.nextInt();
					System.out.print("\t\t   Give the width of area ("+(k+1)+") :");  //give the width of the grid
					width=in.nextInt();
					 if(height<0||width<0)
						 throw new Exception("Width and Heigth must be positive numbers"); //throw exception if input was wrong
					 
					 System.out.print("\nGive the population: ");   //give the population
					 pop=in.nextInt();
					 if(pop>width*height||pop<0)
					  throw new Exception("Population must be a positive number and must be smaller than the area");
					 //throw exception if input was wrong
					 
					 System.out.print("\nGive the number of borders: "); //give the numbers of borders that the grid has
					 numOfBorders=in.nextInt();
					 if(numOfBorders<0||numOfBorders>width*height)
					    throw new Exception("Number of borders must be a positive number, and it must be smaller than the area"); //throw exception if input is wrong
				}
				catch(InputMismatchException e) { //catch exceptions
					error=true;
					System.out.println(e.getMessage());
					
				}
				catch(Exception e) {
					error=true;
					System.out.println(e.getMessage());
					
				}
				
			}while(error);
		
			Human[][] h=make2DHuman(makeHumans(pop), pop, height ,width); //makes the 2d array
			grids[k] = new Grid(h);                                       //makes object grid
			readBorder(grids[k], numOfBorders);                           //reads the borders of the area
		}
			int t=0;
		   for(int i=0; i<time; i++) {                                     //for each minute of the simulation
			   if(t>=amountOfAreas)                                        //if t is larger than the amount of areas 
				   t=0;                                                    //reset the value to zero
			   System.out.println("\nShowing area ("+(t+1)+")!");          
			   grids[t].setDrawFlag(true);                                 //set flag as true for that grid so it can visually represent the grid
			   grids[t].drawGrid();                                        //draw the grid
			   grids[t].drawAll();
			   
			  for(int k=0; k<5 && i<time; k++) {                           //every 5 minutes shows a different area 
				 System.out.println("Minute: "+(i+1));
			     for(int j=0; j<amountOfAreas; j++) {
			        runOneMinute(j,grids);                                     //run one minute of it
			     }
			     i++;
			  }
			  i--;
			  grids[t].setDrawFlag(false);                                //set the flag as false so it does not visual represent this specific grid again 
			  t++;                                                        //increase t to move on to next grid
		   }
		
		System.out.println("The number of people who got infected is: "+ cnt);
	}
	
	
	/**
	 * This method runs one minute of the simulation.
	 * Uses Grid to call methods that determine if the person or the space get infected or not
	 * 
	 * @param g the grid
	 */
	private void runOneMinute(int area_num, Grid[] grids) {
		Grid g=grids[area_num];                                              // g is the grid of the area that is going to be simulated
		g.infectSpaces(timeForSquareToGetInfected);                          //infect all spaces that need to be
		g.noOneMoved();
		Human traveler=null;                                                 // traveler is a human who will change areas
		for(int i=0;i<g.getHeight();i++) {                                   //going through  all the spaces in the array
			for(int j=0;j<g.getWidth();j++) {
				
			if(g.getHumanAt(i, j)!=null && !g.getHasMoved(i, j)) {    
				if(g.getHumanAt(i,j).getClass()==Healthy.class) {            //if the human is healthy
					Healthy healthyPer = (Healthy) g.getHumanAt(i, j);
					if(!healthyPer.getImmune() && g.CheckForInfected(i, j)) { //and it is not immune and has gotten infected be other human
						g.setHuman(makeSick(g.getHumanAt(i, j)), i, j);       //make sick
						System.out.println("\tA person was infected in position ("+i+","+j+") by another person in area ("+(area_num+1)+")!");
						cnt++;                                                // counts how many people got infected
					}
					
					if(!healthyPer.getImmune() && g.CheckForInfectedSpace(i, j, spaceHumanP)) {   //else if it got infected by space
						g.setHuman(makeSick(g.getHumanAt(i, j)), i, j);      //make sick
						System.out.println("\tA person was infected in position ("+i+","+j+") by the space around him/her in area ("+(area_num+1)+")!");
						cnt++;                                               // counts how many people got infected
					}
				}
				if(randomizer.nextDouble()<movingP) {                        //Possibility of moving the humans
					traveler=g.move(i,j);                                    //returns a human who have left the area or returns null if no one left
					if(traveler!=null) {                                     //if it returns a human 
						if(grids[g.getBorderWith(i, j)-1].newHuman(traveler)==true)    // and if new human can be set there (then the new human will be set through the method newHuman) 
						   System.out.println("\tPerson moved from area "+(area_num+1)+" to area "+g.getBorderWith(i, j));//print where the human moved to
						else
							g.setHuman(traveler, i, j);                      // human put back in his previous position because the area is full
					}
				}
			    else 
				     g.StayedInSamePosition(i, j);                           //else increase the time stayed in same position
				
			}// null check 
			}//loop2
    	}//loop1
		g.AddFreeOfInfectedPeopleTime();                                     //counts time that space been empty of infected people
		g.AllArrayHasBeenFreeOfInfected(timeForSpaceToBeSafe);               //makes all infected spaces that have been clear of infected people for some time as safeA
	}//method
}


