package analysis;

import java.io.IOException;
import java.util.*;

import artifact.Artifact;
import artifact.ArtifactRepository;
import pair.PairStudentArtifact;
import git.GitUtil;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import git.GitRepository;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import student.Student;
import student.StudentRepository;
import util.Util;

public abstract class AbstractAnalysis {

	protected StudentRepository studentRepository;
	protected ArtifactRepository artifactRepository;

	public AbstractAnalysis(StudentRepository studentRepository, ArtifactRepository artifactRepository) {
		this.studentRepository = studentRepository;
		this.artifactRepository = artifactRepository;
	}

	public abstract List<PairStudentArtifact> makePairs(GitRepository git) throws GitAPIException, IOException;

	protected List<PairStudentArtifact> deleteRemovedArtifacts(GitRepository git, List<PairStudentArtifact> pairs) throws IOException, GitAPIException {
		DiffFormatter diffFormatter = git.getDiffFormatter();
		Iterator<RevCommit> iterator = git.getCommits().iterator();
		RevCommit currentCommit;

		while (!GitUtil.isFirstCommit(currentCommit = iterator.next())) {
			for (DiffEntry entry : diffFormatter.scan(currentCommit.getParent(0), currentCommit)) {
				String oldPath = entry.getOldPath();

				if (GitUtil.isRemovedEntry(entry) && Util.isJavaClass(oldPath)) {
					// TODO Artifact artifact = new Artifact(oldPath);
					// pairs.removeIf(pair -> pair.compareArtifacts(artifact));
				}
			}
		}

		return pairs;
	}

	protected BlameResult getBlameResult(Repository repository, String pathFile)
			throws RevisionSyntaxException, IOException, GitAPIException {
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
	protected Map<Student, Integer> getFrequency(int lines, BlameResult blameResult) {
		Map<Student, Integer> frequency = new HashMap<Student, Integer>();

		for (int i = 0; i < lines; i++) {
			RevCommit commit = blameResult.getSourceCommit(i);
			if (isValidLine(blameResult.getResultContents().getString(i))) {
				Student newStudent = studentRepository.getStudent(commit.getAuthorIdent().getName());
				if(!frequency.containsKey(newStudent)) frequency.put(newStudent, 0);
				frequency.put(newStudent, frequency.get(newStudent) + 1);
			}
		}

		return frequency;
	}

	protected boolean isValidLine(String line) {
		return !(line.contains("import") && (line.trim().equals("}") || line.trim().equals("{"))
				&& line.trim().equals(""));
	}
}
