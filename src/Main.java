import java.util.Collections;
import java.util.Vector;

public class Main {
	public static void main(String[] args) {
		int size = 5;
		Vector<Integer> a = new Vector<Integer>();
		Vector<Integer> b = new Vector<Integer>();
		for (int i = 0; i < size; i++) {
			a.add(i);
			b.add(size - i - 1);
		}
		Collections.sort(a);
		Collections.sort(b);
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.hashCode());
		System.out.println(b.hashCode());
	}
}