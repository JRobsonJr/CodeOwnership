package analysis;

import java.io.IOException;
import java.util.Iterator;

import artifact.Artifact;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import codeOwnership.PairRepository;
import git.GitRepository;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import student.StudentRepository;
import util.Util;

public abstract class AbstractAnalysis {

	public abstract void makePairs(GitRepository git, PairRepository pairs, StudentRepository students)
			throws NoHeadException, GitAPIException, IOException, Exception;

	protected boolean isFirstCommit(RevCommit commit) {
		boolean isFirstCommit = false;

		try {
			commit.getParent(0);
		} catch (Exception e) {
			isFirstCommit = true;
		}

		return isFirstCommit;
	}

	protected void deleteRemovedArtifacts(GitRepository git, PairRepository pairs) throws Exception {
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterator<RevCommit> iterator = git.getCommits().iterator();
		RevCommit currentCommit;

		while (!isFirstCommit(currentCommit = iterator.next())) {
			for (DiffEntry entry : diffFormatter.scan(currentCommit.getParent(0), currentCommit)) {
				String oldPath = entry.getOldPath();

				if (isRemovedArtifact(entry) && Util.isJavaClass(oldPath)) {
					Artifact artifact = new Artifact(oldPath);
					pairs.removePair(artifact);
				}
			}
		}
	}

	/**
	 * Returns whether the change is the type DELETE.
	 */
	protected boolean isRemovedArtifact(DiffEntry entry) {
		return entry.getChangeType() == DiffEntry.ChangeType.DELETE;
	}
}
