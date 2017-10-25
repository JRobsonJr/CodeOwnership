package main;

import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.CodeOwnership;
import codeOwnership.PairServer;
import codeOwnership.PairStudentArtifact;

public class Main {

	private static CodeOwnership co;
	static PairServer pairs;
	
	public static void main(String[] args) throws Exception {
		co = new CodeOwnership();
		pairs = new PairServer();
		Repository repo = new FileRepository(
				"C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\.git");
		
		Git git = new Git(repo);
		
		co.registerAllStudents(git);
		co.creatPairs(repo, pairs);
		co.deleteRemovedArtifacts(repo, pairs);
		
		System.out.println("ToString de PairsServer:\n");
		System.out.println(pairs.toString());
		
		
		System.out.println("\n "+ "----------------------------Print by student name-------------------------------" + "\n");
						
		System.out.println(pairs.getPairsByStudentName("Júlio Barreto"));
		
		
		
//		co.getCreatedArtifacts(repo);
//		System.out.println(co.aaa());
	}

}