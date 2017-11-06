package codeOwnership;

import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

public interface Analise {
	
	
	public void criaPares(Repository repositorio, PairServer server) throws Exception;
	
	public void registerAllStudents(Git git) throws GitAPIException, IOException;
	
	public void deleteRemovedArtifacts(Repository repo, PairServer pairs) throws Exception;
	

}
