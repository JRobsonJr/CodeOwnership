package app;

import java.io.IOException;
import java.util.Scanner;

import analysis.AnalysisType;
import org.eclipse.jgit.api.errors.GitAPIException;

import codeOwnership.CodeOwnership;
import pair.PairRepository;

import static util.Util.LS;

public class App {

    private static CodeOwnership co;
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		String repoPath = inputRepositoryPath();
		AnalysisType analysisType = chooseAnalysisType();

		co = new CodeOwnership(analysisType, repoPath);
		System.out.println(co.getPairRepository().toString());

		// printAllStudentsNames();
		// TODO co.printAStudentPairs(pairs);

		in.close();
	}

	private static String inputRepositoryPath() {
		System.out.println("Enter the path to your repository:");
		return in.nextLine();
	}

	private static AnalysisType chooseAnalysisType() {
		System.out.println("Choose the type of analysis:" + LS + "1) Creation" + LS + "2) LOC" + LS + "3) Co-authorship");
		int input = Integer.parseInt(in.nextLine());

		for (AnalysisType type : AnalysisType.values()) {
			if (type.getIndex() == input) {
				System.out.println("Analysis by " + type + " was chosen." + LS);
				return type;
			}
		}

		System.out.println("Your input is invalid. Please, try again." + LS);

		return chooseAnalysisType();
	}

	private static void printAllStudentsNames() throws GitAPIException, IOException {
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + LS);
	}

	private static void printAStudentPairs(PairRepository pairs) {
		String student = "";

		while (true) {
			System.out.println("Which student contribution would you like to see?");
			System.out.println(co.listStudents());
			System.out.println("Type student position in the array to see its contributions or press F to finish");
			student = in.nextLine();

			if (student.equalsIgnoreCase("F"))
				break;

			int studentIndex = Integer.parseInt(student) - 1;
			String studentAux = co.getArrayOfStudents()[studentIndex].getName();

			try {
				System.out.println(pairs.getPairsByStudentName(studentAux));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Try another student.");
			}
		}
	}

}
