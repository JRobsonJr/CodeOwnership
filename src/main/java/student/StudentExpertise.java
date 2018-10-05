package student;

import student.Student;

public class StudentExpertise {

	Student student;
	double interfacee;
	double inheritence;
	double tests;
	double exceptionHierarchy;
	double exceptions;
	double exceptionHandling;
	double persistance;
	double abstractClass;

	public StudentExpertise(Student student, double[] totalOfOwnershipOfEachClass, int[] totalOfClassesOfEachExpertise) {
		this.student = student;
		this.interfacee = (totalOfOwnershipOfEachClass[0] / totalOfClassesOfEachExpertise[0]);
		this.inheritence = (totalOfOwnershipOfEachClass[1] / totalOfClassesOfEachExpertise[1]);
		this.tests = (totalOfOwnershipOfEachClass[2] / totalOfClassesOfEachExpertise[2]);
		this.exceptionHierarchy = (totalOfOwnershipOfEachClass[3] / totalOfClassesOfEachExpertise[3]);
		this.exceptions = (totalOfOwnershipOfEachClass[4] / totalOfClassesOfEachExpertise[4]);
		this.exceptionHandling = (totalOfOwnershipOfEachClass[5] / totalOfClassesOfEachExpertise[5]);
		this.persistance = (totalOfOwnershipOfEachClass[6] / totalOfClassesOfEachExpertise[6]);
		this.abstractClass = (totalOfOwnershipOfEachClass[7] / totalOfClassesOfEachExpertise[7]);
	}

	@Override
	public String toString() {
		return this.student.getName() + "\n" + "Interface: " + this.interfacee + "\n" + "Inheritance "
				+ this.inheritence + "\n" + "Tests: " + this.tests + "\n" + "Exceptions Hyerarchy: "
				+ this.exceptionHierarchy + "\n" + "Exceptions: " + this.exceptions + "\n" + "Exceptions Handling: "
				+ this.exceptionHandling + "\n" + "Persistance: " + this.persistance + "\n" + "Abstract Class: "
				+ this.abstractClass + "\n";
	}

}
