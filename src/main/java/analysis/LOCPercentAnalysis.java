package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

import artifact.Artifact;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;

public class LOCPercentAnalysis extends AbstractAnalysis {

	@Override
	public List<PairStudentArtifact> makePairs(GitRepository git, StudentRepository students) throws IOException, GitAPIException {
		this.students = students;
		List<String> paths = git.listRepositoryContents();
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (String classPath : paths) {
			Map<Student, Double> contributions = this.getContributions(git.getRepository(), classPath);
			Artifact artifact = new Artifact(classPath);
			
			Set<Student> keys = contributions.keySet();

			for (Student student : keys) {
				double ownershipPercentage = contributions.get(student);
				PairStudentArtifact newPair = new PairStudentArtifact(student, artifact, ownershipPercentage);
				pairs.add(newPair);
			}
		}

		return pairs;
	}

	private Map<Student, Double> getContributions(Repository repository, String pathFile)
			throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException,
			GitAPIException {
		BlameResult result = getBlameResult(repository, pathFile);
		Map<Student, Integer> frequency = this.getFrequency(result.getResultContents().size(), result);
		Set<Student> keys = frequency.keySet();
		Map<Student, Double> contributions = new HashMap<Student, Double>();
		
		int numberOfLines = 0;

		for (Student student : keys) {
			numberOfLines += frequency.get(student);
		}

		for (Student student : keys) {
			contributions.put(student, 100.0 * frequency.get(student) / numberOfLines);
		}

		return contributions;
	}

}
