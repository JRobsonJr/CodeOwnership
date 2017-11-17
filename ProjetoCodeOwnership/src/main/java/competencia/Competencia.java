package competencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import analise.Analise;
import analise.AnaliseCriacao;
import codeOwnership.CodeOwnership;
import codeOwnership.PairRepository;

public class Competencia {

	public void listClassesAndSubjects(String repoPath, PairRepository pairs) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(repoPath + "\\.git")).readEnvironment().findGitDir().build();
		listRepositoryContents(repository, repoPath, pairs);
		repository.close();
	}

	private static void listRepositoryContents(Repository repository, String repoPath, PairRepository pairs)
			throws IOException {
		Ref head = repository.getRef("HEAD");
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = walk.parseCommit(head.getObjectId());
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			if (isJavaClass(treeWalk.getPathString())) {
				String caminho = makePath(treeWalk.getPathString(), repoPath);
				Set<String> competencia = determinateArtifactSubject(caminho);
				if (pairs.getPairByArtifactName(treeWalk.getPathString()) != null) {
					pairs.getPairByArtifactName(treeWalk.getPathString()).getArtifact().setSubjects(competencia);	
				}
			}
		}
	}

	/**
	 * Tranforma o caminho de forma que seja possivel ler no windows
	 * 
	 * @param pathString
	 *            - src/exception/LogicaException.java
	 * @return - C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de
	 *         Rosbon\\src\\exception\\LogicaException.java
	 */
	private static String makePath(String pathString, String repoPath) {
		String aux = "\\" + pathString.replace("/", "\\");
		pathString = repoPath + aux;

		return pathString;

	}

	/**
	 * Determina as compentencias do artifact atraves de palavras chaves
	 * 
	 * @param path
	 *            - caminho do artifact
	 * @throws IOException
	 */
	public static Set<String> determinateArtifactSubject(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		Set<String> competencias = new HashSet<String>();
		String linha = "";
		while (true) {
			if (linha != null) {
				String[] palavrasDaLinha = linha.split(" ");
				for (int i = 0; i < palavrasDaLinha.length; i++) {
					if (palavrasDaLinha[i].trim().equals("implements")) {
						competencias.add("Interface");
					}
					if (palavrasDaLinha[i].trim().equals("extends")) {
						competencias.add("Herança");
					}
					if (palavrasDaLinha[i].trim().equals("@Test")) {
						competencias.add("Testes");
					}
					if (palavrasDaLinha[i].trim().equals("throws")) {
						competencias.add("Exceptions");
					}
					if (palavrasDaLinha[i].trim().equals("try") || palavrasDaLinha[i].trim().equals("catch")) {
						competencias.add("Tratamentos de Exceptions");
					}
					if (palavrasDaLinha[i].trim().equals("IOException") || palavrasDaLinha[i].trim().equals("File")) {
						competencias.add("Persistência(arquivos)");
					}
				}
			} else {
				break;
			}
			linha = buffRead.readLine();
		}
		buffRead.close();
		return competencias;
	}

	/**
	 * Returns whether the current artifact is a Java Class.
	 */
	private static boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

}