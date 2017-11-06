package codeOwnership;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import student.StudentServer;

public class AnaliseCriacao implements Analise {

	StudentServer students;

	public AnaliseCriacao() {
		this.students = new StudentServer();
	
	}
	
	
	public void registerAllStudents(Git git) throws GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();

		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}

	public void criaPares(Repository repo, PairServer pairs) throws Exception {
		RevWalk walk = new RevWalk(repo);
		DiffFormatter diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repo);

		List<RevCommit> commits = getCommits(repo);

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				AddArtifactsFromFirtsCommit(repo, pairs, walk, commit);
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
	 * 
	 * @param repositorio
	 * @return lista com todos os commits
	 * @throws NoHeadException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	private List<RevCommit> getCommits(Repository repositorio) throws NoHeadException, GitAPIException, IOException {
		Git git = new Git(repositorio);
		Iterable<RevCommit> commits = git.log().all().call();
		List<RevCommit> listCommits = new ArrayList<RevCommit>();

		for (RevCommit commit : commits) {
			listCommits.add(commit);

		}
		return listCommits;

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
	

	

	public void deleteRemovedArtifacts(Repository repo, PairServer pairs) throws Exception {
		RevWalk walk = new RevWalk(repo);
		DiffFormatter diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repo);

		List<RevCommit> commits = getCommits(repo);

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

	/**
	 * Returns whether the change is the type DELETE
	 */
	private boolean isRemovedArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.DELETE;
	}

}
