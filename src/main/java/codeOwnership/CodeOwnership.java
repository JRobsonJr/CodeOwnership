package codeOwnership;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import analysis.AbstractAnalysis;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import git.GitRepository;
import student.StudentRepository;
import expertise.ExpertiseExtractor;
import util.Util;
import student.Student;

public class CodeOwnership {

	private AbstractAnalysis analysis;
	private StudentRepository studentRepository;
	private ExpertiseExtractor expertiseExtractor;
	private PairRepository pairRepository;
	private GitRepository git;

	public CodeOwnership(AbstractAnalysis analysis, String repoPath) throws IOException {
		this.git = new GitRepository(repoPath + "/.git");
		this.studentRepository = new StudentRepository();
		this.analysis = analysis;
		this.expertiseExtractor = new ExpertiseExtractor();
		this.pairRepository = new PairRepository();

		this.makePairs();
		this.determineArtifactExpertises(repoPath);
	}

	public PairRepository getPairRepository() {
		return pairRepository;
	}

	public void makePairs() {
		try {
			List<PairStudentArtifact> pairs = this.analysis.makePairs(git, studentRepository);
			this.pairRepository.setPairs(pairs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void determineArtifactExpertises(String repoPath) throws IOException {
		this.expertiseExtractor.listClassesAndExpertise(this.git, repoPath, pairRepository);
	}

	public StudentRepository getStudentRepository() {
		return this.studentRepository;
	}

	/**
	 * Lists all the students names in the system; it is used for writing the .txt
	 * file.
	 */
	public HashSet<String> listAllStudentsNames() throws NoHeadException, GitAPIException, IOException {
		return git.listAllStudentsNames();
	}

	public Student[] getArrayOfStudents() {
		Student[] arrayOfStudents = this.getStudentRepository().getStudents()
				.toArray(new Student[this.getStudentRepository().getStudents().size()]);
		
		return arrayOfStudents;
	}

	public void registerAllStudents(List<Student> students) {	
		this.studentRepository.setStudents(students);
	}

	public String listStudents() {
		Student[] students = this.getArrayOfStudents();
		String returnString = "";
		
		for (int i = 0; i < students.length; i++) {
			returnString += (i + 1) + ") " + students[i].getName() + Util.LS;
		}

		return returnString;

	}

}