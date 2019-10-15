//This algorithm solves the knapsack problem with hill climbing.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Knapsack_HC
{
	//Slow methods to extend an array if needed
	public static String[] extendArr(String[] old)	//For strings
	{
		String[] n = new String[old.length + 100];
		for(int i = 0; i < old.length; i++)
		{
			n[i] = old[i];
		}
		return n;
	}
	public static double[] extendArr(double[] old)	//For doubles
	{
		double[] n = new double[old.length + 100];
		for(int i = 0; i < old.length; i++)
		{
			n[i] = old[i];
		}
		return n;
	}
	
	//Swap two elements
	public static void swap(String arr[], int a, int b)
	{
		String temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
	public static void swap(double arr[], int a, int b)
	{
		double temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
	
	//Sort the array with a slow algorithm
	public static void sort(String items[], double values[], 
			double weights[], int itemNum)
	{
		for(int i = 0; i < itemNum - 1; i++)
		{
			for(int c = 0; c < itemNum - i - 1; c++)
			{
				if(items[c].charAt(0) > items[c+1].charAt(0))
				{
					//Need to keep the arrays in line
					swap(items, c, c+1);
					swap(values, c, c+1);
					swap(weights, c, c+1);
				}
				//First char are equal, checking for next char
				else if(items[c].charAt(0) == items[c+1].charAt(0))
				{
					boolean swaped = false;
					int len = Math.min(items[c].length() , items[c+1].length());
					for(int a = 1; a < len; a++)
					{
						if(items[c].charAt(a) > items[c+1].charAt(a))
						{
							//Need to keep the arrays in line
							swap(items, c, c+1);
							swap(values, c, c+1);
							swap(weights, c, c+1);
							swaped = true;
							break;
						}
					}
					
					//Have same char but one is longer, longer goes latter
					if(items[c].length() > items[c+1].length() && swaped == false)
					{
						//Need to keep the arrays in line
						swap(items, c, c+1);
						swap(values, c, c+1);
						swap(weights, c, c+1);
					}
					//Elements are the same, printing error and returning
					else if(items[c].equals(items[c+1]))
					{
						System.out.println("Error - Two object names are the same. Failed to sort.");
						return;
					}
				}
			}
		}
	}
	
	//Return the error value of the input
	public static double error(String node[], double tarValue, double tarWeight, 
			double values[], double weights[], int itemNum)
	{
		double weightCur = 0;
		double valueCur = 0;
		
		for(int i = 0; i < itemNum; i++)
		{
			if(node[i] != null)
			{
				weightCur = weightCur + weights[i];
				valueCur = valueCur + values[i];
			}
		}
		return Math.max((weightCur - tarWeight), 0) + Math.max((tarValue - valueCur), 0);
	}
	//Return the error when weights and value is known
	public static double error(double tarValue, double tarWeight, double valueCur, double weightCur)
	{
		return Math.max((weightCur - tarWeight), 0) + Math.max((tarValue - valueCur), 0);
	}
	
	
	//The hill climbing algorithm
	public static String[] hillClimb(final String node[], double totValue, double totWeight, double tarValue, 
			double tarWeight, String items[], double values[], double weights[], int itemNum)
	{	
		//Node to save the solutions from the hill climb
		String[] sol = new String[node.length];
		double solValue = 0.0;
		double solWeight = 0.0;
		double ans = 0.0;
		double err = error(tarValue, tarWeight, totValue, totWeight);
		boolean change = false;
		double solErr = err;
		
		//Iterate over the additions and deletions
		for(int i = 0; i < itemNum; i++)
		{	
			//Additions
			if(node[i] == null)
			{
				//System.out.println("Addition try at = " + i);
				
				ans = error(tarValue, tarWeight, values[i] + totValue, weights[i] + totWeight);
				
				//Ans is better than the node and previous best
				if(ans < err)
				{
					//System.out.println("Adding " + items[i] + " err = " + ans + " ");
					err = ans;
					sol = new String[node.length];
					for(int c = 0; c < itemNum; c++)
					{
						if(node[c] != null)
						{
							sol[c] = node[c];
						}
					}
					sol[i] = items[i];
					solWeight = totWeight + weights[i];
					solValue = totValue + values[i];
					solErr = ans;
					change = true;
					/*for(int i2 = 0; i2 < sol.length; i2++)
					{
						if(sol[i2] != null)
						{
							System.out.print(sol[i2] + " ");
						}
					}
					System.out.println();*/
				}
			}	
			//Deletions
			if(node[i] != null)
			{
				//System.out.println("Deletion try at = " + i);
				
				ans = error(tarValue, tarWeight, totValue - values[i], totWeight - weights[i]);
				
				//Ans is better than the node and previous best
				if(ans < err)
				{
					//System.out.println("Deleting " + items[i] + " err = " + ans + " ");
					err = ans;
					sol = new String[node.length];
					for(int c = 0; c < itemNum; c++)
					{
						if(node[c] != null)
						{
							sol[c] = node[c];
						}
					}
					sol[i] = null;
					solWeight = totWeight - weights[i];
					solValue = totValue - values[i];
					solErr = ans;
					change = true;
					/*for(int i2 = 0; i2 < sol.length; i2++)
					{
						if(sol[i2] != null)
						{
							System.out.print(sol[i2] + " ");
						}
					}
					System.out.println();*/
				}
			}
		}
		
		//Iterate over the replacements
		for(int i = 0; i < itemNum; i++)
		{
			//Not an element in set to replace
			if(node[i] == null)
			{
				continue;
			}
			
			//Remove the current item to replace
			double valueCur = totValue - values[i];
			double weightCur = totWeight - weights[i];
			
			//Iterate over objects to replace with
			for(int replace = 0; replace < itemNum; replace++)
			{
				//Replace is not an element to replace with or is already in node
				if(node[replace] != null)
				{
					continue;
				}
				
				ans = error(tarValue, tarWeight, valueCur + values[replace], 
						weightCur + weights[replace]);
				
				//Ans is better than the node and previous best
				if(ans < err)
				{
					//System.out.println("Replacing: " + node[i] + " for " + sol[replace] + " loc: " + i + ", ");
					err = ans;
					sol = new String[node.length];
					for(int c = 0; c < itemNum; c++)
					{
						if(node[c] != null)
						{
							sol[c] = node[c];
						}
					}
					sol[i] = null;
					sol[replace] = items[replace];
					solWeight = totWeight - weights[i] + weights[replace];
					solValue = totValue - values[i] + values[replace];
					solErr = ans;
					change = true;
					/*for(int i2 = 0; i2 < sol.length; i2++)
					{
						if(sol[i2] != null)
						{
							System.out.print(sol[i2] + " ");
						}
					}
					System.out.println();*/
				}
			}
		}
		
		//Check if the node is the target
		//if so return
		if(solErr <= 0)
		{
			/*System.out.print("Solution: ");
			for(int i = 0; i < sol.length; i++)
			{
				if(sol[i] != null)
				{
					System.out.print(sol[i] + " ");
				}
			}
			System.out.println();*/
			return sol;
		}
		//if not, run hillClimb and return result
		else if(change == true)
		{
			/*System.out.print("Recursive: ");
			for(int i = 0; i < sol.length; i++)
			{
				if(sol[i] != null)
				{
					System.out.print(sol[i] + " ");
				}
			}
			System.out.println("value = " + solValue + " weight = " + solWeight + " err = " + solErr);*/
			return hillClimb(sol, solValue, solWeight, tarValue, tarWeight, 
					items, values, weights, itemNum);
		}
		
		/*System.out.print("No change: ");
		for(int i = 0; i < node.length; i++)
		{
			if(node[i] != null)
			{
				System.out.print(node[i] + " ");
			}
		}
		System.out.println("value = " + totValue + " weight = " + totWeight + " err = " + err);*/
		return node;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		//Create the knapsack variables
		String[] items = new String[30];	//Holds the names of the items
		double[] values = new double[30];	//The values of the corresponding items
		double[] weights = new double[30];	//The weights of the corresponding items
		int itemNum = 0;	//Number of items in the array
		double tarWeight = 0;	//The target weight wanted
		double tarValue = 0;		//The target value wanted
		
		//Reading in the file and filling the knapsack variables
		//Use a file to input the knapsack values
		File file = new File("Knapsack");
		Scanner sc = new Scanner(file);

		//Target value and the maximum weight
		tarValue = sc.nextDouble();
		tarWeight = sc.nextDouble();
		
		//Add the objects
		while(sc.hasNextLine())
		{
			items[itemNum] = sc.next();
			values[itemNum] = sc.nextDouble();
			weights[itemNum] = sc.nextDouble();
			itemNum = itemNum + 1;
			
			//Extend the arrays if the input exceeds there maximum
			if(itemNum >= items.length)
			{
				extendArr(items);
				extendArr(values);
				extendArr(weights);
			}
		}
		
		//Stop reading in
		sc.close();
		
		//Sort the input
		sort(items, values, weights, itemNum);
		
		//Print statement for debugging
		/*System.out.println("tar value = " + tarValue);
		System.out.println("tar weight = " + tarWeight);
		for(int a = 0; a < itemNum; a++)
		{
			System.out.println("Name: " + items[a] + " Value: " + values[a] + " Weight: " + weights[a]);
		}
		System.out.println("item number = " + itemNum + "\n");*/
		
		//Node to save the solutions
		String[] node;
		double nodeWeight = 0.0;
		double nodeValue = 0.0;
		
		String[] ans = null;	//Save the best solution
		double ansErr = 0.0;
		
		//Random number generator
		Random rand = new Random();
		int num;
		
		for(int run = 0; run < 10; run++)
		{
			//Assign the starting elements of node
			node = new String[items.length];
			nodeValue = 0;
			nodeWeight = 0;
			
			//Go through elements and decide if in or not
			for(int i = 0; i < itemNum; i++)
			{
				num = rand.nextInt(2);
				if(num == 0)
				{
					node[i] = items[i];
					nodeValue = nodeValue + values[i];
					nodeWeight = nodeWeight + weights[i];
				}
			}
			
			/*System.out.println("\n\n\nWeight = " + nodeWeight + " Value = " + nodeValue);
			for(int c = 0; c < itemNum; c++)
			{
				if(node[c] != null)
				{
					System.out.print(node[c] + " ");
				}
			}
			System.out.println();*/
			
			//Run the DFS and save the solution
			node = hillClimb(node, nodeValue, nodeWeight, tarValue, tarWeight, 
					items, values, weights, itemNum);
			
			double nodeErr = error(node, tarValue, tarWeight, values, weights, itemNum);
			
			//Assign solutions
			if(nodeErr <= ansErr)
			{
				ans = new String[node.length];
				for(int c = 0; c < itemNum; c++)
				{
					ans[c] = node[c];
				}
				ansErr = nodeErr;
			}
		}

		//Print the output
		if(ans == null)	//There is no solution
		{
			System.out.println("No solution");
		}
		else	//Print object names if there is a solution
		{
			for(int i = 0; i < ans.length; i++)
			{
				if(ans[i] != null)
				{
					System.out.print(ans[i] + " ");
				}
			}
		}

	}
}
