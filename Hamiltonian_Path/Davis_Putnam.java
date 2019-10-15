import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * Input: Proposition.txt with the propositions, one proposition per line
 * This takes in a list of propositions and outputs a solution to satisfy the propositions (or no solution)
 * Output: Solution.txt created to be given to the program HPBackEnd.java
 */

public class Davis_Putnam
{
	public static boolean debug = false;	//Debug info on = true
	public static int vertNum = 0;
	
	//Class to hold the propositions
	public static class node
	{
		String phrase = "";		//The string of the prop phrase
		int phraseNum = -1;		//The number of elements in the phrase
		int phraseLeft = -1;	//The number of unassigned phrases
		int[] elements;			//The elements of the phrase
		boolean[] negitive;		//If the element is negative (true if so)
		int[] truth;			//The truth values
		boolean finished = false;	//If this prop has been made true
		
		//Create a prop phrase
		public node(String phrase)
		{
			this.phrase = phrase;
			this.getPhraseNum();
			this.elements = new int[this.phraseNum];
			this.negitive = new boolean[this.phraseNum];
			this.truth = new int[this.phraseNum];
			this.truthSet();
			this.phraseParse();
		}
		
		public node(){}
		
		//Set up the initial values of the truth
		public void truthSet()
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				this.truth[i] = -1;
				this.negitive[i] = false;
			}
			this.phraseLeft = this.phraseNum;
		}
		
		//Get the number of elements in the phrase
		public int getPhraseNum()
		{
			if(phraseNum != -1)
			{
				return this.phraseNum;
			}
			
			int num = 0;
			int len = this.phrase.length();
			for(int i = 0; i < len; i++)
			{
				if(this.phrase.charAt(i) != ' ')
				{
					num++;
					
					//For cases of mult char in number
					if(this.phrase.charAt(i) == '-')
					{
						i++;
					}
					if(i < len-1)
					{
						if(this.phrase.charAt(i+1) != ' ')
							i++;
					}
				}
			}
			this.phraseNum = num;
			return num;
		}
		
		//Parse out the phrase into elements
		public void phraseParse()
		{
			int count = 0;
			int vert;
			int len = this.phrase.length();
			for(int i = 0; i < len; i++)
			{
				if(this.phrase.charAt(i) == '-')
				{
					this.negitive[count] = true;
					i++;
				}
				if(this.phrase.charAt(i) != ' ')
				{
					if(i < len-1)
					{
						if(this.phrase.charAt(i+1) != ' ')
						{
							vert = Integer.parseInt(String.valueOf(this.phrase.substring(i, i+2)));
							if(vert > vertNum)
							{
								vertNum = vert;
							}
							this.elements[count] = vert;
							count++;
							i++;
						}
						else
						{
							vert = Integer.parseInt(String.valueOf(this.phrase.substring(i, i+1)));
							if(vert > vertNum)
							{
								vertNum = vert;
							}
							this.elements[count] = vert;
							count++;
						}
					}
					else
					{
						vert = Integer.parseInt(String.valueOf(this.phrase.substring(i, i+1)));
						if(vert > vertNum)
						{
							vertNum = vert;
						}
						this.elements[count] = vert;
						count++;
					}
				}
			}
		}

		//Gets a specified element returning -1 if the element is not in the prop ignores if the element is assigned
		public int getElement(int element)
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				//Find the element
				if(this.elements[i] == element)
				{
					//Ignore if assigned
					if(this.truth[i] == -1)
					{
						return i;
					}
				}
			}
			
			return -1;
		}
		
		//Gets the truth value of the prop as a whole (0 = F, 1 = T, -1 = unfinished)
		public int getTruthTot()
		{
			boolean compleate = true;
			for(int i = 0; i < this.phraseNum; i++)
			{
				if(this.truth[i] == 0 && this.negitive[i] == true)
				{
					return 1;
				}
				else if(this.truth[i] == 1 && this.negitive[i] == false)
				{
					return 1;
				}
				else if(this.truth[i] == -1)
				{
					compleate = false;
				}
			}
			
			if(compleate == false)
			{
				return -1;
			}
			
			return 0;
		}
		
		//Gets the truth value of the element stated after negative
		public boolean getTruthElement(int element)
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				if(this.elements[i] == element)
				{
					if(this.truth[i] == 0)
					{
						if(this.negitive[i] == true)
						{
							return true;
						}
						return false;
					}
					if(this.truth[i] == 1)
					{
						if(this.negitive[i] == true)
						{
							return false;
						}
						return true;
					}
					else
					{
						System.out.println("Error - trying to get unset truth value of element");
						return false;
					}
				}
			}
			System.out.println("Error - Element " + element + " not in proposition to get " + this.phrase);
			return false;
		}

		//Gets the truth value of the element at the index stated after negative
		public boolean getTruthIndex(int index)
		{
			if(this.truth[index] == 0)
			{
				if(this.negitive[index] == true)
				{
					return true;
				}
				return false;
			}
			if(this.truth[index] == 1)
			{
				if(this.negitive[index] == true)
				{
					return false;
				}
				return true;
			}
			System.out.println("Error - Element " + this.elements[index] + " not in proposition to get(ind) " + this.phrase);
			return false;
		}
		
		//Sets the element in the prop to the truth value ignore negative, ret true if prop is changed
		public boolean setTruthElement(int element, boolean val)
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				if(this.elements[i] == element)
				{
					this.phraseLeft--;
					if(val == true)
					{
						this.truth[i] = 1;
					}
					else
					{
						this.truth[i] = 0;
					}
					return true;
				}
			}
			return false;
		}

		//Sets the element in the prop at the index to the truth value ignore negative, ret true if prop changed
		public boolean	setTruthIndex(int index, boolean val)
		{
			this.phraseLeft--;
			if(val == true)
			{
				this.truth[index] = 1;
			}
			else
			{
				this.truth[index] = 0;
			}
			return true;
		}
		
		public String toString()
		{
			return this.phrase + " Parse num = " + this.phraseNum;
		}
		
		public void printElements()
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				System.out.print(this.elements[i] + " ");
			}
		}
		
		public void printNegitive()
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				System.out.print(this.negitive[i] + " ");
			}
		}
		
		public void printTruth()
		{
			for(int i = 0; i < this.phraseNum; i++)
			{
				System.out.print(this.truth[i] + " ");
			}
		}
		
		//Print all fields of the proposition
		public void printProp()
		{
			System.out.print(this.toString() + " Elements: ");
			this.printElements();
			System.out.print(" ");
			this.printStatus();
			System.out.println(" finished: " + this.finished);
		}
		
		//Print the negative and the truth values
		public void printStatus()
		{
			System.out.print("Negitive: ");
			this.printNegitive();
			System.out.print(" Truth: ");
			this.printTruth();
		}
	}

	public static void truthInit(int[] truth)
	{
		for(int i = 0; i < truth.length; i++)
		{
			truth[i] = -1;
		}
	}
	
	//Creates a copy of the prop given
	public static node[] copyProp(node[] prop, int propNum)
	{
		node[] newProp = new node[propNum];
		
		boolean copyDebug = false;
		if(copyDebug && debug)
		{
			System.out.println("Original");
			for(int i = 0; i < propNum; i++)
			{
				System.out.print(prop[i].phrase + " Phrase Left: " + prop[i].phraseLeft + " ");
				prop[i].printProp();
			}
		}
		
		for(int i = 0; i < propNum; i++)
		{
			newProp[i] = new node();
			newProp[i].phrase = prop[i].phrase;
			newProp[i].phraseNum = prop[i].phraseNum;
			newProp[i].phraseLeft = prop[i].phraseLeft;
			newProp[i].elements = prop[i].elements.clone();
			newProp[i].negitive = prop[i].negitive.clone();
			newProp[i].truth = prop[i].truth.clone();
			newProp[i].finished = prop[i].finished;
		}
		
		if(copyDebug && debug)
		{
			System.out.println("New");
			for(int i = 0; i < propNum; i++)
			{
				newProp[i].printProp();
			}
		}
		
		return newProp;
	}
	
	//Checks for a singleton proposition and returns the number of prop removed in run, -1 if failed run
	public static int singleton(node[] prop, int[] truth, int propNum)
	{
		for(int i = 0; i < propNum; i++)
		{
			//Found a singleton
			if(prop[i].phraseLeft == 1 && prop[i].finished != true)
			{
				if(debug == true)
					System.out.print("Found Singleton int prop " + i + " ");
				
				//Find which atom is the single
				int num = prop[i].phraseNum;
				for(int c = 0; c < num; c++)
				{
					if(prop[i].truth[c] == -1)
					{
						if(debug)
							System.out.println("negitive: " + prop[i].negitive[c] + ", element: " + prop[i].elements[c]);
						
						int result;
						
						//Found the prop
						if(prop[i].negitive[c] == false)
						{
							result = propagate(prop, truth, propNum, prop[i].elements[c], true);
						}
						else
						{
							result = propagate(prop, truth, propNum, prop[i].elements[c], false);
						}
						return result;
					}
				}
			}
		}
		
		//Nothing has been changed (no singleton)
		return 0;
	}
	
	//Checks for ANY pure literal and returns the number of prop removed of the first hit, -1 if failed
	public static int pureLit(node[] prop, int[] truth, int propNum)
	{
		for(int i = 1; i <= vertNum; i++)
		{
			//Ignore already set elements, not in prop
			if(truth[i-1] != -1)
			{
				continue;
			}
			
			int result = pureLitElement(i, prop, truth, propNum);
			
			//Found a pure lit
			if(result != 0)
			{
				return result;
			}
		}
		
		//No pure lit
		return 0;
	}
	
	//Checks if the element is a pure literal and returns the number of prop removed in run, -1 if failed
	public static int pureLitElement(int element, node[] prop, int[] truth, int propNum)
	{
		int pos;				//The position of the element being looked at
		boolean set = false;	//If the negative is set
		boolean neg = false;	//If the first instance was negative
			
		if(debug)
			System.out.println("Testing elmenet " + element + " for pure lit");
		
		//Check every prop for the element
		for(int i = 0; i < propNum; i++)
		{
			//Checks for the element and gets the loc
			pos = prop[i].getElement(element);
			
			//The element is not in the prop
			if(pos == -1)
			{
				continue;
			}
		
			//First instance found
			if(set == false)
			{
				if(debug)
					System.out.println("First instance found at " + i + ", loc " + pos +  
							" with negitive = " + prop[i].negitive[pos]);
					
				set = true;
				if(prop[i].negitive[pos] == true)
				{
					neg = true;
				}
				else
				{
					neg = false;
				}
			}
			else	//Next instances
			{
				if(debug)
					System.out.println("Instance found at " + i + ", loc " + pos +
							" with negitive = " + prop[i].negitive[pos]);
				
				if(prop[i].negitive[pos] != neg)
				{
					if(debug)
						System.out.println("Not a pure lit, returning");
						
					//Not pure lit
					return 0;
				}
			}
		}
			
		//Checked the element and found an instance
		if(set == true)
		{
			return propagate(prop, truth, propNum, element, !neg);
		}
		
		if(debug)
			System.out.println("ERROR - No instances of " + element + " found in pure lit");
		
		return 0;
	}
	
	//Propagates the truth value and returns the number of prop made finished
	public static int propagate(node[] prop, int[] truth, int propNum, int element, boolean val)
	{
		//Set the truth variable
		if(val == true)
		{
			truth[element-1] = 1;
		}
		else
		{
			truth[element-1] = 0;
		}
		
		boolean res;
		int countFin = 0;
		for(int i = 0; i < propNum; i++)
		{
			//Skip finished prop
			if(prop[i].finished == true)
			{
				continue;
			}
			
			res = prop[i].setTruthElement(element, val);
			
			//The prop is changed, test to see if finished and/or failed run
			if(res == true)
			{
				if(debug)
					System.out.println("Changed prop " + i + " in propagate of element " + element + ", value: " + val);
					
				int result;
				
				result = prop[i].getTruthTot();
				if(result == 0)	//Failed run
				{
					if(debug)
						System.out.println("Failed prop " + i + " in propagate, returning failed");

					return -1;
				}
				else if(result == 1)
				{
					if(debug)
						System.out.println("Eliminated prop " + i + " in propagate");
					
					countFin++;
					prop[i].finished = true;
				}
				//result = -1 means unfinished, continuing
			}
		}
		
		if(debug)
			System.out.println("Propagate eliminated " + countFin + " prop\n");

		return countFin;
	}
	
	//The Dave-Putnam algorithm
	public static int[] DavePut(node[] prop, int[] truth, int propNum, int vertNum, int unfinished)
	{
		//Print the prop for debug
		if(debug)
		{
			System.out.println("Dave-Put\nVertNum = " + vertNum + " propNum = " + propNum);
			for(int i = 0; i < propNum; i++)
			{
				prop[i].printProp();
			}
			System.out.println("\n");
		}
				
		//Check for "easy" prop
		boolean action = true;	//Check if there is no change
		while(action == true)
		{
			action = false;
			
			//Singleton Case
			int result = singleton(prop, truth, propNum);
			if(result == -1)	//Returned failed run
			{
				if(debug)
					System.out.println("Hit a fail condition, returning fail in DavePut\n");
				
				truth[0] = -2;
				return truth;
			}
			
			//Remove the finished prop
			unfinished = unfinished - result;
			
			//No more prop left, success
			if(unfinished == 0)
			{
				if(debug)
					System.out.println("No more prop and all true, returning from DavePut\n");
				
				return truth;
			}
			
			//A prop has been removed = action taken
			if(result > 0)
			{
				action = true;
			}
			else if(debug)
				System.out.println("No results from the singleton run\n");
			
			//Pure Literal Case
			result = pureLit(prop, truth, propNum);
			if(result == -1)
			{
				if(debug)
					System.out.println("Hit a fail condition, returning fail in DavePut\n");
				
				truth[0] = -2;
				return truth;
			}
			
			unfinished = unfinished - result;
			
			if(unfinished == 0)
			{
				if(debug)
					System.out.println("No more prop and all true, returning from DavePut\n");
				
				return truth;
			}
			
			if(result > 0)
			{
				action = true;
			}
			else if(debug)
				System.out.println("No results from the pure lit run\n");
			
			if(debug && action == false)
				System.out.println("No more actions taken in easy cases");
		}
		
		//The hard cases
		//find the first unassigned vertex
		int ele = -1;
		for(int i = 0; i < vertNum; i++)
		{
			if(truth[i] == -1)
			{
				ele = i + 1;
				break;
			}
		}
		
		if(debug)
			System.out.println("Trying to assigne element " + ele + " = to true");
		
		if(ele == -1)
		{
			System.out.println("Error - No unassigned elements left for hard case");
			truth[0] = -2;
			return truth;
		}
		
		//Try setting the element to true
		//Need copy of truth so we do not destroy
		int[] truth2 = truth.clone();
		node[] prop2 = copyProp(prop, propNum);
		int res = propagate(prop2, truth2, propNum, ele, true);
		
		if(unfinished - res == 0)
		{
			if(debug)
				System.out.println("No more prop and all true, returning from DavePut hard case");
			
			return truth2;
		}
		
		if(res > 0)
		{
			truth2 = DavePut(prop2, truth2, propNum, vertNum, unfinished - res);
		
			//If the run is a success
			if(truth2[0] != -2)
			{
				return truth2;
			}
		}
		
		if(debug)
		{
			System.out.println("Trying to assigne element " + ele + " = to false");
			/*System.out.println("Printing stating prop");
			for(int i = 0; i < propNum; i++)
			{
				prop[i].printProp();
			}
			System.out.println("Start Truth");
			for(int i = 0; i < vertNum; i++)
			{
				System.out.print(truth[i] + " ");
			}
			System.out.println("\n");
			System.out.println("Printing prop2");
			for(int i = 0; i < propNum; i++)
			{
				prop2[i].printProp();
			}
			System.out.println("Truth2");
			for(int i = 0; i < vertNum; i++)
			{
				System.out.print(truth2[i] + " ");
			}
			System.out.println("\n");*/
		}
		
		//Try setting the element to false
		truth2 = truth.clone();
		prop2 = copyProp(prop, propNum);
		res = propagate(prop2, truth2, propNum, ele, false);
		
		if(unfinished - res == 0)
		{
			if(debug)
				System.out.println("No more prop and all true, returning from DavePut hard case");
			
			return truth2;
		}
		
		if(res > 0)
		{
			truth2 = DavePut(prop2, truth2, propNum, vertNum, unfinished - res);
		
			//If the run is a success
			if(truth2[0] != -2)
			{
				return truth2;
			}
		}
			
		//No possible solution setting true or false
		truth[0] = -2;
		return truth;
	}
	
	public static void main(String[] args) throws IOException
	{
		//Get the number of propositions and the number of vertices
		File file = new File("Proposition.txt");
		Scanner sc = new Scanner(file);
		
		String line;
		int propNum = 0;
		
		//Count the number of propositions
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			
			//Dont count an empty line
			if(line.equals(""))
			{
				continue;
			}
			
			//Stop this part when we hit '0'
			if(line.equals("0"))
			{
				break;
			}
			
			propNum++;
		}
		
		//End the input
		sc.close();
		
		//Variables to hold the propositions and the truth values
		node[] prop = new node[propNum];
		int unfinished = propNum;
		
		//Input the text file graph
		sc = new Scanner(file);
		
		for(int i = 0; sc.hasNextLine(); i++)
		{
			line = sc.nextLine();
			
			if(line.equals("0") || line.equals("0 ") || line.equals(" 0"))
			{
				break;
			}
			
			prop[i] = new node(line);
		}
		
		int[] truth = new int[vertNum];
		truthInit(truth);
		
		//Print the prop for debug
		if(debug)
		{
			System.out.println("Pre run\nVertNum = " + vertNum + " propNum = " + propNum);
			for(int i = 0; i < propNum; i++)
			{
				prop[i].printProp();
			}
			System.out.println("\n");
		}
		
		truth = DavePut(prop.clone(), truth.clone(), propNum, vertNum, unfinished);
		
		//Print the truth in debug form
		if(debug)
		{
			System.out.println("\n\nTruth:");
			for(int i = 0; i < vertNum; i++)
			{
				System.out.print(truth[i] + " ");
			}
		}
		
		//Print the output
		//Create the file writer and file to write to
		BufferedWriter wr = new BufferedWriter(new FileWriter("DavisPutnamOutput.txt"));

		if(truth[0] >= 0)	//There is a solution
		{
			for(int i = 0; i < vertNum; i++)
			{
				if(truth[i] == 0)
				{
					wr.write((i + 1) + " F");
				}
				if(truth[i] == 1 || truth[i] == -1)
				{
					wr.write((i + 1) + " T");
				}
				wr.newLine();
			}
		}
		
		//Write the back end info
		wr.write("0");
		wr.newLine();
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			
			if(line.equals(""))
			{
				continue;
			}
			
			wr.write(line);
			wr.newLine();
		}
		
		//End the input and output
		sc.close();
		wr.close();
	}

}
