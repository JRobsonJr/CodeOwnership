package codeOwnership;

import java.util.ArrayList;
import java.util.List;

import artifact.Artifact;
import util.Util;

public class PairRepository {

	private List<PairStudentArtifact> pairs;
	// TODO: colocar um Map sendo a chave o nome

	public PairRepository() {
		this.pairs = new ArrayList<PairStudentArtifact>();
	}

	public void addPair(PairStudentArtifact pair) {
		this.pairs.add(pair);
	}

	public void removePairs(Artifact artifact) {
		for (int i = 0; i < pairs.size(); i++) {
			if (pairs.get(i).getArtifact().getName().equals(artifact.getName())) {
				pairs.remove(i);
			}
		}
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

	public String getPairsByStudentName(String studentName) throws Exception {
		if (this.containsStudent(studentName)) {
			ArrayList<PairStudentArtifact> studentArtifacts = this.getStudentArtifacts(studentName);

			String resp = "";

			for (int i = 0; i < studentArtifacts.size(); i++) {
				resp += studentArtifacts.get(i).toString() + Util.LS;
			}

			if (studentArtifacts.size() == 0) {
				resp = "Student " + studentName + " has currently no artifacts in this project.";
			}

			return resp;
		} else {
			throw new Exception("Student not found! Maybe the student currently has no artifacts in this project.");
			// TODO Create an Exception hierarchy.
		}
	}

	@Override
	public String toString() {
		String resp = "";

		for (int i = 0; i < pairs.size(); i++) {
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
