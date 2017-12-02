package student;


import org.eclipse.jgit.lib.PersonIdent;

import util.Util;

public class Student {

	private String name, email;
	private PersonIdent id;

	public Student(PersonIdent id) {
		this.id = id;
		this.name = id.getName();
		this.email = id.getEmailAddress();
	}

	public PersonIdent getId() {
		return this.id;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + Util.LS + "Email: " + this.email + Util.LS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Student other = (Student) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}