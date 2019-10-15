//This algorithm solves the knapsack problem with iterative deepening.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Knapsack_ID
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
		
	
	//A depth first search
	public static String[]  DFS(String node[], double totValue, double totWeight, double tarValue, 
			double tarWeight, String items[], double values[], double weights[], int itemNum, int k)
	{
		//System.out.println("totValue = " + totValue + " totWeight = " + totWeight);
		
		//Have a goal state
		if(totValue >= tarValue && totWeight <= tarWeight)
		{
			/*System.out.println("Returning node:");
			for(int i = 0; i < node.length; i++)
			{
				if(node[i] != null)
				{
					System.out.println(node[i] + " ");

				}
			}*/
			return node;
		}
		
		//Exceeded the weight limit, don't continue
		if(totWeight > tarWeight || k <= 0)
		{
			return null;
		}
		
		String[] ans = null;
		
		//Go through children of the node
		for(int i = itemNum - 1; i >= 0; i--)
		{
			//Hit the last active item (farthest alphabetical order)
			if(node[i] != null)
			{
				break;
			}
			
			//Add the item
			node[i] = items[i];
			totValue = values[i] + totValue;
			totWeight = weights[i] + totWeight;
			
			//System.out.println("Add element: " + node[i]);
			
			ans = DFS(node, totValue, totWeight, tarValue, tarWeight, 
					items, values, weights, itemNum, k - 1);

			if(ans != null)
			{
				//System.out.println("return ans");
				return ans;
			}
			
			//Remove the item (not wanted)
			node[i] = null;
			totValue = totValue - values[i];
			totWeight = totWeight - weights[i];
			
			//System.out.println("Remove element: " + node[i]);
		}
		
		return null;
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
		String[] node = new String[items.length];
		
		for(int i = 1; i < itemNum; i++)
		{
			node = new String[items.length];
			
			//Run the DFS and save the solution
			node = DFS(node, 0.0, 0.0, tarValue, tarWeight, items, values, weights, itemNum, i);
			
			if(node != null)
			{
				break;
			}
		}

		//Print the output
		if(node == null)	//There is no solution
		{
			System.out.println("No solution");
		}
		else	//Print object names if there is a solution
		{
			for(int i = 0; i < node.length; i++)
			{
				if(node[i] != null)
				{
					System.out.print(node[i] + " ");
				}
			}
		}

	}
}
