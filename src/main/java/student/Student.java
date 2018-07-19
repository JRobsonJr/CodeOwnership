package student;

import java.util.Arrays;

import util.Util;

public class Student {

	private String name;
	private String[] aliases;

	public Student(String name, String[] aliases) {
		this.name = name;
		this.aliases = aliases;
	}

	public String getName() {
		return this.name;
	}

	public boolean hasAlias(String name) {
		for (String alias : aliases) {
			if (name.equals(alias)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + Util.LS + "Aliases: " + Arrays.toString(this.aliases);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		return other.getName().equals(this.name);
	}

}