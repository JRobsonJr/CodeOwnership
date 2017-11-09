package main;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.Analise;
import codeOwnership.AnaliseCriacao;
import codeOwnership.Blame;
import codeOwnership.CodeOwnership;
import codeOwnership.PairServer;

public class Main {

	private static CodeOwnership co;
	private static Analise analise;
	static PairServer pairs;
	private static String repositorio;
	private static Blame blamer;
	
	public static void main(String[] args) throws Exception {
		analise = new AnaliseCriacao();
		repositorio = "/home/mariana/homemade-dynamite/.git";
		co = new CodeOwnership();
		pairs = new PairServer();
		blamer = new Blame();
		Repository repo = new FileRepository(repositorio);
		
		Git git = new Git(repo);
		
		analise.registerAllStudents(git);
		analise.criaPares(repo, pairs);
		analise.deleteRemovedArtifacts(repo, pairs);
		
		System.out.println("ToString de PairsServer:\n");
		System.out.println(pairs.toString());
		
			
		//System.out.println("Quem buliu em que num arquivo especifico: (HEAD - Estado atual do repositorio) \n");
		// Especificar repositorio, qual HEAD (estado do repositorio) e arquivo que quer analisar.
		//blamer.gitBlame(repo, "HEAD", "src/album/Album.java");
		
	 
		//co.determinateArtifactSubject("C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\src\\projeto\\ProjetoPET.java");
		
	
		
		System.out.println("\n "+ "----------------------------Print by student name-------------------------------" + "\n");
		//System.out.println(pairs.getPairsByStudentName("JRobsonJr"));
		
		
		
//		co.getCreatedArtifacts(repo);
//		System.out.println(co.aaa());
	}

}