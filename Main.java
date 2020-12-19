import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args)  {
		FileReader fr = null;
		try {
			fr = new FileReader("C:\\Users\\RajKumar\\Documents\\MACS_winter\\Software Dev\\Assignments\\Assignment4\\test9x9_1.txt");
			//					C:\Users\RajKumar\Documents\MACS\Software Dev\Assignments\Assignment4
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		Mathdoku m = new Mathdoku();
		//System.out.println(m.print());
		//System.out.println(m.choices());
		//System.out.println(m.readyToSolve());
		
		//System.out.println(m.solve());
		
		m.loadPuzzle(br);
		//System.out.println(m.print());
		try {
			fr.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//m.print();
		//System.out.println(m.solve());
		System.out.println(m.readyToSolve());
		System.out.println(m.solve());
		System.out.println(m.print());
		System.out.println(m.choices());
	}
}
