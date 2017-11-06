package codeOwnership;

import java.io.BufferedReader;
import java.io.FileDescriptor;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import artifact.Artifact;
import student.Student;
import student.StudentServer;

public class CodeOwnership {

	StudentServer students;
	PairServer pairs;
	private final String LS = System.lineSeparator();

	public CodeOwnership() {
		this.students = new StudentServer();
	}

	public void registerAllStudents(Git git) throws GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();

		for (RevCommit commit : commits) {
			students.addStudent(commit.getAuthorIdent());
		}
		// TODO: como lidar com mesma pessoas mas com Id diferente
	}

	public void creatPairs(Repository repo, PairServer pairs) throws Exception {
		RevWalk walk = new RevWalk(repo);
		DiffFormatter diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repo);

		List<RevCommit> commits = getCommits(repo);

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				AddArtifactsFromFirtsCommit(repo, pairs, walk, commit);
				return;
			} else {

				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
					if (isNewArtifact(entry) && isJavaClass(entry.getNewPath())) {
						Artifact artifact = new Artifact(entry.getNewPath());
						PairStudentArtifact auxPair = new PairStudentArtifact(
								students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
						pairs.addPair(auxPair);
					}
				}
			}
		}

	}

	public void deleteRemovedArtifacts(Repository repo, PairServer pairs) throws Exception {
		RevWalk walk = new RevWalk(repo);
		DiffFormatter diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repo);

		List<RevCommit> commits = getCommits(repo);

		for (RevCommit commit : commits) {
			if (isFirstCommit(commit)) {
				AddArtifactsFromFirtsCommit(repo, pairs, walk, commit);

			} else {
				for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
					if (isRemovedArtifact(entry) && isJavaClass(entry.getOldPath())) {
						Artifact artifact = new Artifact(entry.getOldPath());
						pairs.removePair(artifact);
					}
				}
			}
		}
	}

	private boolean isFirstCommit(RevCommit commit) {
		RevCommit testing = null;
		try {
			testing = commit.getParent(0);
		} catch (Exception e) {
		}

		return testing == null;

	};

	/**
	 * 
	 * @param repositorio
	 * @return lista com todos os commits
	 * @throws NoHeadException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	private List<RevCommit> getCommits(Repository repositorio) throws NoHeadException, GitAPIException, IOException {
		Git git = new Git(repositorio);
		Iterable<RevCommit> commits = git.log().all().call();
		List<RevCommit> listCommits = new ArrayList<RevCommit>();

		for (RevCommit commit : commits) {
			listCommits.add(commit);

		}
		return listCommits;

	}

	/**
	 * Determina as compentencias do artifact
	 * 
	 * @param path
	 *            - caminho do artifact
	 * @throws IOException
	 */
	public void determinateArtifactSubject(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		String linha = "";

		// TODO: como faz para salvar em artifact o seu subject? preciso recuperar o
		// artifact, mas nao consigo pegar o nome dele pelo caminho, não consigo fazer
		// split de \\ para tentar recuperar o nome !!!!
		// String a = path.replaceAll("\"", " ");
		// System.out.println(a);
		// String[] aux = path.split("\\");
		// System.out.println(Arrays.toString(aux));

		while (true) {
			if (linha != null) {

				String[] palavrasDaLinha = linha.split(" ");

				for (int i = 0; i < palavrasDaLinha.length; i++) {
					if (palavrasDaLinha[i].trim().equals("implements")) {
						System.out.println("A classe " + path + "tem como subject interface");

					}
					if (palavrasDaLinha[i].trim().equals("extends")) {
						System.out.println("A classe:  " + path + " tem como subject heranaça");
					}
					if (palavrasDaLinha[i].trim().equals("@Test")) {
						System.out.println("A classe   " + path + "tem como subject Testes");
					}
				}
			} else {
				break;
			}
			linha = buffRead.readLine();
		}
		buffRead.close();
	}

	/**
	 * Fix up for the first commit case
	 * 
	 */
	private void AddArtifactsFromFirtsCommit(Repository repo, PairServer pairs, RevWalk walk, RevCommit commit)
			throws MissingObjectException, IncorrectObjectTypeException, IOException, CorruptObjectException {

		ObjectReader reader = repo.newObjectReader();
		RevTree tree = walk.parseTree(commit);
		CanonicalTreeParser aParser = new CanonicalTreeParser();
		aParser.reset(reader, tree);
		TreeWalk tWalk = new TreeWalk(reader);
		tWalk.addTree(aParser);
		tWalk.setRecursive(true);
		while (tWalk.next()) {
			if (isJavaClass(tWalk.getPathString())) {
				// como eh o primeiro commit nem precisa verificar se eh ADD.
				Artifact artifact = new Artifact(tWalk.getPathString());
				PairStudentArtifact auxPair = new PairStudentArtifact(
						students.getStudent(commit.getAuthorIdent().getEmailAddress()), artifact);
				pairs.addPair(auxPair);
			}
		}

	}

	/**
	 * Returns whether the change is the type ADD(created for the first time)
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
	}

	/**
	 * Returns whether the change is the type DELETE
	 */
	private boolean isRemovedArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.DELETE;
	}

	public void getDiffHead(Repository repository) throws IncorrectObjectTypeException, IOException, GitAPIException {

		/* Mostra o que aconteceu entre 1 estado do repositorio e outro, "HEAD~97^{tree}" 97 commits atrás da atual
		 * "HEAD^{tree}"
		 * mostra o que foi feito em cada arquivo: ADD/DELETE/MODIFY
		 * 
		 * Entry: DiffEntry[MODIFY src/projeto/Projeto.java]
		 * 
		 * */
		
		Git git = new Git(repository);
		ObjectId oldHead = repository.resolve("HEAD^{tree}");
		ObjectId head = repository.resolve("HEAD^^^^^{tree}");

		ObjectReader reader = repository.newObjectReader();

		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
		oldTreeIter.reset(reader, oldHead);
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, head);

		List<DiffEntry> diffs = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
		for (DiffEntry entry : diffs) {
			System.out.println("Entry: " + entry);
		
		}
	}

	
	
	
	/**
	 * Returns whether the current artifact is a Java Class.
	 */
	private boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

}