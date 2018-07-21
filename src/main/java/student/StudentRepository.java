package student;

import java.util.ArrayList;
import java.util.List;

import util.Util;

public class StudentRepository {

	private List<Student> students;

	public StudentRepository() {
		this.students = new ArrayList<Student>();
	}

	public Student getStudent(String name) {
		for (Student student : this.students) {
			if (student.hasAlias(name)) {
				return student;
			}
		}
		return null;
	}

	public List<Student> getStudents() {
		return this.students;
	}

	public Student[] getStudentsAsArray() {
		int size = this.students.size();
		Student[] studentArray = new Student[size];

		return this.students.toArray(studentArray);
	}
	
	public void setStudents(List<Student> students) {
		this.students = students; 
	}
	
	@Override
	public String toString() {
		String resp = "";

		for (int i = 0; i < this.students.size(); i++) {
			resp += (i + 1) + ") " + students.get(i).getName() + Util.LS;
		}

		return resp;
	}

}