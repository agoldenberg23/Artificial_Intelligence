This program takes a sentence, no punctuation, and a context free grammar (and lexicon). 
It prints out the most probable parse tree with its probability.

This is a java file and can be ran with the following commands:
javac CYK_Parser.java
java CYK_Parser
Enter input in one line, no punctuation.

This requires a grammar text file in the same relative path as this program. This is 
hardcoded to be named “grammar” in the program. The format for the grammar file is bellow 
and an example grammar is given. Input is given through the console by typing the 
sentence and hitting enter.

Grammar Format: The grammar file is first the context free grammar and then the lexicon 
separated by a blank line. Both parts begin with a number representing the number of 
lines that follow. 
For the CFG, “S -> Noun Verb [0.2]” is input as “S Noun Verb 0.2” on one line. 
For the lexicon, “Noun -> amy [0.1]” is input as “Noun amy 0.1” on one line.
