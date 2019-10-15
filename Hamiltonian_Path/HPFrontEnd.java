import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * This is the front end of the Hamiltonian Path problem
 * Input: Graph.txt in same relative path
 * Uses the input to create a list of propositions
 * Output: Proposition.txt in same relative path for Davis_Putnam.java
*/

public class HPFrontEnd
{
	//Prints out a given graph to view
	public static void printGraph(char[][] graph, int vertNum)
	{
		System.out.println("Graph: ");
		for(int i = 0; i < vertNum; i++)
		{
			for(int c = 0; c < vertNum; c++)
			{
				if(graph[i][c] == 0)
				{
					System.out.print("0 ");
				}
				else if(c == 0)
				{
					System.out.print(graph[i][c] + "| ");
				}
				else
				{
					System.out.print(graph[i][c] + " ");
				}
			}
			System.out.println();
		}
	}
	
	//Sorts the graph by start vertices and then edges
	public static char[][] sortGraph(char[][] graph, int vertNum)
	{
		char[] temp = new char[vertNum];
		
		for (int i = 0; i < vertNum - 1; i++)
		{
            for (int j = 0; j < vertNum - i - 1; j++)
            {
                if ((graph[j][0] > graph[j+1][0]) && graph[j][0] != 0 && graph[j+1][0] != 0) 
                {
                	temp = graph[j];
                	graph[j] = graph[j+1];
                	graph[j+1] = temp;
                }
			}
		}
		
		graph = sortEdges(graph, vertNum);
		
		return graph;	
	}
	
	//Sort all of the edges of a graph
	public static char[][] sortEdges(char[][] graph, int vertNum)
	{
		for(int i = 0; i < vertNum; i++)
		{
			graph[i] = sortEdge(graph[i], vertNum);
		}
		return graph;
	}
	
	//Sort the edge order
	public static char[] sortEdge(char[] row, int vertNum)
	{
		char temp;
		
		for (int i = 0; i < vertNum - 1; i++)
		{
            for (int j = 1; j < vertNum - i - 1; j++)
            {
                if ((row[j] > row[j+1]) && row[j] != 0 && row[j+1] != 0) 
                {
                	temp = row[j];
                	row[j] = row[j+1];
                	row[j+1] = temp;
                }
			}
		}
		
		return row;
	}
	
	public static void main(String[] args) throws IOException
	{	
		//Input the text file graph
		File file = new File("Graph.txt");
		Scanner sc = new Scanner(file);
		StringBuffer prop = new StringBuffer();
		
		boolean debug = false;	//Set true to add some debug info
		
		int vertNum = Integer.parseInt(String.valueOf(sc.nextLine()));	//Hold the number of vertices
		char[][] graph = new char[vertNum][vertNum];	//Hold the graph data
		
		 //Graph organized with graph[i][0] being the start vertex
		 //All next char is the vertex which the start vertex has an edge to
		
		//Fill out the starting vertex of the graph
		for(int i = 0; i < vertNum; i++)
		{
			graph[i][0] = (char) (65 + i);
		}
		
		if(debug == true)
			System.out.println("Num of Vertices = " + vertNum);
		
		while(sc.hasNextLine())
		{
			String line = sc.nextLine();

			for(int i = 0; i < vertNum; i++)
			{
				//Vertex found in graph, adding next edge
				if(graph[i][0] == line.charAt(0))
				{
					for(int c = 1; c < vertNum; c++)
					{
						if(graph[i][c] == line.charAt(2))
						{
							System.out.println("ERROR - Repeated Edge");
							break;
						}
						else if(graph[i][c] == 0)
						{
							graph[i][c] = line.charAt(2);
							break;
						}
					}
					break;
				}
			}
		}
		
		//Stop reading in the graph
		sc.close();
		
		//Sort the edges of the graph to assure good input
		graph = sortEdges(graph, vertNum);
		
		//Print the graph
		if(debug == true)
		{
			printGraph(graph, vertNum);
			System.out.println();
		}
		
		//Create the file writer and file to write to
		BufferedWriter wr = new BufferedWriter(new FileWriter("Proposition.txt"));
		
		if(debug == true)
			System.out.println("Prop 1: Every vertex is traversed at some time. (A1 V A2 V A3 V A4 V A5 V A6 V A7 V A8)");
		
		for(int vert = 0; vert < vertNum; vert++)
		{
			for(int time = 1; time <= vertNum; time++)
			{
				prop.append((vertNum * vert + time) + " ");
			}
			System.out.println(prop.toString());
			wr.write(prop.toString());
			wr.newLine();
			prop.delete(0, prop.length());
		}
		
		if(debug == true)
			System.out.println("\nProp 2: No pair of vertices are traversed at the same time. (¬A1 V ¬B1)");
		
		for(int time = 0; time < vertNum; time++)
		{
			for(int vert1 = 0; vert1 < vertNum-1; vert1++)
			{
				for(int vert2 = 1; vert2 < vertNum-vert1; vert2++)
				{
					prop.append("-" + (vertNum * vert1 + 1 + time) + " -" + (vertNum * (vert1 + vert2) + 1 + time));
					System.out.println(prop.toString());
					wr.write(prop.toString());
					wr.newLine();
					prop.delete(0, prop.length());
				}
			}
		}
		
		if(debug == true)
			System.out.println("\nProp 3: You cannot go from U at time T to W at time T+1 if there is no edge from U to W. (¬UT V ¬W(T+1))");
		
		//Chose a starting vertex
		for(int i = 0; i < vertNum; i++)
		{
			//Chose an ending vertex
			for(int c = 0; c < vertNum; c++)
			{
				int vertCheck = 65 + c;
				//System.out.print(graph[i][0] + " " + (char)vertCheck + " ");
				boolean isIn = false;
				//Check all possible edges to see if the edge exists
				for(int j = 1; j < vertNum; j++)
				{
					if((int)graph[i][0] == vertCheck)
					{
						//System.out.println();
						isIn = true;
						break;
					}
					if(((int)graph[i][j]) == vertCheck)
					{
						//System.out.println(graph[i][j]);
						isIn = true;
					}
				}
				if(isIn == false)
				{
					for(int time = 0; time < vertNum-1; time++)
					{
						prop.append("-" + (vertNum * i + 1 + time) + " -" + (vertNum * (c) + 2 + time));
						System.out.println(prop);
						wr.write(prop.toString());
						wr.newLine();
						prop.delete(0, prop.length());
					}
				}
			}
		}
		
		/*
 		* 4. (Optional) At every time there is a vertex. (A1 V B1 V C1 V D1 V E1 V F1 V G1 V H1)
 		* 5. (Optional) No vertex is traversed more than once. (¬ A1 V ¬ A2)
 		*/
		
		//Giving the back end info
		wr.write("0");
		wr.newLine();
		if(debug == true)
			System.out.println("\nBack end info");
		
		System.out.println("0");

		for(int vert = 0; vert < vertNum; vert++)
		{
			for(int time = 1; time <= vertNum; time++)
			{
				prop.append((vertNum * vert + time) + " " + graph[vert][0] + " " + time);
				System.out.println(prop.toString());
				wr.write(prop.toString());
				wr.newLine();
				prop.delete(0, prop.length());
			}
		}
		
		//Close the file writer
		wr.close();
	}
}
