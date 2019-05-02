import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class LanguageModel {

	// The length of the moving window
	private int windowLength; 
	// The map where we manage the (window, LinkedList) mappings 
	private HashMap<String, LinkedList<CharProb>> probabilities;

	Random rand;

	/**
	 * Creates a new language model, using the given window length.
	 * @param windowLength
	 */
	public LanguageModel(int windowLength) {
		this.rand = new Random();
		this.windowLength = windowLength;
		probabilities = new HashMap<String, LinkedList<CharProb>>();
	}

	public LanguageModel(int windowLength, int seed) {
		this.rand = new Random(seed);
		this.windowLength = windowLength;
		probabilities = new HashMap<String, LinkedList<CharProb>>();
	}



	/**
	 * Builds a language model from the text in standard input (the corpus).
	 */
	public void train(String text) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(text));

			String window = "";
			char c;

			for (int i = 0; i < windowLength; i++) {
				window += (char)reader.read();
			}
			int cInt;
			while ((cInt = reader.read()) != -1) {

				c = (char) cInt;
				LinkedList<CharProb> probs = probabilities.get(window);

				if (probs == null) {
					probs = new LinkedList<>();
					probabilities.put(window, probs);
				}

				calculateCounts(probs, c);

				window += c;
				window = window.substring(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		for (LinkedList<CharProb> probs : probabilities.values())
			calculateProbabilities(probs);
	}
		
	// If the given character is found in the given list, increments its count;
    // Otherwise, constructs a new CharProb object and adds it to the given list.
	private void calculateCounts(LinkedList<CharProb> probs, char c) {
		int index = Tools.indexOf(probs, c);
		if (index != -1) {
			probs.get(index).count++;
		}
		else {
			probs.add(new CharProb(c));
		}
	}
	
	// Calculates and sets the probabilities (p and cp fields) of all the
	// characters in the given list.
	private void calculateProbabilities(LinkedList<CharProb> probs) {				
		Tools.calculateProbabilities(probs);
	}	

	/**
	 * Returns a string representing the probabilities map.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : probabilities.keySet()) {
			LinkedList<CharProb> keyProbs = probabilities.get(key);
			str.append(key + ": " + keyProbs + "\n");
		}
		return str.toString();
	}


	public String generate(String initialText, int textLength) {
		if (windowLength > initialText.length()) {
			return initialText;
		}
		String text = initialText;
		String window = text.substring(text.length() - windowLength);
		while (text.length() < textLength)
		{
			LinkedList<CharProb> probs = probabilities.get(window);

			if (probs == null)
			{
				return text;
			}

			char c = getRandomChar(probs);
			text += c;
			window = window.substring(1) + c;
		}
		return text;
	}

	public char getRandomChar(LinkedList<CharProb> probs) {
		double r = rand.nextDouble();

		for (int i = 0; i < probs.size(); i++) {
			if (probs.get(i).cp > r)
				return probs.get(i).chr;
		}

		return probs.getLast().chr;
	}
	
	// Learns the text that comes from standard input,
	// using the window length given in args[0],
	// and prints the resulting map. 
	public static void main(String[] args) {
	   int windowLength = 10;

	   String initialText = "heaven and earth";
	   int textLength = 10000;

	   boolean random = true;

		LanguageModel lm;

		if (random) {
		   // Constructs a learning model
		   lm = new LanguageModel(windowLength);
	    }
	   else {
	   	lm = new LanguageModel(windowLength, 20);
	   }
	   // Builds the language model
	   lm.train("Genesis.txt");
	   // Prints the resulting map
	   System.out.println("\n\n" + lm.generate(initialText, textLength));
	}
}