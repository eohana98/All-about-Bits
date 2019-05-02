
import java.util.LinkedList;

public class Tools {
	
	public static void main(String []args) {
		// test1();
		// test2();
		// test3();
		// test4();
		 test5();
	}

	// Creates a linked list, adds a few CharProb objects,
	// and tests the indexOf method.
	private static void test1() {
		LinkedList<CharProb> probs = new LinkedList<CharProb>();
		probs.add(new CharProb('i'));
		probs.add(new CharProb('d'));
		probs.add(new CharProb('c'));
		System.out.println(indexOf(probs,'d'));	// Should print 1
		System.out.println(indexOf(probs,'i'));	// Should print 0
		System.out.println(indexOf(probs,'u'));	// Should print -1
	}
			
	// Creates a linked list, adds a few CharProb objects,
	// and tests the toString method.
	private static void test2() {
		LinkedList<CharProb> probs = new LinkedList<CharProb>();
		probs.add(new CharProb('i'));
		probs.add(new CharProb('d'));
		probs.add(new CharProb('c'));
        System.out.println(toString(probs));
        // The output should look like this:
        // ((i 1)(d 1)(c 1))
	}
	
	// Tests the creation of a character probabilities list from a given string.
	// Does not handle the p and cp fields yet.
	private static void test3() {
		System.out.println(toString(buildList("committee ")));
		// The output should look like this:
		// ((c 1)(o 1)(m 2)(i 1)(t 2)(e 2)(  1))
	}
	
	// Tests the probability calculations.
	private static void test4() {
		LinkedList<CharProb> probs = buildList("committee ");
		System.out.println("Before: " + toString(probs));
		calculateProbabilities(probs);
		// The following printout will make sense only after you change the 
		// toString method of the CharProb class to also list the two
		// probability fields.
		System.out.println("After: " + toString(probs));
	}
	
	// Tests the calculation of the probabilities p and cp.
	private static void test5() {
		LinkedList<CharProb> probs = buildList("committee ");
		calculateProbabilities(probs);
		System.out.println(getRandomChar(probs));
		// Write below code that generates characters at random from a given
		// list. Repeat this experiment many times, and verify that the characters
		// are generated according to the probabilities represented in the given list.
	}

	// Builds a character probabilities list from the given string.
	// For each character, sets the chr and count fields, without
	// dealing with the fields p and cp (they remain zero for now).
	public static LinkedList<CharProb> buildList(String str) {
		LinkedList<CharProb> probs = new LinkedList<>();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			int index = indexOf(probs, c);
			if (index == -1) {
				probs.add(new CharProb(c));
			}
			else {
				probs.get(index).count++;
			}
		}
		return probs;
	}
	
	// If the given character exists in the given list, returns the index of the 
	// element where the character was found. Otherwise returns -1. 
	public static int indexOf(LinkedList<CharProb> probs, char c) {
		for (int i = 0; i < probs.size(); i++) {
			if (probs.get(i).chr == c)
				return i;
		}
		return -1;
	}
		
	// Returns a textual representation of the given probabilities list, using the 
	// format ((c n),(c n),...,(c n)) where c is a character and n is a count value. 
	public static String toString(LinkedList<CharProb> probs) {
		StringBuilder listRep = new StringBuilder("(");
		for (CharProb character : probs) {
			listRep.append(character);
		}
//		String result = listRep.toString();
		return listRep.toString() + ")";
	}
	
	// Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given probabilities list.
	public static void calculateProbabilities(LinkedList<CharProb> probs) {

		int size = 0;
		for (int i = 0; i < probs.size(); i++) {
			size += probs.get(i).count;
		}

		for (int i = 0; i < probs.size(); i++) {
			probs.get(i).p = Math.round(100.0 * (double) probs.get(i).count / size) / 100.0;
		}

		if (size >= 1) {
			probs.get(0).cp = probs.get(0).p;
		}

		for(int i = 1; i < probs.size(); i++) {
			probs.get(i).cp = Math.round(100.0 * (probs.get(i).p + probs.get(i-1).cp)) / 100.0;
		}
	}	
	
	// Returns a character from the given list, according to its probability. 
	public static char getRandomChar(LinkedList<CharProb> probs) {
		double r = Math.random();

		for (int i = 0; i < probs.size(); i++) {
			if (probs.get(i).cp > r)
				return probs.get(i).chr;
		}
		return probs.getLast().chr;
	}
}
