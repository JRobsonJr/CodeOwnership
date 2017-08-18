package main;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import codeOwnership.CodeOwnership;

public class Main {

	private static CodeOwnership co;
	
	public static void main(String[] args) throws Exception {
		co = new CodeOwnership();
		Repository repo = new FileRepository(
				"/home/petcomputacao/Área de Trabalho/ProjetoCodeOwnership/ProjetoP2 - Grupo de Rosbon/.git");
		co.getCreatedArtifacts(repo);
		System.out.println(co.aaa());
	}

}