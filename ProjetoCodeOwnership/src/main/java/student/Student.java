package student;


import java.util.ArrayList;

import org.eclipse.jgit.lib.PersonIdent;

import util.Util;

public class Student {

	private ArrayList<String> names;
	private String email;
	private PersonIdent id;

	
	public Student(String[] studentNames) {
		this.names = new ArrayList<String>();
		for (int i = 0; i < studentNames.length; i++) {
			names.add(studentNames[i]);
		}
	}
	
	public Student(PersonIdent id) {
		this.id = id;
		this.names = new ArrayList<String>();
		this.email = id.getEmailAddress();
	}

	public PersonIdent getId() {
		return this.id;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.names.toString();	
	}

	@Override
	public String toString() {
		return "Name: " + this.names.toString() + Util.LS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	
	// se achar um nome igual já eh igual !
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		for (int i = 0; i < this.names.size(); i++) {
			if(other.getOneName().equals(names.get(i))) {
				return true;
			}
		}
		return false;
	}

	private String getOneName() {
		return this.names.get(0);
	}

	public boolean hasName(String name) {
		return names.contains(name);
	}

}