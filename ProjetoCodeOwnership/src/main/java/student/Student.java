package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

public class Student {

	private String name, email;
	private PersonIdent id;
	private List<String> createdArtifacts;
	private static final String LS = System.lineSeparator();

	public Student(PersonIdent id) {
		this.id = id;
		this.name = id.getName();
		this.email = id.getEmailAddress();
		this.createdArtifacts = new ArrayList<String>();
	}

	public String getEmail() {
		return this.email;
	}

	public void addCreatedArtifact(String artifact) {
		this.createdArtifacts.add(artifact);
	}

	@Override
	public String toString() {
		String resp = "";
		resp = "Name: " + this.name + LS + "Email: " + this.email + LS ;

		for (int i = 0; i < createdArtifacts.size(); i++) {
			resp += createdArtifacts.get(i) + LS;
		}
		
		return resp;
	}

	public PersonIdent getId() {
		return id;
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

	public void addNewArtifacts(RevCommit commit) {

	}
}