package artifact;

import java.util.ArrayList;

public class Artifact {
	
	private String artifacName;
	private ArrayList<ArtifactSubject> subjects;
	
	public Artifact(String artifactName) {
		this.artifacName = artifactName;
		
	}
	
	public String getArtifacName() {
		return artifacName;
	}
	public void setArtifacName(String artifacName) {
		this.artifacName = artifacName;
	}
	public ArrayList<ArtifactSubject> getSubject() {
		return subjects;
	}
	
	/**
	 * 
	 * Adiciona um subject de acordo com o nome passado com paramentro ao artefato
	 * @param subject
	 */
	public void addSubjectByName(String subject) {
		if(subject.equals("heranca")) {
			this.subjects.add(ArtifactSubject.HERANCA);
		} else if(subject.equals("teste")) {
			this.subjects.add(ArtifactSubject.TESTES);
		}else if (subject.equals("interface")) {
			this.subjects.add(ArtifactSubject.COMPOSICAO);
		}
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
