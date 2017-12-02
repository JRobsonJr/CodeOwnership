package codeOwnership;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import analise.Analysis;
import competencia.Competencia;
import git.GitRepository;
import student.StudentServer;

public class CodeOwnership {

	private Analysis analise;
	private StudentServer students;
	private Competencia competencia;
	private GitRepository git;

	public CodeOwnership(Analysis analise, String repoPath) throws IOException {
		this.students = new StudentServer();
		this.analise = analise;
		this.git = new GitRepository(repoPath);
		this.competencia = new Competencia();
	}

	public void makePairs(Repository repo, PairRepository pairs, String path) throws Exception {
		this.analise.makePairs(git, pairs, students);
	}

	public void registerAllStudents() throws GitAPIException, IOException {
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