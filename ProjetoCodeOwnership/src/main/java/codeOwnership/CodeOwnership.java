package codeOwnership;

import java.io.FileDescriptor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import student.Student;
import student.StudentServer;

public class CodeOwnership {

	StudentServer students;
	PairServer pairs;
	private final String LS = System.lineSeparator();

	public CodeOwnership() {
		this.students = new StudentServer();
	}

	public void registerAllStudents(Git git) throws GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();

		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}

	public void creatPairs(Repository repo, PairServer pairs) throws Exception {
		Git git = new Git(repo);
		RevWalk walk = new RevWalk(repo);
		Iterable<RevCommit> commits = git.log().all().call();
		DiffFormatter diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repo);

		for (RevCommit commit : commits) {

			RevCommit diffWith = null;

			// Acontece ArrayOutOfBounce
			try {
				diffWith = commit.getParent(0);
			} catch (Exception e) {

			}
			
			// Se fo primeiro commit:
			if (diffWith == null) {
				AddArtifactsFromFirtsCommit(repo, pairs, walk, commit);
				return;
			}

			for (DiffEntry entry : diffFormatter.scan(diffWith, commit)) {
				if (isNewArtifact(entry) && isJavaClass(entry.getNewPath())) {
					Artifact artifact = new Artifact(entry.getNewPath());
					
					PairStudentArtifact auxPair = new PairStudentArtifact(
							students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
					pairs.addPair(auxPair);

				}
			}

		}

	}

	
	/**
	 * Fix up for the first commit case
	 * 
	 */
	private void AddArtifactsFromFirtsCommit(Repository repo, PairServer pairs, RevWalk walk, RevCommit commit)
			throws MissingObjectException, IncorrectObjectTypeException, IOException, CorruptObjectException {

		ObjectReader reader = repo.newObjectReader();
		RevTree tree = walk.parseTree(commit);
		CanonicalTreeParser aParser = new CanonicalTreeParser();
		aParser.reset(reader, tree);
		TreeWalk tWalk = new TreeWalk(reader);
		tWalk.addTree(aParser);
		tWalk.setRecursive(true);
		while (tWalk.next()) {
			if (isJavaClass(tWalk.getPathString())) {
				// como eh o primeiro commit nem precisa verificar se eh ADD.
				Artifact artifact = new Artifact(tWalk.getPathString());
				PairStudentArtifact auxPair = new PairStudentArtifact(students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
				pairs.addPair(auxPair);
			}
		}

	}

	/**
	 * Returns whether the change is the type ADD(created for the first time)
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

	/**
	 * Returns whether the current artifact is a Java Class.
	 */
	private boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

	// DESCARTAR

	// public String aaa() {
	// return students.toString();
	// }
	// public void getCreatedArtifacts(Repository repo) throws Exception {
	// Git git = new Git(repo);
	// RevWalk walk = new RevWalk(repo);
	// List<Ref> branches = git.branchList().call();
	//
	// for (Ref branch : branches) {
	// String branchName = branch.getName();
	//
	// System.out.println("Commits of branch: " + branchName + LS + SEPARATOR);
	//
	// Iterable<RevCommit> commits = git.log().all().call();
	//
	// for (RevCommit commit : commits) {
	//
	// FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
	// DiffFormatter diffFormatter = new DiffFormatter(stdout);
	// diffFormatter.setRepository(repo);
	// RevCommit diffWith = null;
	//
	// try {
	// diffWith = commit.getParent(0);
	// } catch (Exception e) {
	// System.out.println("rs viu");
	// }
	//
	// // TODO: erro do primeiro commit
	//
	// boolean foundInThisBranch = foundInThisBranch(repo, walk, branchName,
	// commit);
	//
	// if (foundInThisBranch) {
	// System.out.println(commit.getName());
	// System.out.println(commit.getAuthorIdent().getName());
	// System.out.println(commit.getAuthorIdent().getEmailAddress());
	// System.out.println(commit.getAuthorIdent().getWhen());
	// System.out.println(commit.getFullMessage());
	// }
	//
	// try {
	// for (DiffEntry entry : diffFormatter.scan(diffWith, commit)) {
	// if (isNewArtifact(entry) && isJavaClass(entry.getNewPath())) {
	// System.out.println(entry.getChangeType() + ": " + entry.getNewPath());
	//
	// Student studentAux =
	// students.getStudent(commit.getAuthorIdent().getEmailAddress());
	// if (studentAux == null) {
	// students.addStudent(commit.getAuthorIdent());
	// }
	// studentAux = students.getStudent(commit.getAuthorIdent().getEmailAddress());
	// studentAux.addCreatedArtifact(entry.getNewPath());
	// }
	// }
	// } catch (Exception e) {
	//
	// }
	//
	// System.out.println(SEPARATOR);
	// }
	// }
	// }
	//
	//
	//
	// /**
	// * Returns whether the current commit is found in the current branch.
	// */
	// private boolean foundInThisBranch(Repository repo, RevWalk walk, String
	// branchName, RevCommit commit)
	// throws Exception {
	// boolean foundInThisBranch = false;
	//
	// RevCommit targetCommit = walk.parseCommit(repo.resolve(commit.getName()));
	//
	// for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
	// if (e.getKey().startsWith(Constants.R_HEADS)
	// && walk.isMergedInto(targetCommit,
	// walk.parseCommit(e.getValue().getObjectId()))) {
	// String foundInBranch = e.getValue().getName();
	// if (branchName.equals(foundInBranch)) {
	// foundInThisBranch = true;
	// break;
	// }
	// }
	// }
	//
	// return foundInThisBranch;
	// }

}