package util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import student.Student;

public class Util {
	public static final String LS = System.lineSeparator();

	public static boolean isJavaClass(String string) {
		String[] splitString = string.split("\\.");
		
		return splitString.length == 2 && splitString[1].equals("java");
	}

	public static List<Student> getStudentsFromJson(String jsonPath) {
		List<Student> students = new ArrayList<Student>();

		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonPath));
			JSONArray jsonStudents = (JSONArray) jsonObject.get("students");

			for (int index = 0; index < jsonStudents.size(); index++) {
				JSONObject jsonStudent = (JSONObject) jsonStudents.get(index);
				String studentName = (String) jsonStudent.get("name");
				JSONArray studentAliasesJson = (JSONArray) jsonStudent.get("aliases");
				String[] studentAliases = convertToStringArray(studentAliasesJson);

				students.add(new Student(studentName, studentAliases));
			}
		} catch (Exception e) {
			System.err.printf("Error trying to open the file: %s.", e.getMessage());

		}

		return students;
	}

	private static String[] convertToStringArray(JSONArray jsonArray) {
		String[] convertedArray = new String[jsonArray.size()];

		for (int index = 0; index < jsonArray.size(); index++) {
			convertedArray[index] = (String) jsonArray.get(index);
		}

		return convertedArray;
	}
}
