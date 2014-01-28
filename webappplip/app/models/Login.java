package models;

import helpers.HashHelper;
import play.data.validation.Constraints.Required;

public class Login {

	@Required
	public String username;
	@Required
	public String password;

	public String validate() {
		String result_message = "Invalid Username or Password";
		User user = User.authenticate(username);
		if (user != null) {
			if (HashHelper.checkPassword(password, user.password)) {
				result_message = "Successfully logged in";
			}
		}
		return result_message;
	}

}
