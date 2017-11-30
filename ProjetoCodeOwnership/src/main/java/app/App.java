package app;

import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analise.Analysis;
import analise.AnaliseCriacao;
import analise.AnaliseLOC;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import competencia.Competencia;
import util.Util;

public class App {

	private static CodeOwnership co;
	private static Analysis analysis;
	static PairRepository pairs = new PairRepository();;
	private static String repository;
	private static String analysisType;

	public static void main(String[] args) throws Exception {
		repository = JOptionPane.showInputDialog("Caminho do repositorio:");
		analysisType = JOptionPane.showInputDialog("Tipo de analise (criacao ou LOC)");

		// analysisType = "criacao";
		// repository =
		// "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de
		// Rosbon\\";

		// Deixa aqui o caminho do repositoria do seu PC para facilitar na hora
		// de mudar(sem o .git):
		// C:\\Users\\Documentos\\Desktop\\CodeOwnership\\homemade-dynamite\\
		// "/home/mariana/Documents/Didatica_LP2/homemade-dynamite/";
		// "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de
		// Rosbon\\"

		if (analysisType.equals("criacao")) {
			System.out.println("Analise por criação:" + Util.LS);
			analysis = new AnaliseCriacao();
		} else {
			System.out.println("Analise por linha de código:" + Util.LS);
			analysis = new AnaliseLOC();
		}

		co = new CodeOwnership(analysis, "/home/mariana/Documents/Didatica_LP2/homemade-dynamite/.git");

		Repository repo = new FileRepository(repository + ".git");

		co.registerAllStudents();
		co.makePairs(repo, pairs, repository);
		// co.determinateAtifactSubjects(repository, pairs);

		pairs.toString();

		JOptionPane.showMessageDialog(null, pairs.toString());
		// System.out.println(pairs.toString());
	}

}