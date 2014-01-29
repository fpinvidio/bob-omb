package controllers;

import models.Login;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;

public class ApplicationController extends Controller {
	static Form<Login> loginForm = Form.form(Login.class);

	public static Result index() {
		return ok(index.render(""));
	}

	public static Result login() {
		return ok(login.render(loginForm));
	}

	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.ApplicationController.login());
	}

	public static Result authenticate() {
		Form<Login> filledForm = loginForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(login.render(filledForm));
		} else {
			String username = filledForm.get().username;
			session().clear();
			session("username", username);
			return redirect(routes.ApplicationController.index());
		}
	}

}
