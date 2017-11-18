package analise;


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
import student.StudentServer;

public class AnaliseCriacao implements Analise {

	public AnaliseCriacao() {
	}

	public void makePairs(GitRepository git, PairRepository pairs, StudentServer students) throws Exception {
		Repository repo = git.getRepository();	
		RevWalk walk = git.getRevWalk();
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();
		
		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				AddArtifactsFromFirtsCommit(repo, pairs, walk, commit, students);
				this.deleteRemovedArtifacts(git,pairs);
				return;
			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
					if (isNewArtifact(entry) && isJavaClass(entry.getNewPath())) {
						Artifact artifact = new Artifact(entry.getNewPath());
						PairStudentArtifact auxPair = new PairStudentArtifact(
								students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
						pairs.addPair(auxPair);
					}
				}
			}
		}

	}



	/**
	 * Returns whether the change is the type DELETE
	 */
	private boolean isRemovedArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.DELETE;
	}

	/**
	 * Fix up for the first commit case
	 * 
	 */
	private void AddArtifactsFromFirtsCommit(Repository repo, PairRepository pairs, RevWalk walk, RevCommit commit,
			StudentServer students)
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
				PairStudentArtifact auxPair = new PairStudentArtifact(
						students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
				pairs.addPair(auxPair);
			}
		}

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

	private boolean isFirstCommit(RevCommit commit) {
		RevCommit testing = null;
		try {
			testing = commit.getParent(0);
		} catch (Exception e) {
		}

		return testing == null;

	};

	public void deleteRemovedArtifacts(GitRepository git, PairRepository pairs) throws Exception {
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				return;
			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {

					if (isRemovedArtifact(entry) && isJavaClass(entry.getOldPath())) {
						Artifact artifact = new Artifact(entry.getOldPath());
						pairs.removePair(artifact);
					}
				}
			}
		}
	}

	/**
	 * Returns whether the change is the type ADD(created for the first time)
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

}
