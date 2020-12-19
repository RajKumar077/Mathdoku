import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mathdoku {
	Box box = null;
	ArrayList<ArrayList<Box>> puzzle = null;
	int puzzleLength = 0;
	Map<Character, Groups> map = null;
	static int counterC = 0;

	public Mathdoku() {
		puzzle = new ArrayList<ArrayList<Box>>();
		map = new HashMap<Character, Groups>();
		counterC = 0;
		puzzleLength = 0;
	}

	
	/**
	 * this method reads the input and stores the state of puzzle to solve
	 * stores the puzzle in arraylist and hashmap
	 * @param stream
	 * @return
	 */
	public boolean loadPuzzle(BufferedReader stream) {
		if(stream == null) {
			return false;
		}
		String s;

		int counter = 1;

		try {
			if ((s = stream.readLine()) != null) {
				//get the length of the puzzle from the first line.
				puzzleLength = s.length();
				ArrayList<Box> row = new ArrayList<Box>();
				for (char c : s.toCharArray()) {
					if (!isValidChar(c)) {
						return false;
					}
					//create a box for each element
					box = new Box(c);
					//map the box to the hashmap
					if (map.containsKey(c)) {
						map.get(c).addBoxes(box);
					} else {
						map.put(c, new Groups(box));
					}
					row.add(box);
				}
				puzzle.add(row);
			}
		} catch (IOException e) {
			return false;
		}
		//System.out.println();
		try {
			//read the boxes of puzzle upto puzzle length
			while ((s = stream.readLine()) != null) {
				if (counter < puzzleLength) {
					ArrayList<Box> row = new ArrayList<Box>();
					for (char c : s.toCharArray()) {
						if (!isValidChar(c)) {
							return false;
						}
						box = new Box(c);
						if (map.containsKey(c)) {
							map.get(c).addBoxes(box);
						} else {

							map.put(c, new Groups(box));
						}
						row.add(box);
					}
					puzzle.add(row);
					counter++;

				} else {
					//after puzzlelength length starts the constrains on puzzle
					String[] st = s.split(" +");
					if (!isValidOperators(st)) {
						return false;
					}
					Groups g = map.get(st[0].toCharArray()[0]);
					if(g == null) {
						return false;
					}
					if(!isStrBlank(g.operator)) {
						return false;
					}
					g.value = Integer.parseInt(st[1]);
					g.operator = st[2];
				}
			}
		} catch (NumberFormatException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		try {
			stream.close();
		} catch (IOException e) {
			return false;
		}

		return true;
	};

	/**
	 * this method checks whether given puzzle is solvable by checking
	 * the given constraints 
	 * @return
	 */
	public boolean readyToSolve() {
		if(map.isEmpty()) {
			return false;
		}
		for (Map.Entry<Character, Groups> entry : map.entrySet()) {
			Groups g = entry.getValue();
			//if any group does not contain any operator, then return false
			if (isStrBlank(g.operator)) {
				return false;
			}
			if(g.operator.equals(Constants.addition)) {
				//for given sum-value is greater than (sum-group-boxes)*(max-val) in puzzle
				//return false
				if(g.value > g.listBoxes.size()*puzzleLength) {
					return false;
				}
			}
			else if (g.operator.equals(Constants.subtraction)) {
				//more than two boxes for the sub-group, return false
				if (g.listBoxes.size() != 2 && g.value >= puzzleLength) {
					return false;
				}
			}
			else if (g.operator.equals(Constants.division)) {
				//if the coefficient is greater than max val in puzzle, return false
				if (g.value > puzzleLength || g.listBoxes.size() != 2) {
					return false;
				}

			}
			else if (g.operator.equals(Constants.equalTo)) {
				//for equalto operator, box size is one
				if (g.value > puzzleLength || g.listBoxes.size() != 1) {
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * this method solves the given puzzle by calling recursive solvePuzzle method
	 * @return
	 */
	public boolean solve() {
		counterC = 0;
		if (readyToSolve()) {
			setPossibleValues();
			return solvePuzzle();
		}
		return false;
	}

	/**
	 * this is recursive method to solve the puzzle
	 * @return
	 */
	private boolean solvePuzzle() {
		
		for (int row = 0; row < puzzleLength; row++) {
			for (int col = 0; col < puzzleLength; col++) {
				int value = puzzle.get(row).get(col).value;
				String key = puzzle.get(row).get(col).key;
				//if box is unassigned, then proceed
				if (value == 0) {	
					//get the group of the box
					Set<Integer> possibleSet = map.get(key.toCharArray()[0]).possibleSet;
					//for the set of possible values for the box
					for (int val : possibleSet) {

						puzzle.get(row).get(col).value = val;
						//check whether the value fits the box
						if (checkRows(row) && checkCol(col) && checkGrp(key)) {
							
							//System.out.println(print());
							//if yes, fill the next box in puzzle
							if (solvePuzzle()) {
								return true;
							} else {

								puzzle.get(row).get(col).value = 0;
							}
						}

						else {
							puzzle.get(row).get(col).value = 0;
						}

					}
					counterC++;
					return false;

				}
			}
		}

		return true;
	}

	/**
	 * this method prints the solved puzzle
	 * @return
	 */
	public String print() {
		String s = "";
		for (ArrayList<Box> bx : puzzle) {
			for (Box b : bx) {	
				//if box is unsolved, then print group name
				s += b.value == 0 ? b.key : b.value;
			}
			s += "\n";
		}

		return s;
	}

	/**
	 * this method returns number of choices made to solve the puzzle
	 * @return
	 */
	public int choices() {
		return counterC;
	}
	
	/**
	 * for the given group and given box, 
	 * this method sets the possible value to fill the box
	 */
	private void setPossibleValues() {

		for (Map.Entry<Character, Groups> entry : map.entrySet()) {
			Groups g = entry.getValue();
			String operatorq = g.operator;
			//for multiplication operator, get all multiples of the value.
			if (operatorq.equals(Constants.multiplication)) {
				for (int i = 1; i <= puzzleLength; i++) {
					if (g.value % i == 0) {
						g.possibleSet.add(i);
					}
				}

			} else if (operatorq.equals(Constants.division)) {
				//for division operator, all divisors of the given value
				for (int i = g.value; i <= puzzleLength; i++) {
					for (int j = 1; j < i; j++) {
						if (i / j == g.value) {
							g.possibleSet.add(i);
							g.possibleSet.add(j);
						}
					}
				}
			} else if (operatorq.equals(Constants.equalTo)) {
				//box value should be equal to result value
				g.possibleSet.add(g.value);

				for (Box b : g.listBoxes) {
					b.value = g.value;
				}

			} else {
				for (int i = 1; i <= puzzleLength; i++) {
					g.possibleSet.add(i);

				}
			}
		}

	}

	/**
	 * this method checks whether given value is allowed in a row.
	 * @param row
	 * @return
	 */
	private boolean checkRows(int row) {
		Set<Integer> set = null;
		ArrayList<Box> bx = puzzle.get(row);
		set = new HashSet<Integer>();
		for (Box b : bx) {
			if (b.value != 0 && set.contains(b.value)) {
				return false;
			} else {
				set.add(b.value);
			}
		}
		return true;
	}

	/**
	 * this method checks whether given val allows in a column
	 * @param col
	 * @return
	 */
	private boolean checkCol(int col) {
		Set<Integer> set = null;
		set = new HashSet<Integer>();
		for (int j = 0; j < puzzleLength; j++) {
			int val1 = puzzle.get(j).get(col).value;
			if (val1 != 0 && set.contains(val1)) {
				return false;
			} else {
				set.add(val1);

			}
		}
		return true;

	}

	/**
	 * this method checks whether given val able to solve the 
	 * given groups constraints 
	 * @param key
	 * @return
	 */
	private boolean checkGrp(String key) {
		Groups g = map.get(key.toCharArray()[0]);

		if (g.operator.equals(Constants.addition)) {
			int sum = 0;
			for (Box bx : g.listBoxes) {
				if (bx.value == 0) {
					return true;
				} else {
					sum += bx.value;
				}
			}
			if (sum == g.value) {
				return true;
			}
		} else if (g.operator.equals(Constants.subtraction)) {
			ArrayList<Integer> lst = new ArrayList<Integer>();
			int diff = 0;
			for (Box bx : g.listBoxes) {
				if (bx.value == 0) {
					return true;
				} else {
					lst.add(bx.value);
				}
			}
			if (lst.get(0) > lst.get(1)) {
				diff = lst.get(0) - lst.get(1);
			} else {
				diff = lst.get(1) - lst.get(0);
			}
			if (diff == g.value) {
				return true;
			}
		} else if (g.operator.equals(Constants.multiplication)) {
			int mult = 1;
			for (Box bx : g.listBoxes) {
				if (bx.value == 0) {
					return true;
				} else {
					mult *= bx.value;
				}
			}
			if (mult == g.value) {
				return true;
			}
		} else if (g.operator.equals(Constants.division)) {
			ArrayList<Integer> lst = new ArrayList<Integer>();
			int div = 0;
			for (Box bx : g.listBoxes) {
				if (bx.value == 0) {
					return true;
				} else {
					lst.add(bx.value);
				}
			}
			if (lst.get(0) > lst.get(1)) {
				div = lst.get(0) / lst.get(1);
			} else {
				div = lst.get(1) / lst.get(0);
			}
			if (div == g.value) {
				return true;
			}
		} else if (g.operator.equals(Constants.equalTo)) {
			return g.listBoxes.get(0).value == g.value;
		}

		return false;
	}

	/**
	 * checks whether given char is valid alphabet or not 
	 * @param ch
	 * @return
	 */
	private boolean isValidChar(char ch) {
		return ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'));
	}

	/**
	 * check whether given constraints are valid or not
	 * @param st
	 * @return
	 */
	private boolean isValidOperators(String[] st) {
		if (st.length == 3 && st[0].length() == 1 && st[2].length() == 1) {

			boolean exp1 = isValidChar(st[0].toCharArray()[0]);
			boolean exp2 = isNumeric(st[1]);
			boolean exp3 = st[2].equals(Constants.addition) || 
					st[2].equals(Constants.subtraction) || 
					st[2].equals(Constants.multiplication) || 
					st[2].equals(Constants.division)
					|| st[2].equals(Constants.equalTo);
			if (exp1 && exp2 && exp3) {
				return true;
			}

		}
		return false;

	}

	/**
	 * checks given constraint number is valid or not.
	 * @param num
	 * @return
	 */
	private boolean isNumeric(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * check given string is null or blank else return true.
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isStrBlank(String str) {
		if (str != null && !str.isEmpty()) {
			return str.split(" +").length == 0;
		}

		return true;
	}
}
