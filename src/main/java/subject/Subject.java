package subject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import codeOwnership.PairRepository;
import codeOwnership.PairStudentArtifact;
import git.GitRepository;
import util.Util;

public class Subject {

	public void listClassesAndExpertise(GitRepository git, String repoPath, PairRepository pairs) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(repoPath + ".git")).readEnvironment().findGitDir().build();
		listRepositoryContents(git, repoPath, pairs);
		repository.close();
	}

	private void listRepositoryContents(GitRepository git, String repoPath, PairRepository pairs) throws IOException {
		TreeWalk treeWalk = git.getTreeWalk(git);

		while (treeWalk.next()) {
			String pathString = treeWalk.getPathString();

			if (Util.isJavaClass(pathString)) {
				String path = repoPath + "/" + pathString;
				Set<Expertise> subjects = determineArtifactSubjects(path);
				List<PairStudentArtifact> artifactPairs = pairs.getPairsByArtifactName(pathString);

				for (PairStudentArtifact pair : artifactPairs) {
					pair.getArtifact().setSubjects(subjects);
				}
			}
		}
	}

	/**
	 * Determina as competencias do artifact atraves de palavras chaves
	 * 
	 * @param path
	 *            - caminho do artifact
	 * @throws IOException
	 */
	public Set<Expertise> determineArtifactSubjects(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		Set<Expertise> subjects = new HashSet<Expertise>();
		String line = buffRead.readLine();

		while (line != null) {
			String[] splitLine = line.split(" ");

			for (int i = 0; i < splitLine.length; i++) {
				String word = splitLine[i].trim();
				Expertise expertise = extractExpertise(word);

				if (expertise != null) {
					subjects.add(expertise);
				}
			}

			line = buffRead.readLine();
		}

		buffRead.close();

		return subjects;
	}

	private Expertise extractExpertise(String word) {
		for (Expertise exp : Expertise.values()) {
			if (exp.containsKeyword(word)) {
				return exp;
			}
		}

		return null;
	}
}