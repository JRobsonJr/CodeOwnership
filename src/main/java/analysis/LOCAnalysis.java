package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import artifact.Artifact;
import artifact.ArtifactRepository;
import pair.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

public class LOCAnalysis extends AbstractAnalysis {

	private static double DEFAULT_OWNERSHIP_VALUE = 100.0;

	public LOCAnalysis(StudentRepository studentRepository, ArtifactRepository artifactRepository) {
		super(studentRepository, artifactRepository);
	}

	@Override
	public List<PairStudentArtifact> makePairs(GitRepository git) throws IOException, GitAPIException {
		List<String> paths = git.listRepositoryJavaClasses();
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (String classPath : paths) {
			Student greatest = this.getGreatestContributor(git.getRepository(), classPath);
			Artifact artifact = this.artifactRepository.getArtifact(classPath);

			PairStudentArtifact newPair = new PairStudentArtifact(greatest, artifact, DEFAULT_OWNERSHIP_VALUE);
			pairs.add(newPair);
		}

		return pairs;
	}

	private Student getGreatestContributor(Repository repository, String filePath) throws RevisionSyntaxException, IOException, GitAPIException {
		BlameResult result = getBlameResult(repository, filePath);
		Map<Student, Integer> frequency = getFrequency(result.getResultContents().size(), result);

		Student greatestContributor = null;
		int max = 0;

		for (Student student : frequency.keySet()) {
			if (frequency.get(student) > max) {
				max = frequency.get(student);
				greatestContributor = student;
			}
		}

		return greatestContributor;
	}

}
