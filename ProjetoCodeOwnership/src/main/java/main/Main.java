package main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analise.Analise;
import analise.AnaliseCriacao;
import analise.AnaliseLOC;
import analise.Blame;
import codeOwnership.CodeOwnership;
import codeOwnership.PairServer;

public class Main {

	private static CodeOwnership co;
	private static Analise analise;
	static PairServer pairs;
	private static String repositorio;
	private static Blame blamer;
	private static String analysisType = "criacao";

	public static void main(String[] args) throws Exception {
		
		if (analysisType.equals("criacao")) {
			analise = new AnaliseCriacao();
		}else {
			analise = new AnaliseLOC();
			
		}
		
		repositorio = "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\.git";
		co = new CodeOwnership(analise);
		pairs = new PairServer();
		blamer = new Blame();
		Repository repo = new FileRepository(repositorio);

		Git git = new Git(repo);

		co.registerAllStudents(git);
		co.makePairs(repo, pairs);
		// ano eh mais preciso chama dentro do final do metodo de adc co.deleteRemovedArtifacts(repo, pairs);

		System.out.println("ToString de PairsServer:\n");
		System.out.println(pairs.toString());

		// System.out.println("Quem buliu em que num arquivo especifico: (HEAD - Estado
		// atual do repositorio) \n");
		// Especificar repositorio, qual HEAD (estado do repositorio) e arquivo que quer
		// analisar.
		// blamer.gitBlame(repo, "HEAD", "src/album/Album.java");

		// co.determinateArtifactSubject("C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2
		// - Grupo de Rosbon\\src\\projeto\\ProjetoPET.java");

		
	}

}