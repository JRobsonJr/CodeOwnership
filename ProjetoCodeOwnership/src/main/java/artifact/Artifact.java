package artifact;

public class Artifact {
	
	private String artifacName;
	private ArtifactSubject subject;
	//TODO: subject, como acessar a classe? como ver sua competencia ? 
	
	public Artifact(String artifactName) {
		this.artifacName = artifactName;
		
	}
	
	public String getArtifacName() {
		return artifacName;
	}
	public void setArtifacName(String artifacName) {
		this.artifacName = artifacName;
	}
	public ArtifactSubject getSubject() {
		return subject;
	}
	public void setSubject(ArtifactSubject subject) {
		this.subject = subject;
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

	
	
	
	
}
