package extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private Map<String, String> extendedType;
	private Map<String, List<String>> methodsFromClass;

	public Extractor(GitRepository repository) {
		this.repository = repository;
		this.componentClass = new ComponentClass<>();
		this.extendedType = new HashMap<>();
		this.methodsFromClass = new HashMap<>();
	}

	public void get() throws ParseException, IOException {
		this.componentClass.getAllMethods(new File("C:\\Users\\Mareana\\Desktop\\CodeOwnership\\src\\main\\java"));
		this.printMethods();
		this.printExtendTypes();
	}

	public void printMethods() {
		Set<Object> methods = this.componentClass.getMethods().keySet();
		for (Object key : methods) {
			this.methodsFromClass.put((String) key.toString(), new ArrayList<>());
			for (MethodDeclaration method : this.componentClass.getMethods().get(key)) {
				this.methodsFromClass.get(key).add(method.getDeclarationAsString(false, false, false));

			}
		}
	}

	public void printExtendTypes() {
		Map<Object, NodeList<ClassOrInterfaceType>> classes = this.componentClass.getInheritance();
		Set<Object> c = classes.keySet();
		for (Object object : c) {
			if (classes.get(object).size() > 0) {
				this.extendedType.put((String) object,
						classes.get(object).toString().substring(1, classes.get(object).toString().length() - 1));
			}
		}
		printCommonMethods();
	}

	public void printCommonMethods() {
		Set<String> classes = this.extendedType.keySet();
		for (String string : classes) {
			System.out.print(LS + "CLASS>>>" + string);
			System.out.println("   EXTENDED>>>" + this.extendedType.get(string));

			List<String> superMethods = this.methodsFromClass.get(this.extendedType.get(string));

			List<String> methods = this.methodsFromClass.get(string);

			System.out.println("Common methods beetwen " + string + " and " + this.extendedType.get(string));

			for (String superMethod : superMethods) {
				for (String method : methods) {
					if (superMethod.equals(method)) {
						System.out.println(method);
					}

				}

			}
		}
	}
}