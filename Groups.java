import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Group class to to hold the all related boxes in the group and operation on the boxes
 * @author RajKumar
 *
 */
public class Groups {
	int value;
	String operator;
	ArrayList<Box> listBoxes = new ArrayList<Box>();
	Set<Integer> possibleSet = null;

	public Groups(Box box) {
		listBoxes.add(box);
		possibleSet = new HashSet<Integer>();
	}

	
	public void addBoxes(Box box) {
		listBoxes.add(box);
	}

}
