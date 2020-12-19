# Mathdoku
Mathdoku - Java program
A mathdoku puzzle has some similarities with a sudoku puzzle 

In short, you are given a square n x n grid. Within the grid, each
cell is identified as part of some grouping. Each grouping is a
connected set of 1 or more cells and each grouping is given an
operator and a result of the operator. Figure 1 shows a sample
puzzle.

The task is to put the integers 1 to n into the cells of the grid so
that:

- No integer appears twice in any row
- No integer appears twice in any column
- Applying the operator to all the values in one grouping

gives the result assigned to the operator.
Valid operators are + for addition, - for subtraction, * for
multiplication, / for division, and = to specify that a grouping (of
one cell) has a given value. When applying operators, all results
must be integers. When applying the – and / operators, the
larger integer is always the leftmost operand of the operation.

sample input:
aabb
cdee
cfeg
ffhg
a 3 –
b 1 –
c 2 /
d 2 =
e 5 +
f 9 *
g 2 /
h 4 =

Solution Overview:
The objective of the application is to solve the “Mathdoku” puzzle, also called a “Kenk” puzzle. Mathdoku resembles the sudoku puzzle which contains n x n grid. The only difference in mathdoku is it contains groups instead of sub-grids in sudoku.
Each group has certain constraints like a valid operator(‘+’, ’-’, ’*,’’/’, & ‘=’) and a value, when applying the given operator on all the boxes in the group results in the value provided.

Files and Structure:
There is a total of four java files in the application:
1.	Mathdoku.java: This class is the main class of the application, this class contains core methods like loadPuzzle, readyToSolve, solve, print, and choices.
2.	Groups.java: This class holds the group structure to the puzzle, This provides the operator, value and the boxes belong to the group.
3.	Box.java: This class is each cell in the puzzle, contains a value of the box and group name it belongs to.
4.	Constants.java: This is constants file to store the constants in the application.

Data structures:
ArrayLists are used to store the state of the puzzle. ArrayList<ArrayList<Box>>  where rows and columns are stored and Box contains the value and group name. 
HashMap is used to store group information. HashMap<String, Groups> where each group name is mapped to the constraints of the group and boxes that belong to the group.

Methods and Implementation:
1.	loadPuzzle: This method reads the input and stores the state of the puzzle to solve. The implementation of the method is as follows:
I.	Read the stream from the bufferredrader
II.	Get the length n of the puzzle from the first line of the input
III.	read the first n lines of the input and store the group names matrix in the ArrayList<ArrayList>
IV.	for each group name in the input, map group name to the cells.
V.	After n lines, read the constraints on the groups.
VI.	Validate constrains before storing the constraints.
2.	readyToSolve: This method checks whether a given puzzle is solvable or not by checking the constraints on the groups. Following are some of the checks on the constraints:
I.	If there are no constraints, then return false.
II.	 For addition-for given value is greater than (group-boxes)*(max-val) in puzzle return false. 
III.	For subtraction-more than two boxes for the group, return false.
IV.	For division-if the coefficient is higher than max val in the puzzle, return false
V.	For equalTo- the number of boxes should be one.
3.	Solve: This method is a recursive method to solve the puzzle.
I.	Iterate through the arraylist and get each box.
II.	For the given group key in the box, get the possible values of the box.
III.	For each possible set, check whether given value fits in the box by checking row, column and group constraints.
IV.	If fits, solve the next cell.
V.	If the decision fails, backtrack the changes and try the next value from the possible set.
VI.	Continue until the last box of the puzzle.
4.	Print: This method returns the puzzle, returns the cell values if puzzle is solved, else return group names of each cell.
5.	Choices: This method prints the number of decisions made by the application to solve the puzzle
6.	setPossibleValues: This method sets the possible values to the cell.
I.	For multiplication operator, get all multiples of the value.
II.	For the division operator, all divisors of the given value.
III.	For equalTo operator, box value should be equal to the result value.
IV.	Else set all values from 1 to puzzle length.
7.	checkRows: This method checks whether given value for the box is unique in the given row.
8.	checkCol: This method check whether given value for the box is unique in the given column.
9.	checkGrp: This method checks whether the given value satisfies the group constraints or not.
10.	isValidChar: checks whether the given character is a valid alphabet or not
11.	isValidOperators: check whether the given constraints for the group .are valid or not
12.	isNumeric: checks whether given numbers in the constraints are valid or not
13.	isStrBlank: check given string is null or blank else return true

Strategy and algorithm:
The key strategy to solve the problem is to group the boxes that belong to the same group. Following is the algorithm to solve the problem:
1.	identify the size ‘n’ of the puzzle by reading the length of the first line of the input.
2.	From 1 to n lines in the input, for each character, store char in the ArrayList<ArrayList> and map that cell to the hashmap. HashMap<char in input, cell in ArrayList>.
3.	Now we have a matrix of group names and a map with group names->cells.
4.	From n+1 to end of input, each line is parsed and store the constraints in the cells.
5.	For the degree of efficiency: From the given constraints on each group, identify the set of possible values for each box.
I.	For ‘*’ operator, the possible set would be all numbers ‘n’ from 1 to puzzle length where constraintVal % n == 0.
II.	For ‘/’ operator, the possible set would be - numbers n1,n2 from 1 to puzzle length where n1/n2 == contraintVal. 
III.	For ‘=’ operator, the number is same as constraintVal and fills the cell with constraint value.
6.	Once the possible sets are identified, for each cell in the puzzle, loop through the possible set and for each value, check:
I.	Value is unique in the row.
II.	Value is unique in the column.
III.	Value satisfies the group constraints.
7.	If the above conditions satisfy, set the value in the cell and proceed to solve the next cell. Else check for the next value in the possible set.
8.	If at any point, unable to solve the puzzle, backtrack to the previous state and try with the next possible value from the set.
9.	Continue until all the group constraints are solved and all cells in the puzzle are filled with the identified possible set.
