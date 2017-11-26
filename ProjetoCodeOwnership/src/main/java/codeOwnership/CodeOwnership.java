package codeOwnership;

import java.io.BufferedReader;
import java.io.FileDescriptor;


import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;


import analise.Analise;

import competencia.Competencia;
import git.GitRepository;
import student.Student;
import student.StudentServer;

public class CodeOwnership {

	private Analise analise;
	StudentServer students;
	PairRepository pairs;
	Competencia competencia = new Competencia();
	GitRepository git;
	private final String LS = System.lineSeparator();

	public CodeOwnership(Analise  analise, String repoPath) throws IOException {
		this.students = new StudentServer();
		this.analise = analise;
		this.git = new GitRepository(repoPath);
	}

	public void makePairs(Repository repo, PairRepository pairs,String path) throws Exception {
		analise.makePairs(git, pairs, students);

	}
	
	public void registerAllStudents() throws GitAPIException, IOException {
		System.out.println(git.getDirectory());
		Iterable<RevCommit> commits = this.git.getCommits();
		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}
	
	public void determinateAtifactSubjects(String repositorio, PairRepository pairs) throws IOException {
		competencia.listClassesAndSubjects(repositorio, pairs);
	}
	
}