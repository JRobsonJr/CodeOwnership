package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import codeOwnership.PairRepository;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;
import util.Util;

public class LOCPercentAnalysis {

	private StudentRepository students;

	public void makePairs(GitRepository git, PairRepository pairs, StudentRepository students) throws Exception {
		this.students = students; // Isso faz mais sentido em um construtor.
		List<String> paths = listRepositoryContents(git);
		for (String className : paths) {
			Map<Student, Integer> contributions = new HashMap<Student, Integer>();
			Artifact artifact = new Artifact(className);
			Set<Student> contributors = contributions.keySet();
			
			// PairStudentArtifact auxPair = new PairStudentArtifact(greater, artifact, 100.0);
			// pairs.addPair(auxPair);
		}
	}

	private Student getGreatestContributor(Repository repository, String pathFile) throws RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException, GitAPIException {
		BlameResult result = getBlameResult(repository, pathFile);
		Map<Student, Integer> frequency = getFrequency(result.getResultContents().size(), result);
		Student greatestContributor = null;
		int max = 0;
		Set<Student> keys = frequency.keySet();

		for (Student student : keys) {
			if (frequency.get(student) > max) {
				max = frequency.get(student);
				greatestContributor = student;
			}
		}

		return greatestContributor;
	}
	
	private BlameResult getBlameResult(Repository repository, String pathFile) throws RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException, GitAPIException {
		BlameCommand blamed = new BlameCommand(repository);
		ObjectId commitID = repository.resolve("HEAD");
		blamed.setStartCommit(commitID);
		blamed.setFilePath(pathFile);
		BlameResult blameResult = blamed.call();

		return blameResult;
	}

	/**
	 * Exibe quem escreveu cada linha do arquivo especificado.
	 * 
	 * @param lines
	 * @param blameResult
	 */
	private Map<Student, Integer> getFrequency(int lines, BlameResult blameResult) {
		Map<Student, Integer> frequency = new HashMap<Student, Integer>();

		for (int i = 0; i < lines; i++) {
			RevCommit commit = blameResult.getSourceCommit(i);
			if (!blameResult.getResultContents().getString(i).trim().equalsIgnoreCase("")) {
				Student newStudent = students.getStudent(commit.getAuthorIdent().getName());
				if (frequency.containsKey(newStudent)) {
					frequency.put(newStudent, frequency.get(newStudent) + 1);
				} else {
					frequency.put(newStudent, 1);
				}
			}
		}

		return frequency;
	}

	private List<String> listRepositoryContents(GitRepository git)
			throws IOException, RevisionSyntaxException, GitAPIException {
		List<String> classes = new ArrayList<String>();
		Ref head = git.getRepository().getRef("HEAD");
		RevWalk walk = git.getRevWalk();
		RevCommit commit = walk.parseCommit(head.getObjectId());
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(git.getRepository());
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);

		while (treeWalk.next()) {
			if (Util.isJavaClass(treeWalk.getPathString())) {
				classes.add(treeWalk.getPathString());
			}
		}

		return classes;
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

	public void deleteRemovedArtifacts(GitRepository git, PairRepository pairs) throws Exception {
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterable<RevCommit> commits = git.getCommits();

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				return;
			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {

					if (isRemovedArtifact(entry) && Util.isJavaClass(entry.getOldPath())) {
						Artifact artifact = new Artifact(entry.getOldPath());
						pairs.removePair(artifact);
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

}
