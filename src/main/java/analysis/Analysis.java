package analysis;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import codeOwnership.PairRepository;
import git.GitRepository;
import student.StudentRepository;

public interface Analysis {

	public void makePairs(GitRepository git, PairRepository pairs, StudentRepository students)
			throws NoHeadException, GitAPIException, IOException, Exception;

}
