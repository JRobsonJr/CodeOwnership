package main;

import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.Blame;
import codeOwnership.CodeOwnership;
import codeOwnership.PairServer;
import codeOwnership.PairStudentArtifact;

public class Main {

	private static CodeOwnership co;
	static PairServer pairs;
	private static String repositorio;
	private static Blame blamer;
	
	public static void main(String[] args) throws Exception {
		repositorio = "/home/mariana/homemade-dynamite/.git";
		co = new CodeOwnership();
		pairs = new PairServer();
		blamer = new Blame();
		Repository repo = new FileRepository(repositorio);
		
		Git git = new Git(repo);
		
		co.registerAllStudents(git);
		co.creatPairs(repo, pairs);
		co.deleteRemovedArtifacts(repo, pairs);
		
		System.out.println("ToString de PairsServer:\n");
		System.out.println(pairs.toString());
		
			
		System.out.println("Quem buliu em que num arquivo especifico: (HEAD - Estado atual do repositorio) \n");
		// Especificar repositorio, qual HEAD (estado do repositorio) e arquivo que quer analisar.
		blamer.gitBlame(repo, "HEAD", "src/album/Album.java");
		
	 
		//co.determinateArtifactSubject("C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\src\\projeto\\ProjetoPET.java");
		
		co.getDiffHead(repo);
		
		System.out.println("\n "+ "----------------------------Print by student name-------------------------------" + "\n");
		System.out.println(pairs.getPairsByStudentName("JRobsonJr"));
		
		
		
//		co.getCreatedArtifacts(repo);
//		System.out.println(co.aaa());
	}

}