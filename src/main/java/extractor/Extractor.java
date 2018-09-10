package extractor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import git.GitRepository;

public class Extractor {
	
	public static final String LS = System.lineSeparator();
	private GitRepository repository;
	private ComponentClass<Object> componentClass;
	
	
	public Extractor(GitRepository repository) {
		this.repository = repository;
		this.componentClass = new ComponentClass<>();
	}

	public void get() throws ParseException, IOException {
		this.componentClass.getAllMethods(new File("C:\\Users\\Mareana\\Desktop\\CodeOwnership\\src\\main\\java"));
		this.printMethods();
		this.printExtendTypes();
	}
	
	public void printMethods() {
			Set<Object> methods = this.componentClass.getMethods().keySet();
			String line = "";
			for (Object key : methods) {
				line = LS + "CLASS: "+ key + LS;
				line += LS +  "METHODS: " + LS;
				for (MethodDeclaration method : this.componentClass.getMethods().get(key)) {
					line += " --------------- " +  method.getDeclarationAsString() + LS;
				}
				System.out.println(line);
			}
	}
	
	public void printExtendTypes() {
		Map<Object, NodeList<ClassOrInterfaceType>> classes = this.componentClass.getInheritance();
		Set<Object> c = classes.keySet();
		for (Object object : c) {
			if(classes.get(object).size() > 0) {
				System.out.print("CLASS: " + object + "  ");
				System.out.println("EXTENDS: " + classes.get(object));
			}
		}
	}
	
	public void printCommonMethods() {
		
	}
	

	
}
