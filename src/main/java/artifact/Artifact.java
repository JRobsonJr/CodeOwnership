package artifact;

import expertise.Expertise;
import expertise.ExpertiseExtractorUtil;

import java.util.Set;

import static util.Util.LS;

public class Artifact {

	private String absolutePath;
	private String relativePath;
	private Set<Expertise> expertise;
	
	public Artifact(String projectPath, String path) {
		this.absolutePath = projectPath + "/" + path;
		this.relativePath = path;
		this.expertise = ExpertiseExtractorUtil.extractExpertiseFromJavaClass(this.absolutePath);
	}
	
	public String getPath() {
		return this.relativePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relativePath == null) ? 0 : relativePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artifact other = (Artifact) obj;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Path: " + this.relativePath + LS + "Expertise: " + this.expertise.toString();
	}	

}
