package competencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ManipuladorArquivo {

	public static void leitor(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		String linha = "";

		while (true) {
			if (linha != null) {
				
				String[] palavrasDaLinha = linha.split(" ");
				
				
				for (int i = 0; i < palavrasDaLinha.length; i++) {
					if (palavrasDaLinha[i].trim().equals("implements")) {
						System.out.println(linha);
						System.out.println(palavrasDaLinha[i]);
					}
					if (palavrasDaLinha[i].trim().equals("extends")) {
						System.out.println(linha);
						System.out.println(palavrasDaLinha[i]);
					}
					if (palavrasDaLinha[i].trim().equals("@Test")) {
						System.out.println(linha);
						System.out.println(palavrasDaLinha[i]);
					}
				}
			} else {
				break;
			}
			linha = buffRead.readLine();
		}
		buffRead.close();
	}

	public static void main(String[] args) throws IOException {
		ManipuladorArquivo a = new ManipuladorArquivo();
		a.leitor("C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\tests\\pessoa\\PessoaTest.java");
		
		
		//a.leitor(
		//		"C:\\Users\\Documentos\\Desktop\\CodeOwnership\\ProjetoP2 - Grupo de Rosbon\\src\\pessoa\\PessoaController.java");
	}
}