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

public class CreationAnalysis implements Analysis {

	public void makePairs(GitRepository git, PairRepository pairs, StudentRepository students) throws Exception {
		Repository repo = git.getRepository();
		RevWalk walk = git.getRevWalk();
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				addArtifactsFromFirstCommit(repo, pairs, walk, commit, students);
				this.deleteRemovedArtifacts(git, pairs);
				return;
			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
					if (this.isNewArtifact(entry) && Util.isJavaClass(entry.getNewPath())) {
						Artifact artifact = new Artifact(entry.getNewPath());
						String studentName = commit.getAuthorIdent().getName();
						Student student = students.getStudent(studentName);
						PairStudentArtifact auxPair = new PairStudentArtifact(student, artifact);

						// TODO: mudar isso aqui para nome ao inves do email vai ter que faze um logica
						// de percorrer os nomes
						pairs.addPair(auxPair);
					}
				}
			}
		}
	}

	/**
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
			if (Util.isJavaClass(treeWalk.getPathString())) {
				// como eh o primeiro commit nem precisa verificar se eh ADD.
				Artifact artifact = new Artifact(treeWalk.getPathString());
				String studentName = commit.getAuthorIdent().getName();
				Student student = students.getStudent(studentName);
				PairStudentArtifact auxPair = new PairStudentArtifact(student, artifact);
				pairs.addPair(auxPair);
			}
		}
	}
	
	private boolean isFirstCommit(RevCommit commit) {
		RevCommit testing = null;
		
		try {
			testing = commit.getParent(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testing == null;

	};

	private void deleteRemovedArtifacts(GitRepository git, PairRepository pairs) throws Exception {
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				return;
			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
					if (this.isRemovedArtifact(entry) && Util.isJavaClass(entry.getOldPath())) {
						Artifact artifact = new Artifact(entry.getOldPath());
						pairs.removePair(artifact);
					}
				}
			}
		}
	}
	
	/**
	 * Returns whether the artifact has been just added.
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

	/**
	 * Returns whether the artifact has been removed.
	 */
	private boolean isRemovedArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.DELETE;
	}
}
