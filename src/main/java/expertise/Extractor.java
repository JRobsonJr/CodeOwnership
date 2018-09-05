package expertise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import git.GitRepository;
import util.ComponentClass;

public class Extractor {
	
	private GitRepository repository;
	private ComponentClass<Object> ComponentClass;
	private Map<String,List<String>> methods;
	
	public Extractor(GitRepository repository) {
		this.repository = repository;
		this.ComponentClass = new ComponentClass<>();
		this.methods = new HashMap<>();
	}
	

	
}
