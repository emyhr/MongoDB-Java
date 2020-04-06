package util;

public class Utils {

	public static boolean isNumber(String X) {
		try {
			Integer.parseInt(X);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
