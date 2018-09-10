package extractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ComponentClass<Object> extends VoidVisitorAdapter<Object>{
	
	private Map<Object,List<MethodDeclaration>> methods;
	private Map<Object,NodeList<ClassOrInterfaceType>> inheritance;
	
	 public ComponentClass() {
		this.methods = new HashMap<Object,List<MethodDeclaration>>();
		this.inheritance = (new HashMap<Object, NodeList<ClassOrInterfaceType>>());
	}

	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
         super.visit(n, arg);
         this.getInheritance().put((Object) n.getNameAsString(), n.getExtendedTypes());
         this.methods.put((Object) n.getNameAsString(), n.getMethods());
     }
	 
	 public void getAllMethods(File projectDir) throws ParseException, IOException {
	        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
	        	Object a = (Object) path;
	            try {
	            	this.visit(JavaParser.parse(file), a);
	            } catch (IOException e) {
	                new RuntimeException(e);
	            }
	        }).explore(projectDir);
	    }

	public Map<Object,List<MethodDeclaration>> getMethods() {
		return methods;
	}

	public void setMethods(Map<Object,List<MethodDeclaration>> methods) {
		this.methods = methods;
	}

	public Map<Object,NodeList<ClassOrInterfaceType>> getInheritance() {
		return inheritance;
	}

	public void setInheritance(Map<Object,NodeList<ClassOrInterfaceType>> inheritance) {
		this.inheritance = inheritance;
	}
	
}
