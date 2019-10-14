import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CYK_Parser {

	//The node class used by the tree
	public static class Node
	{
		String element;		//Holds the element
		int start;			//Start of the phrase part
		int end;			//End of the phrase part
		String word;		//The word being contained if a leaf
		Node left;			//The left child
		Node right;			//The right child
		double prob;		//Probability

		//Create a node with all fields filled in
		public Node(String element, int start, int end, 
				String word, Node l, Node r, double prob)
		{
			this.element = element;
			this.start = start;
			this.end = end;
			this.word = word;
			this.left = l;
			this.right = r;
			this.prob = prob;
		}

		//Return a sting of the node for debug
		public String toString()
		{
			return "element: " + element + " start: " + start + " end: " + end
					+ " word: " + word + " prob: " + prob;
		}
	}
	
	//Slow method to extend an array if needed
	public static String[] extendArr(String[] old)
	{
		String[] n = new String[old.length + 100];
		for(int i = 0; i < old.length; i++)
		{
			n[i] = old[i];
		}
		return n;
	}
	
	//Give a nonterm value
	public static int nonTermVal(String s)
	{
		if(s.equals("S"))
		{
			return 0;
		}
		if(s.equals("NP"))
		{
			return 1;
		}
		if(s.equals("PP"))
		{
			return 2;
		}
		if(s.equals("PPList"))
		{
			return 3;
		}
		if(s.equals("VerbAndObject"))
		{
			return 4;
		}
		if(s.equals("VPWithPPList"))
		{
			return 5;
		}
		if(s.equals("Noun"))
		{
			return 6;
		}
		if(s.equals("Prep"))
		{
			return 7;
		}
		if(s.equals("Verb"))
		{
			return 8;
		}
		return -1;
	}
	
	public static void printTree(Node[][][] P, int N)
	{
		if(P[0][0][N-1].left == null || P[0][0][N-1].right == null)
		{
			System.out.println();
			System.out.println("This sentence cannot be parsed");
		}
		else
		{
			System.out.println();
			printTree1(P[0][0][N-1], 0);
			System.out.println("Probability = " + P[0][0][N-1].prob);
		}
	}
	
	public static void printTree1(Node node, int ind)
	{
		if(node != null)
		{
			//Print indentation
			for(int i = 0; i < ind; i++)
			{
				System.out.print("   ");
			}
			//Print the nonterminal
			System.out.print(node.element);
			
			//Print the word if the node is a leaf
			if(node.word != null)
			{
				System.out.print("  " + node.word);
			}
			System.out.println();
			
			printTree1(node.left, ind+1);
			printTree1(node.right, ind+1);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{	
		//Grammar variables
		String[][] lex = new String[12][2];	//Lexicon with POS [i][0], word [i][1]
		double[] lexProb = new double[12];	//Lexicon probabilities corresponding
		String[][] gram = new String[18][3];	//Grammar with [i][0] -> [i][1] [i][2]
		double[] gramProb = new double[18];		//Grammar probabilities corresponding
		String[] nonTerm = new String[9];
		nonTerm[0] = "S"; nonTerm[1] = "NP"; nonTerm[2] = "PP";
		nonTerm[3] = "PPList"; nonTerm[4] = "VerbAndObject";
		nonTerm[5] = "VPWithPPList"; nonTerm[6] = "Noun";
		nonTerm[7] = "Prep";	nonTerm[8] = "Verb";
		
		//Use a file to input the grammar
		File file = new File("grammar");
		Scanner sc = new Scanner(file);
		
		//Fill the context free grammar portion
		int numGram = Integer.parseInt(String.valueOf(sc.next()));
		for(int i = 0; i < numGram; i++)
		{
			gram[i][0] = sc.next();
			//System.out.print(gram[i][0]);
			gram[i][1] = sc.next();
			//System.out.print(" " + gram[i][1]);
			gram[i][2] = sc.next();
			//System.out.print(" " + gram[i][2]);
			gramProb[i] = Double.parseDouble(sc.next());
			//System.out.println(" " + gramProb[i]);
		}
		
		//Fill the lexicon portion
		int numLex = Integer.parseInt(String.valueOf(sc.next()));
		for(int i = 0; i < numLex; i++)
		{
			lex[i][0] = sc.next();
			//System.out.print(lex[i][0]);
			lex[i][1] = sc.next();
			//System.out.print(" " + lex[i][1]);
			lexProb[i] = Double.parseDouble(sc.next());
			//System.out.println(" " + lexProb[i]);
		}
		//System.out.println("\n\n\n");
		sc.close();
		
		//Input the sentence
		sc = new Scanner(System.in);
		
		int arrLength = 30;		//Variable to check if extension needed
		int N = 0;				//Number of words
		String sentance;		//The sentence input in a string
		String[] sent = new String[arrLength];	//The sentence input as an array of words
		String word = "";		//A string to hold the characters of the word

		System.out.println("Enter input in one line, no punctuation.");
		sentance = sc.nextLine();
		
		//Spiting the sentence string into words for the array
		for(int i = 0; i < sentance.length(); i++)
		{
			//Get each word separated by spaces and new lines
			while(sentance.charAt(i) != ' ' && sentance.charAt(i) != '\n')
			{
				word = word + sentance.charAt(i);
				i++;
				//Hit the end of the input string
				if(i >= sentance.length())
				{
					break;
				}
			}
			word = word.toLowerCase();
			sent[N] = word;
			word = "";
			N++;
			//Extend the array if the length is to long
			if(N >= arrLength)
			{
				arrLength = arrLength + 100;
				sent = extendArr(sent);
			}
		}
		sc.close();
			
		//Array to be filled by the CYK
		Node[][][] P = new Node[9][N][N];

		//Set the individual word POS
		for(int i = 0; i < N; i++)
		{
			word = sent[i];
			for(int rule = 0; rule < numLex; rule++)
			{
				if(word.equals(lex[rule][1]))
				{
					P[nonTermVal(lex[rule][0])][i][i] = new Node(lex[rule][0], i, i, word, null, null, lexProb[rule]);
				}
			}
		}
		
		//Parse out the combinations
		for(int length = 1; length < N; length++)	//Length of the phrase
		{
			for(int start = 0; start < N-length; start++)		//Start of the phrase
			{
				int end = start+length;					//End of the phrase
				//System.out.println(length+" "+ start +" "+ end);
				for(int rule = 0; rule < numGram; rule++)
				{
					if(P[nonTermVal(gram[rule][0])][start][end] == null)
					{
						P[nonTermVal(gram[rule][0])][start][end] = 
							new Node(gram[rule][0], start, end, null, null, null, 0.0);
					}
					
					for(int split = start; split < end; split++)	//Separates the subphrases
					{
						//System.out.println("       " + split);
						//Get the prob of the rule in this slip
						double newProb = 0.0;	//If == null, then will be 0.0 (rule does not apply)
						if(P[nonTermVal(gram[rule][1])][start][split] != null 
							&& P[nonTermVal(gram[rule][2])][split+1][end] != null)
						{
							newProb = P[nonTermVal(gram[rule][1])][start][split].prob 
								* P[nonTermVal(gram[rule][2])][split+1][end].prob * gramProb[rule];
						}
						//Set the new prob if better
						if(newProb > P[nonTermVal(gram[rule][0])][start][end].prob)
						{
							P[nonTermVal(gram[rule][0])][start][end] = 
								new Node(gram[rule][0], start, end, null, 
								P[nonTermVal(gram[rule][1])][start][split],	
								P[nonTermVal(gram[rule][2])][split+1][end], newProb);
							//System.out.println(P[nonTermVal(gram[rule][0])][start][end].toString());
						}
					}
				}
			}
		}
		
		//Print the tree
		printTree(P, N);
	}
}
