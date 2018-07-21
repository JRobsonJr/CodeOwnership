package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

import artifact.Artifact;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;

public class LOCAnalysis extends AbstractAnalysis {

	private static double DEFAULT_OWNERSHIP_VALUE = 100.0;

	@Override
	public List<PairStudentArtifact> makePairs(GitRepository git, StudentRepository students) throws IOException, GitAPIException {
		this.students = students;
		List<String> paths = git.listRepositoryContents();
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (String className : paths) {
			Student greater = this.getGreatestContributor(git.getRepository(), className);
			Artifact artifact = new Artifact(className);
			PairStudentArtifact newPair = new PairStudentArtifact(greater, artifact, DEFAULT_OWNERSHIP_VALUE);
			pairs.add(newPair);
		}

		return pairs;
	}

	private Student getGreatestContributor(Repository repository, String pathFile)
			throws RevisionSyntaxException, IOException, GitAPIException {
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

}
