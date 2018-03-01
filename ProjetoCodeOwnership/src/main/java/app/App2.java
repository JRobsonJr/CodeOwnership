package app;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analysis.Analysis;
import analysis.CreationAnalysis;
import analysis.LOCAnalysis;
import analysis.LOCPercentAnalysis;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import util.Util;

public class App2 {

	private static CodeOwnership co;
	private static Analysis analysis;
	private static PairRepository pairs = new PairRepository();
	private static String repository;
	private static String analysisType;
	private static Scanner in = new Scanner(System.in);

	// Deixa aqui o caminho do repositorio do seu PC para facilitar na hora
	// de mudar(sem o .git):
	// "/home/mariana/Documents/Didatica_LP2/homemade-dynamite/";
	// C:\\Users\\DavidEduardo\\Documents\\CodeOwnershipAux\\GrupoRobson\\projetop2\\
	// C:\\Users\\DavidEduardo\\Documents\\CodeOwnershipAux\\Homemade-dynamite\\homemade-dynamite\\
	// C:\\Users\\DavidEduardo\\Documents\\CodeOwnership\\homemade-dynamite-names.txt
	// C:\\Users\\jrobs\\Documents\\Projects\\homemade-dynamite\\homemade-dynamite-names.json
	// /home/davidep/Área de Trabalho/dhomemade-dynamite/
	// /home/davidep/Área de
	// Trabalho/t3emp/code/CodeOwnership/homemade-dynamite-names.txt
	// /home/josersaj/Projects/CodeOwnership/homemade-dynamite-names.json
	// /home/josersaj/Projects/homemade-dynamite/

	public static void main(String[] args) throws Exception {

		takeRepositoryPath();

		chooseAnalysisType();
		
		co = new CodeOwnership(analysis, repository + ".git");

		printAllStudentsNames();

		registerStudentsByJsonFile();

		Repository repo = new FileRepository(repository + ".git");

		co.makePairs(repo, pairs, repository);
		
		co.determineArtifactSubjects(repository, pairs);

		System.out.println(pairs.toString());

		printAStundentPairs();

		in.close();
	}

	private static void takeRepositoryPath() {
		System.out.println("Enter your repository path:");
		repository = in.nextLine();
	}

	private static void printAStundentPairs() {
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

		System.out.println("Registered students:" + Util.LS + co.getStudentRepository());
	}

	private static void printAllStudentsNames() throws NoHeadException, GitAPIException, IOException {
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + Util.LS);
	}

	private static void chooseAnalysisType() {
		System.out.println("Choose the type of analysis: loc, creation or loc(%)");
		analysisType = in.nextLine();

		if (analysisType.equalsIgnoreCase("creation")) {
			System.out.println("Analysis by creation was chosen" + Util.LS);
			analysis = new CreationAnalysis();
		} else if (analysisType.equalsIgnoreCase("loc")) {
			System.out.println("Analysis by LOC was chosen" + Util.LS);
			analysis = new LOCAnalysis();
		} else {
			System.out.println("Analysis by LOC(%) was chosen" + Util.LS);
			analysis = new LOCPercentAnalysis();
		}
	}

}
