package codeOwnership;

import artifact.Artifact;
import student.Student;
import util.Util;

public class PairStudentArtifact {
	
	private Student student;
	private Artifact artifact;
	public double ownershipPercentage; 

	public PairStudentArtifact(Student student, Artifact artifact, double ownershipPercentage ) {
		this.student = student;
		this.artifact = artifact;
	}
	
	public Artifact getArtifact() {
		return this.artifact;
	}
	
	public String getStudentName() {
		return this.student.getName();
	}
	
	public String getArtifactName() {
		return this.artifact.getName();
	}

	@Override
	public String toString() {
		return student.getName() + " is the owner of: " + artifact.getName() + " (" + this.ownershipPercentage + "%) " + Util.LS + "Artifact details:" + Util.LS + artifact.toString();
	}
}
