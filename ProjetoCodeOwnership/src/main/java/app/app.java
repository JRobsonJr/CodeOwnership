package app;

import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import analise.Analise;
import analise.AnaliseCriacao;
import analise.AnaliseLOC;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;
import competencia.Competencia;

public class app {

	private static CodeOwnership co;
	private static Analise analise;
	static PairRepository pairs = new PairRepository();;
	private static String repository;
	private static String analysisType;
	
	public static void main(String[] args) throws Exception {		
		
		
		repository = JOptionPane.showInputDialog("Cainho do repositorio:");
		analysisType = JOptionPane.showInputDialog("Tipo de analise (criacao ou LOC)");
		
 		//analysisType = "criacao";
		//repository = "C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\";

		//Deixa aqui o caminho do repositoria do seu PC para facilitar na hora de mudar(sem o .git):
		//C:\\Users\\Documentos\\Desktop\\CodeOwnership\\homemade-dynamite\\
		//"/home/mariana/homemade-dynamite/";
		//"C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\"
		
		if (analysisType.equals("criacao")) {
			System.out.println("Analise por criação:\n ");
			analise = new AnaliseCriacao();
		} else {
			 System.out.println("Analise por linha de código:\n");
			analise = new AnaliseLOC();
		}
		
		co = new CodeOwnership(analise, repository + ".git");
		
		Repository repo = new FileRepository(repository + ".git");
		Git git = new Git(repo);

		co.registerAllStudents();
		co.makePairs(repo, pairs, repository);
		co.determinateAtifactSubjects(repository, pairs);
		
		JOptionPane.showMessageDialog(null, pairs.toString());
		//System.out.println(pairs.toString());
	
	}

}