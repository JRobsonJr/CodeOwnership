package git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class GitUtil {

    private GitUtil() throws InstantiationException {
        throw new InstantiationException("This is a utility class, therefore it shouldn't be instantiated.");
    }

    public static boolean isFirstCommit(RevCommit commit) {
        boolean isFirstCommit = false;

        try {
            commit.getParent(0);
        } catch (Exception e) {
            isFirstCommit = true;
        }

        return isFirstCommit;
    }

    /**
     * Returns whether the change is the type ADD.
     */
    public static boolean isAddedEntry(DiffEntry entry) {
        return entry.getChangeType() == DiffEntry.ChangeType.ADD;
    }

    /**
     * Returns whether the change is the type DELETE.
     */
    public static boolean isRemovedEntry(DiffEntry entry) {
        return entry.getChangeType() == DiffEntry.ChangeType.DELETE;
    }

    public static List<String> getArtifactsFromCommit(DiffFormatter diffFormatter, RevCommit commit) throws IOException {
        List<String> artifacts = new ArrayList<String>();

        for (DiffEntry entry : diffFormatter.scan(commit.getParent(0), commit)) {
            if (isAddedEntry(entry) && Util.isJavaClass(entry.getNewPath())) {
                artifacts.add(entry.getNewPath());
            }
        }

        return artifacts;
    }

    public static List<String> getArtifactsFromFirstCommit(TreeWalk treeWalk) throws IOException {
        List<String> artifacts = new ArrayList<String>();

        while (treeWalk.next()) {
            String pathString = treeWalk.getPathString();

            if (Util.isJavaClass(pathString)) {
                artifacts.add(pathString);
            }
        }

        return artifacts;
    }
}
