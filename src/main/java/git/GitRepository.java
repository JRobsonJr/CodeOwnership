package git;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitRepository {

	private Repository repository;
	private Git git;
	private String directory;
	private RevWalk walk;
	private DiffFormatter diffFormatter;

	public GitRepository(String directory) throws IOException {
		this.directory = directory;
		this.repository = new FileRepository(directory);
		this.git = new Git(repository);
		this.walk = new RevWalk(repository);
		this.diffFormatter = new DiffFormatter(new FileOutputStream(FileDescriptor.out));
		diffFormatter.setRepository(repository);
	}

	public Iterable<RevCommit> getCommits() throws NoHeadException, GitAPIException, IOException {
		Iterable<RevCommit> commits = git.log().all().call();
		return commits;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public RevWalk getRevWalk() {
		return this.walk;
	}

	public DiffFormatter getDiffFormatter() {
		return this.diffFormatter;
	}

	public String getDirectory() {
		return this.directory;
	}

	public HashSet<String> listAllStudentsNames() throws NoHeadException, GitAPIException, IOException {
		HashSet<String> allStudentsNames = new HashSet<String>();
		Iterable<RevCommit> commits = this.getCommits();

		for (RevCommit commit : commits) {
			allStudentsNames.add(commit.getAuthorIdent().getName());
		}
		
		return allStudentsNames;
	}
	
	public TreeWalk getTreeWalk(GitRepository git) throws IOException {
		Ref head = this.getRepository().getRef("HEAD");
		RevWalk walk = this.getRevWalk();
		RevCommit commit = walk.parseCommit(head.getObjectId());
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(this.getRepository());
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		
		return treeWalk;
	}
}