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
import student.Student;
import student.StudentServer;

public class CodeOwnership {

	private Analise analise;
	StudentServer students;
	PairServer pairs;
	private final String LS = System.lineSeparator();

	public CodeOwnership(Analise  analise) {
		this.students = new StudentServer();
		this.analise = analise;
	}

	public void makePairs(Repository repo, PairServer pairs,String path) throws Exception {
		analise.makePairs(repo, pairs, students,path );

	}
	
	public void registerAllStudents(Git git) throws GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();
		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}


	// public void getDiffHead(Repository repository) throws
	// IncorrectObjectTypeException, IOException, GitAPIException {
	//
	// /* Mostra o que aconteceu entre 1 estado do repositorio e outro,
	// "HEAD~97^{tree}" 97 commits atrás da atual
	// * "HEAD^{tree}"
	// * mostra o que foi feito em cada arquivo: ADD/DELETE/MODIFY
	// *
	// * Entry: DiffEntry[MODIFY src/projeto/Projeto.java]
	// *
	// * */
	//
	// Git git = new Git(repository);
	// ObjectId oldHead = repository.resolve("HEAD^{tree}");
	// ObjectId head = repository.resolve("HEAD^^^^^{tree}");
	//
	// ObjectReader reader = repository.newObjectReader();
	//
	// CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	// oldTreeIter.reset(reader, oldHead);
	// CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
	// newTreeIter.reset(reader, head);
	//
	//
	//
	// List<DiffEntry> diffs =
	// git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
	// for (DiffEntry entry : diffs) {
	//
	// if(isRemovedArtifact(entry)) {
	// Artifact artifact = new Artifact(entry.getOldPath());
	// pairs.removePair(artifact);
	//
	//
	// }
	//
	//
	// }
	// }

}