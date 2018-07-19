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
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import codeOwnership.PairRepository;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import student.Student;
import student.StudentRepository;
import util.Util;

public class LOCPercentAnalysis extends AbstractAnalysis {

	private StudentRepository students;

	@Override
	public void makePairs(GitRepository git, PairRepository pairs, StudentRepository students) throws Exception {
		this.students = students; // Isso faz mais sentido em um construtor.
		List<String> paths = listRepositoryContents(git);
		
		for (String classPath : paths) {
			Map<Student, Double> contributions = this.getContributions(git.getRepository(), classPath);
			Artifact artifact = new Artifact(classPath);
			
			Set<Student> keys = contributions.keySet();
			
			for (Student student : keys) {
				double ownershipPercentage = contributions.get(student);
				PairStudentArtifact newPair = new PairStudentArtifact(student, artifact, ownershipPercentage);
				pairs.addPair(newPair);
			}
		}
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
			if (isValidLine(blameResult.getResultContents().getString(i))) {
				Student newStudent = students.getStudent(commit.getAuthorIdent().getName());
				if(!frequency.containsKey(newStudent)) frequency.put(newStudent, 0);
				frequency.put(newStudent, frequency.get(newStudent) + 1);
			}
		}

		return frequency;
	}

	private List<String> listRepositoryContents(GitRepository git)
			throws IOException, RevisionSyntaxException, GitAPIException {
		List<String> classes = new ArrayList<String>();
		TreeWalk treeWalk = git.getTreeWalk(git);
		
		while (treeWalk.next()) {
			if (Util.isJavaClass(treeWalk.getPathString())) {
				classes.add(treeWalk.getPathString());
			}
		}

		return classes;
	}
	
	private boolean isValidLine(String line) {
		return !((line.contains("import") && (line.trim().equals("}") || line.trim().equals("{")) 
				&& line.trim().equals("")));
	}

}
