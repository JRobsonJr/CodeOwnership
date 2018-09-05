package pair;

import java.util.ArrayList;
import java.util.List;

import exception.StudentNotFoundException;
import util.Util;

public class PairRepository {

	private List<PairStudentArtifact> pairs;

	public PairRepository() {
		this.pairs = new ArrayList<PairStudentArtifact>();
	}

	public List<PairStudentArtifact> getPairsByArtifactName(String artifactName) {
		List<PairStudentArtifact> pairs = new ArrayList<PairStudentArtifact>();

		for (PairStudentArtifact pair : this.pairs) {
			if (pair.getArtifactName().equals(artifactName)) {
				pairs.add(pair);
			}
		}

		return pairs;
	}

	public String getStudentContributionInfo(String studentName) throws StudentNotFoundException {
		if (!this.containsStudent(studentName)) {
			throw new StudentNotFoundException("Student has currently no artifacts in this project.");
		}

		List<PairStudentArtifact> studentArtifacts = this.getStudentArtifacts(studentName);
		String resp = studentName + "'s contributions:" + Util.LS;

		for (int i = 0; i < studentArtifacts.size(); i++) {
			resp += "=> " + studentArtifacts.get(i).toString() + Util.LS + Util.LS;
		}

		return resp;
	}

	public List<PairStudentArtifact> getPairs() {
		return pairs;
	}

	public void setPairs(List<PairStudentArtifact> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String toString() {
		String resp = "";
		
		Object[] a = this.pairs.toArray();
		System.out.println(a[30]);
		a.toString();

		for (int i = 0; i < pairs.size(); i++) {
			System.out.println(i);
			System.out.println(">>>>" +pairs.get(i));
			
			resp += pairs.get(i).toString() + Util.LS + Util.LS;
		}

		return resp;
	}

	private ArrayList<PairStudentArtifact> getStudentArtifacts(String studentName) {
		ArrayList<PairStudentArtifact> studentArtifacts = new ArrayList<PairStudentArtifact>();

		for (int i = 0; i < pairs.size(); i++) {
			if (pairs.get(i).getStudentName().equals(studentName)) {
				studentArtifacts.add(pairs.get(i));
			}
		}

		return studentArtifacts;
	}

	private boolean containsStudent(String studentName) {
		for (PairStudentArtifact pair : this.pairs) {
			if (pair.getStudentName().equals(studentName)) {
				return true;
			}
		}

		return false;
	}

}
