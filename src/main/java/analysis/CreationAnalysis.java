package analysis;

import java.io.IOException;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import codeOwnership.PairRepository;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;
import util.Util;

public class CreationAnalysis extends AbstractAnalysis {

	private static double DEFAULT_OWNERSHIP_VALUE = 100.0;

	@Override
	public void makePairs(GitRepository git, PairRepository pairs, StudentRepository students) throws Exception {
		Repository repo = git.getRepository();
		RevWalk walk = git.getRevWalk();
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				addArtifactsFromFirstCommit(repo, pairs, walk, commit, students);
				this.deleteRemovedArtifacts(git, pairs);
			} else {
				this.addArtifactsFromCommit(diffFormatter, commit, pairs, students);
			}
		}
	}

	private void addArtifactsFromCommit(DiffFormatter diffFormatter, RevCommit commit, PairRepository pairs, StudentRepository students) throws IOException {
		for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
			if (this.isNewArtifact(entry) && Util.isJavaClass(entry.getNewPath())) {
				this.addNewArtifact(entry.getNewPath(), pairs, students, commit);
			}
		}
	}

	private void addNewArtifact(String artifactName, PairRepository pairs, StudentRepository students, RevCommit commit) {
		Artifact artifact = new Artifact(artifactName);
		String studentName = commit.getAuthorIdent().getName();
		Student student = students.getStudent(studentName);
		PairStudentArtifact auxPair = new PairStudentArtifact(student, artifact, DEFAULT_OWNERSHIP_VALUE);
		pairs.addPair(auxPair);
	}

	/*
	 * Fix up for the first commit case.
	 */
	private void addArtifactsFromFirstCommit(Repository repo, PairRepository pairs, RevWalk walk, RevCommit commit,
			StudentRepository students)
			throws MissingObjectException, IncorrectObjectTypeException, IOException, CorruptObjectException {

		ObjectReader reader = repo.newObjectReader();
		RevTree tree = walk.parseTree(commit);
		CanonicalTreeParser parser = new CanonicalTreeParser();
		parser.reset(reader, tree);
		TreeWalk treeWalk = new TreeWalk(reader);
		treeWalk.addTree(parser);
		treeWalk.setRecursive(true);
		
		while (treeWalk.next()) {
			String pathString = treeWalk.getPathString();

			if (Util.isJavaClass(pathString)) {
				this.addNewArtifact(pathString, pairs, students, commit);
			}
		}
	}
	
	/**
	 * Returns whether the artifact has been just added.
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

}
