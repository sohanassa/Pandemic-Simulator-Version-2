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
	//private static int height;                    //  the height of the place
	//private static int width;                     //  the width of the place
	//private static int population;                //  the number of people in the simulation
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
		//height=h;
		//width=w;
		//population=pop;
		this.amountOfAreas=amountOfAreas;
		timeForSpaceToBeSafe=timespace;
		timeForSquareToGetInfected=timespacegettinginfected;
		this.time=time;
		this.maskProtection=maskProtection;
	}

	public Simulate(int amountOfAreas) {
		this(20,10,0.7,0.6,0.4,0.6,8,60,3,20,amountOfAreas);
	}
	
	public void readBorder(Grid g, int n) {
		int x=0,y=0;
		int dest=0;
		for(int i=0; i<n; i++) {
			boolean flag=false;
			do {
				try {
					flag=false;
					System.out.print("Give the position of the border (x, y): ");
					 x=in.nextInt();
					 y=in.nextInt();
					if(y!=0&&y!=g.getHeight()-1&&x!=0&&x!=g.getWidth()-1)
						throw new Exception("This position can not ba a border");
					
					System.out.print("Where does this border lead?, (1-> for first area, 2-> for second area..): ");
					dest=in.nextInt();
					if(dest<=0 || dest>amountOfAreas)
						throw new Exception("Destination must be bigger >1 or <= than number of areas!");
				}
				catch(InputMismatchException e) {
					flag=true;
					System.out.println(e.getMessage());
				}
				catch(Exception e) {
					flag=true;
					System.out.println(e.getMessage());
				}
				
			}while(flag);
			g.setAsBorder(x, y, dest);
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
		Grid[] grids =new Grid[amountOfAreas];
		int pop=0,width=0 ,height=0,numOfBorders=0;
		for(int k=0; k<amountOfAreas; k++) {
			// need to add system.out
			boolean error=false;
			do {
				try {
					error=false;
					System.out.print("Give the height and width of area ("+(k+1)+") :");
					    height=in.nextInt();
					    width=in.nextInt();
					 if(height<0||width<0)
						 throw new Exception("Width and Heigth must be positive numbers");
					 
					 System.out.print("Give the population: ");
					    pop=in.nextInt();
					   if(pop>width*height||pop<0)
						   throw new Exception("Population must be a positive number and must be smaller than the area");
					   
					   System.out.print("Give the number of borders: ");
					           numOfBorders=in.nextInt();
					    if(numOfBorders<0||numOfBorders>width*height)
					    	throw new Exception("Number of borders must be a positive number, and it must be smaller than the area");
				}
				catch(InputMismatchException e) {
					error=true;
					System.out.println(e.getMessage());
					
				}
				catch(Exception e) {
					error=true;
					System.out.println(e.getMessage());
					
				}
				
			}while(error);
		
			Human[][] h=make2DHuman(makeHumans(pop), pop, height ,width);
			grids[k] = new Grid(h);
			readBorder(grids[k], numOfBorders);
		}
		
		for(int i=0; i<time; i++) { //for each minute of the simulation
			System.out.println("Minute: "+(i+1));
			for(int j=0; j<amountOfAreas; j++) {
				runOneMinute(j,grids); //run one minute of it
			}
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
		Grid g=grids[area_num];
		g.infectSpaces(timeForSquareToGetInfected); //infect all spaces that need to be
		Human traveler=null;
		for(int i=0;i<g.getHeight();i++) {                                  //going through  all the spaces in the array
			for(int j=0;j<g.getWidth();j++) {
				
			if(g.getHumanAt(i, j)!=null) {                              
				if(g.getHumanAt(i,j).getClass()==Healthy.class) {    //if the human is healthy
					Healthy healthyPer = (Healthy) g.getHumanAt(i, j);
					if(!healthyPer.getImmune() && g.CheckForInfected(i, j)) { //and it is not immune and has gotten infected be other human
						g.setHuman(makeSick(g.getHumanAt(i, j)), i, j);       //make sick
						System.out.println("\tA person was infected in position ("+i+","+j+") by another person!");
						cnt++;    // counts how many people got infected
					}
					
					if(!healthyPer.getImmune() && g.CheckForInfectedSpace(i, j, spaceHumanP)) {//else if it got infected by space
						g.setHuman(makeSick(g.getHumanAt(i, j)), i, j);   //make sick
						System.out.println("\tA person was infected in position ("+i+","+j+") by the space around him/her!");
						cnt++;    // counts how many people got infected
					}
				}
				if(randomizer.nextDouble()<movingP) {  //move the humans
					//int bordering = g.getBorderWith(i, j);
					traveler=g.move(i,j);
					if(traveler!=null) {  //an pume if(move) then check which on it border and then call the method to add a new human 
						grids[g.getBorderWith(i, j)-1].newHuman(traveler);
						System.out.println("Person moved from area "+(area_num+1)+" to area "+g.getBorderWith(i, j));
					}
				}
			    else
				     g.StayedInSamePosition(i, j); //else increase the time stayed in same position
				
			}// null check 
			}//loop2
    	}//loop1
		g.AddFreeOfInfectedPeopleTime();                         //counts time that space been empty of infected people
		g.AllArrayHasBeenFreeOfInfected(timeForSpaceToBeSafe);  //makes all infected spaces that have been clear of infected people for some time as safeA
	}//method
	
}

