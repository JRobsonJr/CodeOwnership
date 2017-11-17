package analise;

import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.PairServer;
import student.StudentServer;

public interface Analise {

	void makePairs(Repository repo, PairServer pairs, StudentServer students,String path) throws NoHeadException, GitAPIException, IOException, Exception;

}
