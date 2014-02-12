package controllers;

import static play.data.Form.form;
import helpers.HashHelper;

import java.util.List;

import exceptions.EmptyPasswordException;
import models.Role;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.user.create;
import views.html.user.edit;
import views.html.user.index;
import views.html.user.show;

@Security.Authenticated(SecuredAuthenticator.class)
public class UserController extends Controller {
	static Form<User> userForm = Form.form(User.class);
	static List<Role> roles = Role.find.all();

	public static Result index() {
		return redirect(routes.UserController.index(0, "name", "asc", ""));
	}
	
	public static Result index(int page, String sortBy, String order, String filter) {
		return ok(
            index.render(
                User.page(page, 5, sortBy, order, filter),
                sortBy, order, filter
            )
        );
	}
	
	public static Result show(Long id) {
		User user = User.find.byId(id);
		return ok(show.render(user));
	}

	public static Result newUser() {
		return ok(create.render(userForm, roles));
	}
	
	public static Result edit(Long id) {
		User user = User.find.byId(id);
        Form<User> computerForm = form(User.class).fill(
            user
        );
        return ok(
            edit.render(user, computerForm, roles)
        );
    }
	
	public static Result update(Long id) {
        Form<User> filledForm = form(User.class).bindFromRequest();
        User user = User.find.byId(id);
        if(filledForm.hasErrors()) {
        	System.out.print(filledForm.errors().toString());
            return badRequest(edit.render(user, filledForm, roles));
        }
        User updated = filledForm.get();
        user.name = updated.name;
        user.last_name = updated.last_name;
        user.username = updated.username;
        user.role = updated.role;
        user.update();
        flash("success", "User " + filledForm.get().name + " has been updated");
        return index();
    }

	public static Result createUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm, roles));
		} else {
			User.create(filledForm.get());
			flash("success", "User " + filledForm.get().name + " has been created.");
			return index();
		}
	}

}
