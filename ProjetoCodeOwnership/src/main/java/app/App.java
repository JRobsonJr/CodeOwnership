package app;

import javax.swing.JOptionPane;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analysis.Analysis;
import analysis.CreationAnalysis;
import analysis.LOCAnaylsis;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import student.Student;
import util.Util;

public class App {

	private static CodeOwnership co;
	private static Analysis analysis;
	private static PairRepository pairs = new PairRepository();
	private static String repository;
	private static String analysisType;
	private static String[] analysisOption = { "creation", "loc" };

	public static void main(String[] args) throws Exception {
		repository = JOptionPane.showInputDialog("Enter your repository path:");
		analysisType = (String) JOptionPane.showInputDialog(null, "Choose the type of analysis:", "Analysis type",
				JOptionPane.QUESTION_MESSAGE, null, analysisOption, analysisOption[0]);

		// Deixa aqui o caminho do repositorio do seu PC para facilitar na hora
		// de mudar(sem o .git):
		// "/home/mariana/Documents/Didatica_LP2/homemade-dynamite/";
		// "C:\\Users\\DavidEduardo\\Documents\\CodeOwnershipAux\\GrupoRobson\\projetop2\\"

		if (analysisType.equals("creation")) {
			System.out.println("Analysis by creation:" + Util.LS);
			analysis = new CreationAnalysis();
		} else {
			System.out.println("Analysis by LOC:" + Util.LS);
			analysis = new LOCAnaylsis();
		}

		co = new CodeOwnership(analysis, repository + ".git");

		// Lista os nomes para montar o txt
		System.out.println(co.listAllStudentsNames().toString());

		String txtPath = JOptionPane.showInputDialog("Enter your txt file path:");
		co.registerAllStudents(Util.getNamesFromTxt(txtPath));

		System.out.println(co.getStudents().toString());
		Repository repo = new FileRepository(repository + ".git");

		co.makePairs(repo, pairs, repository);
		co.determinateAtifactSubjects(repository, pairs);

		JOptionPane.showMessageDialog(null, pairs.toString());
		// System.out.println(pairs.toString());

		Student student = (Student) JOptionPane.showInputDialog(null,
				"What student contribution would you like to see?", "Student Info", JOptionPane.QUESTION_MESSAGE, null,
				co.arrayOfStudents(), co.getStudents().getStudents().get(0));

		JOptionPane.showMessageDialog(null,
				student.getName() + ":\n" + pairs.getPairsByStudentName(student.getName()).toString());
		// System.out.println(pairs.getPairsByStudentName(student.getName()).toString());
	}

}