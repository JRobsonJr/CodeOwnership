package codeOwnership;

import java.io.IOException;
import java.util.HashSet;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;

import analysis.Analysis;
import git.GitRepository;
import student.StudentRepository;
import subject.Subject;
import student.Student;

public class CodeOwnership {

	private Analysis analise;
	private StudentRepository students;
	private Subject competencia;
	private GitRepository git;

	public CodeOwnership(Analysis analise, String repoPath) throws IOException {
		this.students = new StudentRepository();
		this.analise = analise;
		this.git = new GitRepository(repoPath);
		this.competencia = new Subject();
	}

	public void makePairs(Repository repo, PairRepository pairs, String path) throws Exception {
		this.analise.makePairs(git, pairs, students);
	}

	public void determinateAtifactSubjects(String repositorio, PairRepository pairs) throws IOException {
		competencia.listClassesAndSubjects(repositorio, pairs);
	}

	public StudentRepository getStudents() {
		return students;
	}
	
	/**
	 * This method will list all the students names in the system, this will be useful for making the txt file
	 */
	public HashSet<String> listAllStudentsNames() throws NoHeadException, GitAPIException, IOException {
		return git.listAllStudentsNames();
	}
	
	public Student[] arrayOfStudents() {
		Student[] arrayOfStudents = this.getStudents().getStudents().toArray(new Student[this.getStudents().getStudents().size()]);
		return arrayOfStudents;
	}
	
	public void registerAllStudents(String allStudentsNames) {
		String[] auxLineSeparator = allStudentsNames.split("\n");
		for (int i = 0; i < auxLineSeparator.length; i++) {
			students.addStudent(auxLineSeparator[i].split(","));
		}
	}

	public String listStudents() {
		Student [] students = this.arrayOfStudents();
		String returnString = "";
		for (int i = 0; i < students.length; i++) {
			returnString += i+1 + students[i].getName() + '\n';
		}
		
		return returnString;
	
	}
	
	
}