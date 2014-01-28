package helpers;

import org.mindrot.jbcrypt.BCrypt;

import exceptions.EmptyPasswordException;


public class HashHelper {

	public static boolean checkPassword(String candidate,
			String encryptedPassword) {
		if (candidate == null) {
			return false;
		}
		if (encryptedPassword == null) {
			return false;
		}
		return BCrypt.checkpw(candidate, encryptedPassword);
	}

	public static String createPassword(String clearString) throws EmptyPasswordException {
		if (clearString == null) {
			throw new EmptyPasswordException();
		}
		return BCrypt.hashpw(clearString, BCrypt.gensalt());
	}

}
