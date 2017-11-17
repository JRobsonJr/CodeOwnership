package artifact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Artifact {
	
	private String artifacName;
	private Set<String> subjects;
	
	public Artifact(String artifactName) {
		this.artifacName = artifactName;
		this.subjects = new HashSet();
		
	}
	
	public String getArtifacName() {
		return artifacName;
	}
	public void setArtifacName(String artifacName) {
		this.artifacName = artifacName;
	}
	public Set<String> getSubject() {
		return subjects;
	}
	
	
	public void setSubjects(Set<String> subjects) {
		this.subjects = subjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifacName == null) ? 0 : artifacName.hashCode());
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
		if (artifacName == null) {
			if (other.artifacName != null)
				return false;
		} else if (!artifacName.equals(other.artifacName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  "Subjects: " + this.subjects.toString();
	}
	
	
	
}
