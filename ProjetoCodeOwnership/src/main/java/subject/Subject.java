package subject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import codeOwnership.PairRepository;
import util.Util;

public class Subject {

	public void listClassesAndSubjects(String repoPath, PairRepository pairs) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(repoPath + ".git")).readEnvironment().findGitDir().build();
		listRepositoryContents(repository, repoPath, pairs);
		repository.close();
	}

	private static void listRepositoryContents(Repository repository, String repoPath, PairRepository pairs)
			throws IOException {
		Ref head = repository.getRef("HEAD");
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = walk.parseCommit(head.getObjectId());
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			if (Util.isJavaClass(treeWalk.getPathString())) {
				// using windows:
				// String caminho = makePath(treeWalk.getPathString(),
				// repoPath);

				String path = repoPath + "/" + treeWalk.getPathString();
				Set<String> subjects = determineArtifactSubjects(path);
				
				if (pairs.getPairByArtifactName(treeWalk.getPathString()) != null) {
					pairs.getPairByArtifactName(treeWalk.getPathString()).getArtifact().setSubjects(subjects);
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
	public static Set<String> determineArtifactSubjects(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		Set<String> subjects = new HashSet<String>();
		String line = "";

		while (true) {
			if (line != null) {
				String[] splittedLine = line.split(" ");
				
				for (int i = 0; i < splittedLine.length; i++) {
					if (splittedLine[i].trim().equals("implements")) {
						subjects.add("Interface");
					}
					if (splittedLine[i].trim().equals("extends")) {
						subjects.add("Herança");
					}
					if (splittedLine[i].trim().equals("@Test")) {
						subjects.add("Testes");
					}
					if (splittedLine[i].trim().equals("throws")) {
						subjects.add("Exceptions");
					}
					if (splittedLine[i].trim().equals("try") || splittedLine[i].trim().equals("catch")) {
						subjects.add("Exception handling");
					}
					if (splittedLine[i].trim().equals("IOException") || splittedLine[i].trim().equals("File")) {
						subjects.add("Persistência(arquivos)");
					}
					if (splittedLine[i].trim().equals("abstract")) {
						subjects.add("Classes abstratas");
					}
					
				}
			} else {
				break;
			}

			line = buffRead.readLine();
		}

		buffRead.close();
		return subjects;
	}

}