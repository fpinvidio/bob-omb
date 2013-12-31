package controllers;

import java.util.List;

import models.Role;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.create;
import views.html.user.index;

public class UserController extends Controller {
	static Form<User> userForm = Form.form(User.class);
	static List<Role> roles = Role.find.all();

	public static Result index() {
		return ok(index.render(""));
	}

	public static Result newUser() {
		return ok(create.render(userForm, roles));
	}

	public static Result createUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm, roles));
		} else {
			User.create(filledForm.get());
			flash("success", "User " + filledForm.get().name + " has been created.");
			return redirect(routes.UserController.index());
		}
	}

}
