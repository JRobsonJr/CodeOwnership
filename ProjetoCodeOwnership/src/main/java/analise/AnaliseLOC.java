package analise;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.PairServer;
import student.StudentServer;

public class AnaliseLOC implements Analise{

	public void makePairs(Repository repo, PairServer pairs,StudentServer students)
			throws NoHeadException, GitAPIException, IOException {
		// TODO Auto-generated method stub
		
	}

	public void deleteRemovedArtifacts(Repository repo, PairServer pairs) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
