/**
 * Box class is for each cell in the puzzle,
 * it stores the value and the group name it belongs to
 * @author RajKumar
 *
 */
public class Box {
	int value;
	String key;
	
	public Box(char key) {
		this.value = 0;
		this.key = Character.toString(key);
	}
}
