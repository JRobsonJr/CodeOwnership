package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Util {
	public static final String LS = System.lineSeparator();

	public static boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

	public static String getNamesFromTxt(String txtPath) {
		String returnString = "";
		try {
			FileReader arq = new FileReader(txtPath);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine();
			while (linha != null) {
				returnString += linha + "\n";

				linha = lerArq.readLine(); // lê da segunda até a última linha
			}

			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		System.out.println();
		return returnString;
	}

}
