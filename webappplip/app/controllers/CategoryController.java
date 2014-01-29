package controllers;

import models.Category;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.category.create;
import views.html.category.index;

public class CategoryController extends Controller {
	static Form<Category> categoryForm = Form.form(Category.class);

	@Security.Authenticated(SecuredAuthenticator.class)
	public static Result index() {
		
		if (SecuredAuthenticator.isAdmin(request()))
			return ok(index.render(""));
		return redirect(routes.ApplicationController.login());
	}

	public static Result newCategory() {
		return ok(create.render(categoryForm));
	}

	public static Result createCategory() {
		Form<Category> filledForm = categoryForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm));
		} else {
			Category.create(filledForm.get());
			flash("success", "Category " + filledForm.get().name
					+ " has been created.");
			return redirect(routes.CategoryController.index());
		}
	}
}
