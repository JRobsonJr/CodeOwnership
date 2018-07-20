package app;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analysis.AbstractAnalysis;
import analysis.CreationAnalysis;
import analysis.LOCAnalysis;
import analysis.LOCPercentAnalysis;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import util.Util;

import static util.Util.LS;

public class App {

    private static CodeOwnership co;
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		String repositoryPath = inputRepositoryPath();
		AbstractAnalysis analysis = chooseAnalysisType();
		
		co = new CodeOwnership(analysis, repositoryPath + "/.git");

		printAllStudentsNames();
		registerStudentsByJsonFile();

		Repository repo = new FileRepository(repositoryPath + "/.git");

		PairRepository pairs = new PairRepository();
		co.makePairs(repo, pairs, repositoryPath);
		co.determineArtifactExpertises(repositoryPath, pairs);
		System.out.println(pairs.toString());

		printAStudentPairs(pairs);

		in.close();
	}

	private static String inputRepositoryPath() {
		System.out.println("Enter the path to your repository:");
		return in.nextLine();
	}

	private static AbstractAnalysis chooseAnalysisType() {
		System.out.println("Choose the type of analysis:" + LS +
                "1) Creation" + LS + "2) LOC" + LS + "3) Co-authorship");
		int analysisType = Integer.parseInt(in.nextLine());

		if (analysisType == 1) {
			System.out.println("Analysis by creation was chosen" + LS);
			return new CreationAnalysis();
		} else if (analysisType == 2) {
			System.out.println("Analysis by LOC was chosen" + LS);
			return new LOCAnalysis();
		} else if (analysisType == 3) {
			System.out.println("Analysis by co-authorship was chosen" + LS);
			return new LOCPercentAnalysis();
		} else {
		    System.out.println("Your input is invalid. Please, try again." + LS);
		    return chooseAnalysisType();
        }
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
				System.out.println(pairs.getPairsByStudentName(studentAux.toString()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Try another student.");
			}
		}
	}

	private static void registerStudentsByJsonFile() {
		System.out.println("Enter the path to the .json file containing the aliases:");
		String jsonPath = in.nextLine();

		co.registerAllStudents(Util.getStudentsFromJson(jsonPath));

		System.out.println("Registered students:" + LS + co.getStudentRepository());
	}

	private static void printAllStudentsNames() throws NoHeadException, GitAPIException, IOException {
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + LS);
	}

}
