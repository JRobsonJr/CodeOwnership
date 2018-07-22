package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import artifact.ArtifactRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

import artifact.Artifact;
import pair.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;

public class LOCPercentAnalysis extends AbstractAnalysis {

	public LOCPercentAnalysis(StudentRepository studentRepository, ArtifactRepository artifactRepository) {
		super(studentRepository, artifactRepository);
	}

	@Override
	public List<PairStudentArtifact> makePairs(GitRepository git) throws IOException, GitAPIException {
		List<String> paths = git.listRepositoryJavaClasses();
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (String classPath : paths) {
			Map<Student, Double> contributions = this.getContributions(git.getRepository(), classPath);
			Artifact artifact = this.artifactRepository.getArtifact(classPath);

			for (Student student : contributions.keySet()) {
				double ownershipPercentage = contributions.get(student);
				PairStudentArtifact newPair = new PairStudentArtifact(student, artifact, ownershipPercentage);
				pairs.add(newPair);
			}
		}

		return pairs;
	}

	private Map<Student, Double> getContributions(Repository repository, String pathFile)
			throws RevisionSyntaxException, IOException, GitAPIException {
		BlameResult result = this.getBlameResult(repository, pathFile);
		Map<Student, Integer> frequency = this.getFrequency(result.getResultContents().size(), result);

		int totalLines = 0;

		Set<Student> frequencyKeys = frequency.keySet();

		for (Student student : frequencyKeys) {
			totalLines += frequency.get(student);
		}

		Map<Student, Double> contributions = new HashMap<Student, Double>();

		for (Student student : frequencyKeys) {
			contributions.put(student, 100.0 * frequency.get(student) / totalLines);
		}

		return contributions;
	}

}
