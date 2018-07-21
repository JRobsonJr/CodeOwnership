package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
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
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;
import util.Util;

public class CreationAnalysis extends AbstractAnalysis {

	private static double DEFAULT_OWNERSHIP_VALUE = 100.0;

	@Override
	public List<PairStudentArtifact> makePairs(GitRepository git, StudentRepository students) throws GitAPIException, IOException {
		Repository repo = git.getRepository();
		RevWalk walk = git.getRevWalk();
		DiffFormatter diffFormatter = git.getDiffFormatter();

		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (RevCommit commit : git.getCommits()) {
			List<PairStudentArtifact> commitPairs;
			String studentName = commit.getAuthorIdent().getName();
			Student student = students.getStudent(studentName);

			if (this.isFirstCommit(commit)) {
				TreeWalk treeWalk = this.getTreeWalk(repo, walk, commit);
				commitPairs = this.getArtifactsFromFirstCommit(treeWalk, student);
			} else {
				commitPairs = this.getArtifactsFromCommit(diffFormatter, commit, student);
			}

			pairs.addAll(commitPairs);
		}

		return this.deleteRemovedArtifacts(git, pairs);
	}

	private TreeWalk getTreeWalk(Repository repo, RevWalk walk, RevCommit commit) throws IOException {
		ObjectReader reader = repo.newObjectReader();
		RevTree tree = walk.parseTree(commit);

		CanonicalTreeParser parser = new CanonicalTreeParser();
		parser.reset(reader, tree);

		TreeWalk treeWalk = new TreeWalk(reader);
		treeWalk.addTree(parser);
		treeWalk.setRecursive(true);

		return treeWalk;
	}

	private List<PairStudentArtifact> getArtifactsFromCommit(DiffFormatter diffFormatter, RevCommit commit, Student student) throws IOException {
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
			if (this.isNewArtifact(entry) && Util.isJavaClass(entry.getNewPath())) {
				PairStudentArtifact pair = this.createNewPair(entry.getNewPath(), student);
				pairs.add(pair);
			}
		}

		return pairs;
	}

	private PairStudentArtifact createNewPair(String artifactName, Student student) {
		Artifact artifact = new Artifact(artifactName);

		return new PairStudentArtifact(student, artifact, DEFAULT_OWNERSHIP_VALUE);
	}

	/*
	 * Fix up for the first commit case.
	 */
	private List<PairStudentArtifact> getArtifactsFromFirstCommit(TreeWalk treeWalk, Student student) throws IOException {
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		while (treeWalk.next()) {
			String pathString = treeWalk.getPathString();

			if (Util.isJavaClass(pathString)) {
				pairs.add(this.createNewPair(pathString, student));
			}
		}

		return pairs;
	}
	
	/**
	 * Returns whether the artifact has been just added.
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

}
