package expertise;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class ExpertiseExtractorUtil {

	public static Set<Expertise> extractExpertiseFromJavaClass(String path) {
		Set<Expertise> expertises = new HashSet<Expertise>();

		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(path));
			String line = buffRead.readLine();

			while (line != null) {
				Set<Expertise> lineExpertise = extractExpertiseFromLine(line);
				expertises.addAll(lineExpertise);

				line = buffRead.readLine();
			}

			buffRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return expertises;
	}

	private static Set<Expertise> extractExpertiseFromLine(String line) {
		Set<Expertise> expertises = new HashSet<Expertise>();
		String[] splitLine = line.split(" ");

		for (int i = 0; i < splitLine.length; i++) {
			String word = splitLine[i].trim();
			Expertise expertise = extractExpertiseFromWord(word);

			if (expertise != null) {
				if (expertise.equals(Expertise.INHERITANCE)) {
					String nextWord = splitLine[i + 1].trim();
					Expertise otherExpertise = extractExpertiseFromBigram(word, nextWord);
					if (otherExpertise != null) {
						expertises.add(otherExpertise);
					}
				}
				expertises.add(expertise);
			}
		}

		return expertises;
	}

	private static Expertise extractExpertiseFromBigram(String word, String nextWord) {
		return extractExpertiseFromWord(word + " " + nextWord);
	}

	private static Expertise extractExpertiseFromWord(String word) {
		String cleanWord = removeUnwantedCharacters(word);

		for (Expertise exp : Expertise.values()) {
			if (exp.containsKeyword(cleanWord)) {
				return exp;
			}
		}

		return null;
	}

	private static String removeUnwantedCharacters(String word) {
		CharSequence[] unwantedCharacters = { "{", "}", ";" };
		String cleanWord = word;

		for (CharSequence character : unwantedCharacters) {
			cleanWord = cleanWord.replace(character, "");
		}

		return cleanWord;
	}
}