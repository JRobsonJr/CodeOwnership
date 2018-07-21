package git;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import util.Util;

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

	public Iterable<RevCommit> getCommits() throws GitAPIException, IOException {
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

    public TreeWalk getTreeWalk() throws IOException {
        Ref head = this.getRepository().getRef("HEAD");
        RevWalk walk = this.getRevWalk();
        RevCommit commit = walk.parseCommit(head.getObjectId());
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(this.getRepository());
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        return treeWalk;
    }

	public HashSet<String> listAllStudentsNames() throws GitAPIException, IOException {
		HashSet<String> names = new HashSet<String>();
		Iterable<RevCommit> commits = this.getCommits();

		for (RevCommit commit : commits) {
			names.add(commit.getAuthorIdent().getName());
		}
		
		return names;
	}

	public List<String> listRepositoryContents() throws IOException, RevisionSyntaxException {
		List<String> contents = new ArrayList<String>();
		TreeWalk treeWalk = this.getTreeWalk();

		while (treeWalk.next()) {
		    contents.add(treeWalk.getPathString());
		}

		return contents;
	}

	public List<String> listRepositoryJavaClasses() throws IOException, RevisionSyntaxException {
	    List<String> classes = this.listRepositoryContents();
        classes.removeIf(content -> !Util.isJavaClass(content));

        return classes;
    }
}