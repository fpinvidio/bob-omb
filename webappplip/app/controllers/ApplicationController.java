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
	
	public static Result authenticate() {
	    Form<Login> filledForm = loginForm.bindFromRequest();
	    if (filledForm.hasErrors()) {
	        return badRequest(login.render(filledForm));
	    } else {
	        session().clear();
	        session("username", filledForm.get().username);
	        return redirect(
	            routes.ApplicationController.index()
	        );
	    }
	}

}
