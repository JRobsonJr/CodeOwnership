package codeOwnership;

import java.io.BufferedReader;
import java.io.FileDescriptor;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import analise.Analise;
import artifact.Artifact;
import competencia.Competencia;
import student.Student;
import student.StudentServer;

public class CodeOwnership {

	private Analise analise;
	StudentServer students;
	PairRepository pairs;
	Competencia competencia = new Competencia();
	private final String LS = System.lineSeparator();

	public CodeOwnership(Analise  analise) {
		this.students = new StudentServer();
		this.analise = analise;
	}

	public void makePairs(Repository repo, PairRepository pairs,String path) throws Exception {
		analise.makePairs(repo, pairs, students,path );

	}
	
	public void registerAllStudents(Git git) throws GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();
		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}
	
	public void determinateAtifactSubjects(String repositorio, PairRepository pairs) throws IOException {
		competencia.listClassesAndSubjects(repositorio, pairs);
	}
	
}