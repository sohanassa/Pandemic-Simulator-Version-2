package team16.hw6;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class is for reading the inputs from the user and running the simulation
 * based on the inputs given.
 * 
 * @author Zoe Passiadou
 * @author Sohaib Nassar
 *
 */
public class ReadFromUser {

	public static void main(String[] args) {
		System.out.println("COVID-19 SIMULATOR!\n");
	       System.out.println("*StdDraw Disclaimer*\n1)Green Circle <- Healthy Person\n2)Orange Circle <- Sick Person\n3)Red Square <- Infected Area\n4)Purple Space <- Infected Border\n5)Grey Space <- Border");
		Scanner in = new Scanner(System.in);
		String choice="";
		boolean flag=false;
		Simulate s;
		int amountOfAreas=0;
		
		do {
			try {
				flag=false;
				System.out.print("\nGive number of areas: ");
				amountOfAreas=in.nextInt();
				if(amountOfAreas<=0)
					throw new Exception();
				System.out.print("\nGive number of areas: "); 
				amountOfAreas=in.nextInt();//gets number of areas
				System.out.print("\nWould you like a manual simulator? (Yes/No): ");
				choice=in.next(); //represents whether or not it will be a manual simulator or not
				if(!choice.equalsIgnoreCase("Yes")&&!choice.equalsIgnoreCase("No"))
					throw new Exception(); //throw exception if answer was not yes/no
			}
			catch (Exception e) {
				in.nextLine();
				flag=true;
				System.out.println("Wrong input, please enter again:");
			}
		}while(flag);
		
		int h=0,w=0,people=0,time=0,timeSpace=0,maskPers=0,immunePers=0,timeSpaceInfected=0,maskProtection=0;
		double movingP=0,infectingP=0,infSpaceP=0,spaceInfHuman=0;
			 
		if(choice.equalsIgnoreCase("No")){
			 s= new Simulate(amountOfAreas);
		}
		 	
		
		else {
		boolean error=false;
	do {                                         // do while loop for re reading in case exception was thrown
		try {                                    //try for reading all inputs 
		   error=false;  //for checking if exception was thrown
		
		   System.out.print("Give time of simmulation in minutes:");
		   time = in.nextInt();
		    if(time<=0)   //if the time given is negative
		    	throw new Exception("Time must be larger than 0"); //throw exception
		
		   System.out.print("Give the time needed for a space to become disinfected:");
		   timeSpace = in.nextInt();
		   if(timeSpace<=0) //if the time given is negative
		    	throw new Exception("Time must be larger than 0");//throw exception
		   
		   System.out.print("Give the time needed for a space to get infected:");
		   timeSpaceInfected = in.nextInt();
		   if(timeSpaceInfected<=0)//if the time given is negative
		    	throw new Exception("Time must be larger than 0");//throw exception
		   System.out.print("Give possibility of moving (between 0-1):");
		   movingP = in.nextDouble();
		   if(movingP>1 || movingP<0) //if the possibility is not from 0-1
			   throw new Exception("possibility of moving must be between 0 and 1!");//throw exception
		
		   System.out.print("Give possibility of infecting another human (between 0-1):");
		   infectingP = in.nextDouble();
		   if(infectingP>1 || infectingP<0) //if the possibility is not from 0-1
			   throw new Exception("possibility of infecting another human must be between 0 and 1!");//throw exception
		
		   System.out.print("Give possibility of infecting a space (between 0-1):");
		   infSpaceP = in.nextDouble();
		   if(infSpaceP>1 || infSpaceP<0)//if the possibility is not from 0-1
			   throw new Exception("possibility of infecting a space must be between 0 and 1!");//throw exception
		
		   System.out.print("Give possibility of getting infected froma a space (between 0-1):");
		   spaceInfHuman = in.nextDouble();
		   if(spaceInfHuman>1 || spaceInfHuman<0)//if the possibility is not from 0-1
			   throw new Exception("possibility of getting infected froma a space must be between 0 and 1!");//throw exception
		
		   System.out.print("Give percentage of mask use (between 0-100):");
		   maskPers = in.nextInt();
		   if(spaceInfHuman>100 || spaceInfHuman<0)// if the possibility is not from 0-100
			   throw new Exception("percentage of mask use must be between 0 and 100!");//throw exception
		
		   System.out.print("Give percentage of immune peoplee (between 0-100):");
		   immunePers = in.nextInt();
		   if(immunePers>100 || immunePers<0)// if the possibility is not from 0-100
			   throw new Exception("percentage of immune people must be between 0 and 100!");//throw exception
		   
		   System.out.print("Give the percentage of protection that a face mask gives (between 0-100):");
		     maskProtection=in.nextInt();
		     if(maskProtection>100 || maskProtection<0)// if the possibility is not from 0-100
				   throw new Exception("percentage must be between 0 and 100!");//throw exception
		     
		     
        }
		
		catch(InputMismatchException e) {              //catch  InputMismatchException
			 error=true;                               //set error as true
		     System.out.println("Wrong input!");
		     in.nextLine();
	       }
		
		catch(Exception e) {//catch  InputMismatchException
			error=true;//set error as true
			System.out.println(e.getMessage());
			in.nextLine();
	    	}

		
	   }while(error);
		//create an object type Simulate
	  s= new Simulate(maskPers,immunePers,infectingP,infSpaceP,spaceInfHuman,movingP,timeSpace,time,timeSpaceInfected,maskProtection,amountOfAreas);
		}
	   s.runSimulation(); //call runSimulation
	   System.out.println("END OF SIMULATION!");
	}
}