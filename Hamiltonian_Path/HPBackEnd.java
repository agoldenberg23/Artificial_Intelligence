import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * Input: DavisPutnamOutput.txt from the Davis Putnam algorithm
 * Output: Output.txt with the list of vertices satisfying the Hamiltonian Path in order
 */

public class HPBackEnd
{
	public static boolean debug = false;
	
	public static void main(String[] args) throws IOException
	{
		//Input the text file from Davis Putnam
		File file = new File("DavisPutnamOutput.txt");
		Scanner sc = new Scanner(file);
		
		//An array to create the order
		int[] vert = new int[26];
		int pos = 0;
		
		//Get the true vert in order
		String line;
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			
			//Skip empty lines
			if(line.equals(""))
			{
				continue;
			}
			
			//Go to next part to get the names
			if(line.equals("0") || line.equals("0 ") || line.equals(" 0"))
			{
				break;
			}
			
			if(line.substring(line.length()-1, line.length()).equals("T"))
			{
				//Two digit number
				if(line.substring(1,2).equals(" ") == false)
				{
					if(debug)
						System.out.println("Element " + line.substring(0, 2) + " is T, Add it at pos " + pos);
					
					vert[pos] = Integer.parseInt(String.valueOf(line.substring(0, 2)));	
				}
				else	//One digit number
				{
					if(debug)
						System.out.println("Element " + line.substring(0, 1) + " is T, Add it at pos " + pos);
					
					vert[pos] = Integer.parseInt(String.valueOf(line.substring(0, 1)));	
				}
				
				pos++;
				if(pos >= 26)
				{
					System.out.println("ERROR - More thean 26 vertices");
				}
			}
			//Else skip the line
		}
		
		//Create file and file writer
		BufferedWriter wr = new BufferedWriter(new FileWriter("Output.txt"));
		
		//No Solution
		if(pos == 0)
		{
			wr.write("NO SOLUTION");
			sc.close();
			wr.close();
			return;
		}
		
		//Get the names
		String[] names = new String[pos];
		int[] times = new int[pos];
		
		int pos2 = 0;
		for(int i = 1; sc.hasNextLine(); i++)
		{
			line = sc.nextLine();
			
			//Skip empty lines
			if(line.equals(""))
			{
				continue;
			}
			
			if(vert[pos2] == i)
			{
				//Is a two digit number
				if(line.substring(line.length()-2, line.length()-1).equals(" ") == false)
				{
					if(debug)
						System.out.println("Pos " + pos2 + " is named " + line.substring(line.length()-4, line.length()));
					
					names[pos2] = (line.substring(line.length()-4, line.length()-3));
					times[pos2] = Integer.parseInt(String.valueOf(line.substring(line.length()-2, line.length())));
				}
				else	//Is a one digit number
				{
					if(debug)
						System.out.println("Pos " + pos2 + " is named " + line.substring(line.length()-3, line.length()));
					
					names[pos2] = (line.substring(line.length()-3, line.length()-2));
					times[pos2] = Integer.parseInt(String.valueOf(line.substring(line.length()-1, line.length())));
				}
				
				pos2++;
			}
		}
		
		if(debug)
		{
			for(int i = 0; i < pos; i++)
			{
				System.out.println(names[i] + " " + times[i]);
			}
		}
		
		//Sort the names by time order and write the name
		int tempInt;
		String tempString;
		
		for (int i = 0; i < pos - 1; i++)
		{
            for (int j = 0; j < pos - i - 1; j++)
            {
                if ((times[j] > times[j+1])) 
                {
                	tempInt = times[j];
                	tempString = names[j];
                	times[j] = times[j+1];
                	names[j] = names[j+1];
                	times[j+1] = tempInt;
                	names[j+1] = tempString;
                }
			}
		}
		
		pos2 = 0;
		for(int i = 0; i < pos; i++)
		{
			wr.write(names[i]);
			
			pos2++;
			if(pos2 < pos)
			{
				wr.write(", ");
			}
		}
		
		//End the input and output
		sc.close();
		wr.close();
	}

}
