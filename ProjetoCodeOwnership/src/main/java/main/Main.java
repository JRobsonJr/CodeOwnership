package main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analise.Analise;
import analise.AnaliseCriacao;
import analise.AnaliseLOC;
import codeOwnership.CodeOwnership;
import codeOwnership.PairServer;
import competencia.Competencia;

public class Main {

	private static CodeOwnership co;
	private static Analise analise;
	static PairServer pairs;
	private static String repositorio;
	private static String analysisType;
	private static Competencia competencia = new Competencia();

	//Args[caminho do repositorio, tipo de analise('criacao' ou 'loc')]
	
	public static void main(String[] args) throws Exception {

//		repositorio = args[0];
//		analysisType = args[1];
		
		analysisType = "linha";
		repositorio = "D:\\Users\\PET Computacao\\Documents\\David Eduardo\\codeOwnership\\ProjetoP2 - Grupo de Rosbon\\homemade-dynamite";
		String rep = "/home/mariana/homemade-dynamite/.git";
		if (analysisType.equals("criacao")) {
			System.out.println("Analise por criação:\n ");
			analise = new AnaliseCriacao();
		} else {
			 System.out.println("Analise por linha de código:\n");
			analise = new AnaliseLOC();
		}
		
		co = new CodeOwnership(analise);
		pairs = new PairServer();
		Repository repo = new FileRepository(rep);
		Git git = new Git(repo);
		co.registerAllStudents(git);
		
		co.makePairs(repo, pairs, rep);

		System.out.println("ToString de PairsServer:\n \n");
		
		System.out.println(pairs.toString());

		
//		 Especificar repositorio, qual HEAD (estado do repositorio) e arquivo que quer
//		 analisar.
	

		System.out.println("Competencia:");
		competencia.listClassesAndSubjects(rep);

	}

}