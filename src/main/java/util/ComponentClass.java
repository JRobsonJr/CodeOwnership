package util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

public class ComponentClass<Object> extends VoidVisitorAdapter<Object>{
	
	public List<MethodDeclaration> methods;
 	
	 @Override
     public void visit(ClassOrInterfaceDeclaration n, Object arg) {
         super.visit(n, arg);
         this.methods = n.getMethods();
     }
	 
	 public void getAllMethods(File projectDir) throws ParseException {
	        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
	            System.out.println(path);
	            try {
	            	this.visit(JavaParser.parse(file), null);
	            } catch (IOException e) {
	                new RuntimeException(e);
	            }
	        }).explore(projectDir);
	    }
	
}
