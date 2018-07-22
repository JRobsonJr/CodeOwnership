package app;

import java.io.IOException;
import java.util.Scanner;

import analysis.AnalysisType;
import codeOwnership.CodeOwnership;
import exception.StudentNotFoundException;

import org.eclipse.jgit.api.errors.GitAPIException;

import static util.Util.LS;

public class App {

    private static CodeOwnership co;
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		String repoPath = inputRepositoryPath();
		AnalysisType analysisType = chooseAnalysisType();
		co = new CodeOwnership(analysisType, repoPath);
		displayStudentInformation();

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

	private static void displayStudentInformation() {
		System.out.println("Which student contributions would you like to see?");
		System.out.println(co.listStudents());

		System.out.println("Choose a valid number (an invalid number will end this program):");
		int input = Integer.parseInt(in.nextLine());

		try {
			String studentName = co.getStudentByIndex(input - 1).getName();
			System.out.println(co.getStudentContributionInfo(studentName));
			displayStudentInformation();
		} catch (IndexOutOfBoundsException e) {
			System.out.println(LS + "See ya!");
		} catch (StudentNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Try another student.");
		}
	}

	private static void printAllStudentsNames() throws GitAPIException, IOException {
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + LS);
	}

}
