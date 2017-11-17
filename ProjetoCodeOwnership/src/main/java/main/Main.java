package main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analise.Analise;
import analise.AnaliseCriacao;
import analise.AnaliseLOC;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import competencia.Competencia;

public class Main {

	private static CodeOwnership co;
	private static Analise analise;
	static PairRepository pairs = new PairRepository();;
	private static String repositorio;
	private static String analysisType;
	public static void main(String[] args) throws Exception {		
		
		analysisType = "criacao";
		repositorio = "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon";
		
		//Deixa aqui o caminho do repositoria do seu PC para facilitar na hora de mudar(sem o .git):
		
		//"/home/mariana/homemade-dynamite/";
		//"C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon"
		
		if (analysisType.equals("criacao")) {
			System.out.println("Analise por criação:\n ");
			analise = new AnaliseCriacao();
		} else {
			 System.out.println("Analise por linha de código:\n");
			analise = new AnaliseLOC();
		}
		co = new CodeOwnership(analise);
		
		Repository repo = new FileRepository(repositorio + "\\.git");
		Git git = new Git(repo);

		co.registerAllStudents(git);
		co.makePairs(repo, pairs, repositorio);
		co.determinateAtifactSubjects(repositorio, pairs);
		 
		System.out.println(pairs.toString());
	
	}

}