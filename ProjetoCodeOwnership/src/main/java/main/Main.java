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
import competencia.Competencia;

public class Main {

	private static CodeOwnership co;
	private static Analise analise;
	static PairServer pairs;
	private static String repositorio;
	private static Blame blamer;
	private static String analysisType = "criacao";
	private static Competencia competencia = new Competencia();

	public static void main(String[] args) throws Exception {

		if (analysisType.equals("criacao")) {
			analise = new AnaliseCriacao();
		} else {
			analise = new AnaliseLOC();
		}

		repositorio = "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon";
		co = new CodeOwnership(analise);
		pairs = new PairServer();
		blamer = new Blame();
		Repository repo = new FileRepository(repositorio + "\\.git");

		Git git = new Git(repo);

		co.registerAllStudents(git);
		co.makePairs(repo, pairs);

		System.out.println("ToString de PairsServer:\n");
		System.out.println(pairs.toString());

		// System.out.println("Quem buliu em que num arquivo especifico: (HEAD - Estado
		// atual do repositorio) \n");
		// Especificar repositorio, qual HEAD (estado do repositorio) e arquivo que quer
		// analisar.
		// blamer.gitBlame(repo, "HEAD", "src/album/Album.java");

		System.out.println("Competencia:");
		competencia.listClassesAndSubjects(repositorio);

	}

}