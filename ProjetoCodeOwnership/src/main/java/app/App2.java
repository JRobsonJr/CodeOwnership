package app;

import java.util.Scanner;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analysis.Analysis;
import analysis.CreationAnalysis;
import analysis.LOCAnaylsis;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import util.Util;

public class App2 {

	private static CodeOwnership co;
	private static Analysis analysis;
	private static PairRepository pairs = new PairRepository();
	private static String repository;
	private static String analysisType;

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);

		System.out.println("Enter your repository path:");
		repository = in.nextLine();
		System.out.println("Choose the type of analysis: loc or creation");
		analysisType = in.nextLine();

		// Deixa aqui o caminho do repositorio do seu PC para facilitar na hora
		// de mudar(sem o .git):
		// "/home/mariana/Documents/Didatica_LP2/homemade-dynamite/";
		// C:\\Users\\DavidEduardo\\Documents\\CodeOwnershipAux\\GrupoRobson\\projetop2\\
		// C:\\Users\\DavidEduardo\\Documents\\CodeOwnershipAux\\Homemade-dynamite\\homemade-dynamite\\
		// C:\\Users\\DavidEduardo\\Documents\\CodeOwnership\\homemade-dynamite-names.txt
		// /home/davidep/Área de Trabalho/dhomemade-dynamite/
		// /home/davidep/Área de Trabalho/t3emp/code/CodeOwnership/homemade-dynamite-names.txt
		
		
		if (analysisType.equals("creation")) {
			System.out.println("Analysis by creation was chosen" + Util.LS);
			analysis = new CreationAnalysis();
		} else {
			System.out.println("Analysis by LOC was chosen" + Util.LS);
			analysis = new LOCAnaylsis();
		}

		co = new CodeOwnership(analysis, repository + ".git");
		
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + "\n");
		
		System.out.println("Enter the path to the txt file with the names");
		String txtPath = in.nextLine();

		co.registerAllStudents(Util.getNamesFromTxt(txtPath));

		System.out.println("Regitered students: \n" + co.getStudents().toString() + "\n");

		Repository repo = new FileRepository(repository + ".git");

		co.makePairs(repo, pairs, repository);
		co.determinateAtifactSubjects(repository, pairs);

		System.out.println(pairs.toString());
		
		String student = "";
		
		while (!student.equalsIgnoreCase("F")) {
			System.out.println("Which student contribution would you like to see?");
			System.out.println(co.listStudents());
			System.out.println("Type student position in the array to se it's contributions or press F to finish");
			student = in.nextLine();

			String studentAux = co.arrayOfStudents()[Integer.parseInt(student) - 1].getName();
			
			try {
				System.out.println(pairs.getPairsByStudentName(studentAux.toString()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("try another student");
			}

		}
	}

}
