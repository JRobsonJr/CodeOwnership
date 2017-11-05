package codeOwnership;

import java.io.IOException;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * 
 * @author mariana
 *
 */

public class Blame {
	

	public void gitBlame(Repository repository, String currentBranch, String pathFile) throws RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException, GitAPIException {

		BlameCommand blamed = new BlameCommand(repository);
		ObjectId commitID = repository.resolve(currentBranch);
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
		for(int i = 0; i < lines; i++) {
			RevCommit commit = blameResult.getSourceCommit(i);
			if(!blameResult.getResultContents().getString(i).trim().equalsIgnoreCase("")) {
				System.out.println("Line: " + i + " Author: " + commit.getAuthorIdent().getName());
				System.out.println("Code: " + blameResult.getResultContents().getString(i) + "\n");	
			}

		}

	}

}
