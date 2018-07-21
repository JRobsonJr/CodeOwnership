package artifact;

import java.util.ArrayList;
import java.util.List;

import util.Util;

public class ArtifactRepository {

    private List<Artifact> artifacts;

    public ArtifactRepository() {
        this.artifacts = new ArrayList<Artifact>();
    }

    public Artifact getArtifact(String path) {
        for (Artifact artifact : this.artifacts) {
            if (artifact.getPath().equals(path)) {
                return artifact;
            }
        }

        return null;
    }

    public void createArtifacts(String projectPath, List<String> classes) {
        for (String path : classes) {
            Artifact artifact = new Artifact(projectPath, path);
            this.artifacts.add(artifact);
        }
    }

    public List<Artifact> getArtifacts() {
        return this.artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    @Override
    public String toString() {
        String resp = "";

        for (Artifact artifact : this.artifacts) {
            resp += artifact.toString() + Util.LS;
        }

        return resp;
    }

}