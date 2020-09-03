package app;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import analysis.AnalysisType;
import artifact.Artifact;
import codeOwnership.CodeOwnership;
import exception.StudentNotFoundException;
import extractor.Extractor;
import expertise.Expertise;
import git.GitRepository;
import pair.PairStudentArtifact;
import student.Student;
import student.StudentExpertise;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.javaparser.utils.Pair;

import util.Util;

import static util.Util.LS;

public class App {

	private static CodeOwnership co;
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		String repoPath;
		AnalysisType analysisType;
		if (args.length == 2) {
			repoPath = args[0];
			analysisType = chooseAnalysisType(Integer.parseInt(args[1]));
		} else {
			repoPath = inputRepositoryPath();
			analysisType = chooseAnalysisType();
		}
		co = new CodeOwnership(analysisType, repoPath);
		printAllStudentsNames();
		
		System.out.println(generateExpertiseData(co));
		
		generateTSV();
		displayStudentInformation();
		
		in.close();
	}

	private static void printPairs(CodeOwnership co2) {
		List<PairStudentArtifact> aa = co.getPairRepository().getPairs();
		
		for(PairStudentArtifact pair : aa) {
			System.out.println(pair.toString());
			System.out.println();
		}
		
	}

	private static String inputRepositoryPath() {
		System.out.print("Enter the path to your repository: ");
		return in.nextLine();
	}

	private static void printArtifacts(CodeOwnership co) {
		List<Artifact> list = co.getArtifactRepository().getArtifacts();
		for (Artifact artifact : list) {
			System.out.println(artifact.toString());
		}

	}

	private static AnalysisType chooseAnalysisType() {
		System.out
				.println("Choose the type of analysis:" + LS + "1) Creation" + LS + "2) LOC" + LS + "3) Co-authorship");
		int input = Integer.parseInt(in.nextLine());

		AnalysisType chooseAnalysisType = chooseAnalysisType(input);

		if (chooseAnalysisType != null) {
			return chooseAnalysisType;
		}
		System.out.println("Your input is invalid. Please, try again." + LS);

		return chooseAnalysisType();
	}

	private static AnalysisType chooseAnalysisType(int input) {
		for (AnalysisType type : AnalysisType.values()) {
			if (type.getIndex() == input) {
				System.out.println("Analysis by " + type + " was chosen." + LS);
				return type;
			}
		}
		return null;
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

	private static void generateTSV() {
		if (Util.generateTSV(co.getPairRepository().getPairs())) {
			System.out.println("TSV successfully generated! Check outputs/analysis-result.tsv ;)" + LS);
		} else {
			System.out.println("There occurred an error during the generation of this project's TSV. :(" + LS);
		}
	}

	private static void printAllStudentsNames() throws GitAPIException, IOException {
		System.out.println("Students names in the system:");
		System.out.println(co.listAllStudentsNames().toString() + LS);
	}

	// Alterandoooo --------------------------------------

	private static String generateExpertiseData(CodeOwnership co) {
		List<Student> students = co.getAllStudents();
		String resp = "";
		List<PairStudentArtifact> pairs = co.getPairRepository().getPairs();
		int[] totalOfClassesOfEachExpertise = co.totalOfClassOfEachExpertise();

		for (Student student : students) {
			double[] counterOfExpertises = new double[8];

			for (PairStudentArtifact pairStudentArtifact : pairs) {

				Student studentOfTheCurrentPair = pairStudentArtifact.getStudent();
				Artifact artifact = pairStudentArtifact.getArtifact();

				if (student.equals(studentOfTheCurrentPair)) {
					countExpertise(counterOfExpertises, artifact, pairStudentArtifact.getOwnershipPercentage());
				}
			}

			StudentExpertise studentExpertise = new StudentExpertise(student, counterOfExpertises,
					totalOfClassesOfEachExpertise);
			resp += studentExpertise.toString();
		}

		return resp;

	}

	private static void countExpertise(double[] counterOfExpertises, Artifact artifact, double ownershipPercentege) {
		if (artifact.getExpertise().contains(Expertise.INTERFACE))
			counterOfExpertises[0] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.INHERITANCE))
			counterOfExpertises[1] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.TESTS))
			counterOfExpertises[2] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.EXCEPTION_HIERARCHY))
			counterOfExpertises[3] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.EXCEPTIONS))
			counterOfExpertises[4] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.EXCEPTION_HANDLING))
			counterOfExpertises[5] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.PERSISTENCE))
			counterOfExpertises[6] += ownershipPercentege;
		if (artifact.getExpertise().contains(Expertise.ABSTRACT_CLASSES))
			counterOfExpertises[7] += ownershipPercentege;
	}

}
