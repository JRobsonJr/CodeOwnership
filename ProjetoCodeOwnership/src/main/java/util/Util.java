package util;

public class Util {
	public static final String LS = System.lineSeparator();
	
	public boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}
}
