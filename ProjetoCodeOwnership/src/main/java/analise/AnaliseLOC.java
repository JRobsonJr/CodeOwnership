package analise;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.blame.BlameResult;
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

import codeOwnership.PairRepository;
import git.GitRepository;
import student.StudentServer;

public class AnaliseLOC implements Analise {

	public void makePairs(GitRepository git, PairRepository pairs, StudentServer students)
			throws NoHeadException, GitAPIException, IOException {
		this.listRepositoryContents(git);

	}

	public void gitBlame(Repository repository, String pathFile) throws RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException, GitAPIException {

		BlameCommand blamed = new BlameCommand(repository);
		ObjectId commitID = repository.resolve("HEAD");
		blamed.setStartCommit(commitID);
		blamed.setFilePath(pathFile);
		BlameResult blameResult = blamed.call();

		int lines = blameResult.getResultContents().size();
		showBlame(lines, blameResult);
	}

	/**
	 * Exibe quem escreveu cada linha do arquivo especificado.
	 * 
	 * @param lines
	 * @param blameResult
	 */
	private void showBlame(int lines, BlameResult blameResult) {
		Map<String,Integer> frequency = new HashMap<String, Integer>();
		
		for (int i = 0; i < lines; i++) {
			RevCommit commit = blameResult.getSourceCommit(i);
			if (!blameResult.getResultContents().getString(i).trim().equalsIgnoreCase("")) {
				String authorName = commit.getAuthorIdent().getName();
				if(frequency.containsKey(authorName)){
					frequency.put(authorName, frequency.get(authorName) + 1);
				}else{
					frequency.put(authorName, 1);
				}
			}
		}
		Set<String> chaves = frequency.keySet();
		for (String nome : chaves) {
			System.out.println("Author: " + nome + "\nLinhas: " + frequency.get(nome) + "\n");
			
		}

	}

	private void listRepositoryContents(GitRepository git) throws IOException, RevisionSyntaxException, GitAPIException {
		Repository repository = git.getRepository();
		Ref head = repository.getRef("HEAD");
		RevWalk walk = git.getRevWalk();
		RevCommit commit = walk.parseCommit(head.getObjectId());
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			if (isJavaClass(treeWalk.getPathString())) {
				System.out.println("--------------------------------------------");
				System.out.println("Classe:" + treeWalk.getPathString() + ":");
				
				
				gitBlame(repository, treeWalk.getPathString());

			}
		}
	}




	private static boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

}
