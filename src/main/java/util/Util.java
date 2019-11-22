package util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.opencsv.CSVReader;

import pair.PairStudentArtifact;
import student.Student;

public class Util {

	public static final String LS = System.lineSeparator();
	private static final String TSV_SEPARATOR = "\t";

	public static int countLines(String filename) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filename));	
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} catch(IOException ioe) {
			return -1;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					
				}
			}
		}
	}

	public static boolean isJavaClass(String string) {
		String[] splitString = string.split("\\.");

		return splitString.length == 2 && splitString[1].equals("java");
	}

	public static List<Student> getStudentsFromJson(String jsonPath) {
		
		File f = new File(jsonPath + File.separator + "students.json");
		List<Student> students = new ArrayList<Student>();

		if (f.exists()) {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = null;
			try {
				jsonObject = (JSONObject) parser.parse(new FileReader(jsonPath));
			} catch (Exception e) {
				System.err.println("Erro ao ler arquivo de estudantes.");
				e.printStackTrace();
			}
			JSONArray jsonStudents = (JSONArray) jsonObject.get("students");
			
			for (int index = 0; index < jsonStudents.size(); index++) {
				JSONObject jsonStudent = (JSONObject) jsonStudents.get(index);
				String studentName = (String) jsonStudent.get("name");
				JSONArray studentAliasesJson = (JSONArray) jsonStudent.get("aliases");
				String[] studentAliases = convertToStringArray(studentAliasesJson);
				
				students.add(new Student(studentName, studentAliases));
			}
		}

		f = new File(jsonPath + File.separator + "students.csv");
		if (f.exists()) {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(f));
				String[] line;
				while ((line = reader.readNext()) != null) {
					String studentName = line[0];
					String[] studentAliases = new String[line.length - 1];
					System.arraycopy(line, 1, studentAliases, 0, line.length - 1);
					students.add(new Student(studentName, studentAliases));
				}
			} catch (IOException e) {
				System.err.println("Erro ao ler arquivo de estudantes.");
				e.printStackTrace();
			}
		}
		return students;
	}

	public static boolean generateTSV(List<PairStudentArtifact> pairs) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("outputs/analysis-result.tsv"), "utf-8"));

			writer.write("STUDENT NAME" + TSV_SEPARATOR +
					"CLASS PATH" + TSV_SEPARATOR +
					"OWNERSHIP PERCENTAGE" + TSV_SEPARATOR +
					"CLASS SIZE" + TSV_SEPARATOR +
					"EXPERTISE LIST" + LS);

			DecimalFormat df = new DecimalFormat("#.#");

			for (PairStudentArtifact pair : pairs) {
				String line = pair.getStudentName() + TSV_SEPARATOR +
						pair.getArtifactName() + TSV_SEPARATOR +
						df.format(pair.ownershipPercentage) + TSV_SEPARATOR +
						pair.getArtifactSize() + TSV_SEPARATOR +
						Arrays.toString(pair.getArtifact().getExpertiseArray()) + LS;
				writer.write(line);
			}

			writer.close();

			return true;
		} catch (IOException ex) {
			System.err.println("Error: " + ex.getMessage());
			return false;
		}
	}

		private static String[] convertToStringArray(JSONArray jsonArray) {
		String[] convertedArray = new String[jsonArray.size()];

		for (int index = 0; index < jsonArray.size(); index++) {
			convertedArray[index] = (String) jsonArray.get(index);
		}

		return convertedArray;
	}

}
