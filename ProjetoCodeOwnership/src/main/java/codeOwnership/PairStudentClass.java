package codeOwnership;

import student.Student;

public class PairStudentClass {
	
	Student student;
	String artifactName;

	public PairStudentClass(Student student, String artifactName) {
		this.student = student;
		this.artifactName = artifactName;
	}

	@Override
	public String toString() {
		return student.getId().getName() + "  is the owner of: " + artifactName;
	}

}
