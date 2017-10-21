package codeOwnership;

import java.util.ArrayList;
import java.util.List;

public class PairServer {

	private List<PairStudentArtifact> pairs;  // TODO: colocar um Map sendo a chave o nome
	
	public PairServer() {
		this.pairs =  new ArrayList<PairStudentArtifact>();
	}
	
	public String getPairsByStudentName(String nome){
		ArrayList<PairStudentArtifact> studentArtifacts = new ArrayList<PairStudentArtifact>();
		
		for (int i = 0; i < pairs.size(); i++) {
			if(pairs.get(i).student.getName().equals(nome)) {
				studentArtifacts.add(pairs.get(i));
			}
		}
		
		if(studentArtifacts.size() == 0) {
			System.out.println("Student "+ nome + "has no artifact or name not found at the project!"); 
			//TODO: Melhora esta condição, especificar se nao tem ou se o nome ta errado. 
		}
		
		String resp = "";
		for (int i = 0; i < studentArtifacts.size(); i++) {
			resp += studentArtifacts.get(i).toString() + "\n";
		}
		
		
		return resp;
	}
	
	public void addPair(PairStudentArtifact pair) {
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
