package student;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;

import util.Util;

public class StudentRepository {

	private List<Student> students;

	public StudentRepository() {
		this.students = new ArrayList<Student>();
	}

	public void addStudent(PersonIdent id) {
		Student student = new Student(id);
		
		if (!students.contains(student)) {
			students.add(student);
		}
	}
	
	public Student getStudent(String email) {
		for (Student student : students) {
			if (student.getEmail().equals(email)) {
				return student;
			}
		}
		// TODO Exception? RESP: Se o sistema só usa isso caso devesse existir o aluno, sim.
		return null;
	}
	
	@Override
	public String toString() {
		String resp = "";
		
		for (int i = 0; i < students.size(); i++) {
			resp += students.get(i).toString() + Util.LS;
		}
		
		return resp;
	}

	public List<Student> getStudents() {
		return students;
	}
	
	
}