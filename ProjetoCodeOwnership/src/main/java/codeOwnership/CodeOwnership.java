package codeOwnership;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import student.Student;
import student.StudentController;

public class CodeOwnership {

	StudentController students;
	private final String LS = System.lineSeparator();
	private final String SEPARATOR = "--------------------------------------------------";

	public CodeOwnership() {
		this.students = new StudentController();
	}

	// se ele for adicionar todos os adds em students esse nome ta meio errado
	public void getCreatedArtifacts(Repository repo) throws Exception {
		Git git = new Git(repo);
		RevWalk walk = new RevWalk(repo);
		List<Ref> branches = git.branchList().call();

		for (Ref branch : branches) {
			String branchName = branch.getName();

			System.out.println("Commits of branch: " + branchName + LS + SEPARATOR);

			Iterable<RevCommit> commits = git.log().all().call();

			for (RevCommit commit : commits) {

				FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
				DiffFormatter diffFormatter = new DiffFormatter(stdout);
				diffFormatter.setRepository(repo);
				RevCommit diffWith = null;

				try {
					diffWith = commit.getParent(0);
				} catch (Exception e) {
					System.out.println("rs viu");
				}

				// TODO: erro do primeiro commit

				boolean foundInThisBranch = foundInThisBranch(repo, walk, branchName, commit);

				if (foundInThisBranch) {
					System.out.println(commit.getName());
					System.out.println(commit.getAuthorIdent().getName());
					System.out.println(commit.getAuthorIdent().getEmailAddress());
					System.out.println(commit.getAuthorIdent().getWhen());
					System.out.println(commit.getFullMessage());
				}

				try {
					for (DiffEntry entry : diffFormatter.scan(diffWith, commit)) {
						if (isNewArtifact(entry) && isJavaClass(entry.getNewPath())) {
							System.out.println(entry.getChangeType() + ": " + entry.getNewPath());

							Student studentAux = students.getStudent(commit.getAuthorIdent().getEmailAddress());
							if (studentAux == null) {
								students.addStudent(commit.getAuthorIdent());
							}
							studentAux = students.getStudent(commit.getAuthorIdent().getEmailAddress());
							studentAux.addCreatedArtifact(entry.getNewPath());
						}
					}
				} catch (Exception e) {
					
				}

				System.out.println(SEPARATOR);
			}
		}
	}

	public String aaa() {
		return students.toString();
	}

	/**
	 * Returns whether the change is the type ADD(created for the first time)
	 */
	private boolean isNewArtifact(DiffEntry entry) {
		return entry.getChangeType() == ChangeType.ADD;
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

	/**
	 * Returns whether the current commit is found in the current branch.
	 */
	private boolean foundInThisBranch(Repository repo, RevWalk walk, String branchName, RevCommit commit)
			throws Exception {
		boolean foundInThisBranch = false;

		RevCommit targetCommit = walk.parseCommit(repo.resolve(commit.getName()));

		for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
			if (e.getKey().startsWith(Constants.R_HEADS)
					&& walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
				String foundInBranch = e.getValue().getName();
				if (branchName.equals(foundInBranch)) {
					foundInThisBranch = true;
					break;
				}
			}
		}

		return foundInThisBranch;
	}

}
