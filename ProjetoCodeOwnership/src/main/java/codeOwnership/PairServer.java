package codeOwnership;

import java.util.ArrayList;
import java.util.List;

public class PairServer {

	private List<PairStudentClass> pairs;
	
	public PairServer() {
		this.pairs =  new ArrayList<PairStudentClass>();
	}
	
	public ArrayList<PairStudentClass> getPairsByStudentName(String nome){
		ArrayList<PairStudentClass> studentArtifacts = new ArrayList<PairStudentClass>();
		
		for (int i = 0; i < pairs.size(); i++) {
			if(pairs.get(i).student.getName().equals(nome)) {
				studentArtifacts.add(pairs.get(i));
			}
		}
		
		if(studentArtifacts.size() == 0) {
			System.out.println("Student "+ nome + "has no artifact or name not found at the project!"); 
			//TODO: Melhora esta condição, especificar se nao tem ou se o nome ta errado. 
		}
		
		return studentArtifacts;
	}
	
	public void addPair(PairStudentClass pair) {
		this.pairs.add(pair);
	}

	@Override
	public String toString() {
		String resp = "";
		
		for (int i = 0; i < pairs.size(); i++) {
			resp += pairs.get(i).toString() +"\n";
			
		}
		
		return resp;
	}
	
	
	
	
	
	
	
	
	
}