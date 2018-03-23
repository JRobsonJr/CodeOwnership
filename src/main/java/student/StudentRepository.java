package student;

import java.util.ArrayList;
import java.util.List;

import util.Util;

public class StudentRepository {

	private List<Student> students;

	public StudentRepository() {
		this.students = new ArrayList<Student>();
	}

	public void addStudent(String name, String[] aliases) {
		Student student = new Student(name, aliases);
		this.students.add(student);
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
	
	public void setStudents(List<Student> students) {
		this.students = students; 
	}
	
	@Override
	public String toString() {
		String resp = "";

		for (Student student : this.students) {
			resp += student.toString() + Util.LS;
		}

		return resp;
	}
}