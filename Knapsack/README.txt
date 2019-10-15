This is two different programs to solve the knapsack problem.
Knapsack_ID.java is an iterative deepening method and
Knapsack_HC is a hill climbing method. Knapsack_ID will 
produce the same knapsack every time and will always find a 
knapsack if possible. Knapsack_HC will produce the best 
knapsack that can be found using 10 random starts. The output 
can vary between runs and has a possibility of failing.


These are java files and can be ran with the following commands:
javac Knapsack_ID.java
java Knapsack_ID

javac Knapsack_HC.java
java Knapsack_HC

The input is given by a text file in the same relative path as the
java file. The text file must be named "Knapsack" (no quotations).
The file must be formatted in the following way:
Target_Value Target_Weight
Object_Name Object_Value Object_Weight
Object_2_Name Object_2_Value Object_2_Weight
...

All numbers are doubles. Include spaces only in between input 
and do not have a space or new line after the last input at the end 
of the input file. The object names can be any string and this 
program will sort the objects alphabetically (uppercase matters). 
Two objects with the same name will produce an error statement. 
The program will still produce a knapsack with the target values and 
weights if possible but will not be sorted correctly if this happens. The 
values and weights can be any double variable. The ... represents the 
fact that any number of objects can be added. An example of the text 
file is included with this program.
