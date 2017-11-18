package analise;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import codeOwnership.PairRepository;
import git.GitRepository;
import student.StudentServer;

public interface Analise {

	void makePairs(GitRepository git, PairRepository pairs, StudentServer students) throws NoHeadException, GitAPIException, IOException, Exception;

}
