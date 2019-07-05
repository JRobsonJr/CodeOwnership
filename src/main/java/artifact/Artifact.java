package artifact;

import expertise.Expertise;
import expertise.ExpertiseExtractorUtil;
import util.Util;

import java.util.Set;

public class Artifact {

	private String absolutePath;
	private String relativePath;
	private int size;
	private Set<Expertise> expertise;
	
	public Artifact(String projectPath, String path) {
		this.absolutePath = projectPath + "/" + path;
		this.relativePath = path;
		this.expertise = ExpertiseExtractorUtil.extractExpertiseFromJavaClass(this.absolutePath);
		this.size = Util.countLines(this.absolutePath);
	}
	
	public String getPath() {
		return this.relativePath;
	}

	public int getSize() {
		return this.size;
	}

	public String[] getExpertiseArray() {
		Expertise[] expertiseArray = this.expertise.toArray(new Expertise[this.expertise.size()]);
		String[] stringArray = new String[this.expertise.size()];

		for (int i = 0; i < this.expertise.size(); i++) {
			stringArray[i] = expertiseArray[i].name();
		}

		return stringArray;
	}

	
	
	public Set<Expertise> getExpertise() {
		return this.expertise;
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
		return this.relativePath + "\n" + "Expertises: " + this.expertise.toString();
	}	

}
