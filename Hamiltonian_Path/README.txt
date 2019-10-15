The Hamiltonian path problem is split into three different codes ran in the 
following order.
1] HPFrontEnd.java
2] Davis_Putnam.java
3] HPBackEnd.java

This will give details for each of the algorithms separately.

HPFrontEnd.java
This can be ran with the following commands:
javac HPFrontEnd.java
java HPFrontEnd

The input is given in the text file “Graph.txt” and the output is 
“Proposition.txt”. The graph file has the name hardcoded and will be taken 
from the same relative path as the program. Graph file formatting is bellow 
and an example of an input is supplied. The output is the propositions in 
numerical form with the names of each of the propositions after a line 
containing one 0 only. This will also give a similar output in the console 
but is not written to be used with Davis_Putnam.java. An example of this 
is supplied.

Graph file formatting: First line is the number of vertices in the graph
Each next line is two vertices separated by a space to represent an edge
The vertices are represented by one uppercase letter
No space at the end of a line. 

Davis_Putnam.java
This can be ran with the following commands:
javac Davis_Putnam.java
java Davis_Putnam

The input is given in the text file “Proposition.txt” (the output of 
HPFrontEnd.java) and the output is “DavisPutnamOutput.txt”. An example of 
both is supplied. The output will give a list of the numerical proposition 
and the truth value separated by a space on each line. Then a line with 
one 0 only and the lines afterwards is copied from the input to the 
output unchanged.

HPBackEnd.java
This can be ran with the following commands:
javac HPBackEnd.java
java HPBackEnd

The input is given in the text file “DavisPutnamOutput.txt” (the output of 
Davis_Putnam.java) and the output is “Output.txt”. An example of both is 
supplied. The output is a list of edges separated by commas and spaces that 
give a satisfying Hamiltonian path.
