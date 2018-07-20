package artifact;

import expertise.Expertise;

import java.util.HashSet;
import java.util.Set;

public class Artifact {
	
	private String name;
	private Set<Expertise> expertises;
	
	public Artifact(String artifactName) {
		this.name = artifactName;
		this.expertises = new HashSet<Expertise>();
	}
	
	public String getName() {
		return this.name;
	}

	public void setExpertises(Set<Expertise> expertises) {
		this.expertises = expertises;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expertises: " + this.expertises.toString();
	}	
}
