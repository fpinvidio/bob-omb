package controllers;

import static play.data.Form.form;
import models.Category;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.category.create;
import views.html.category.index;
import views.html.category.edit;
import views.html.category.show;

@Security.Authenticated(SecuredAuthenticator.class)
public class CategoryController extends Controller {
	static Form<Category> categoryForm = Form.form(Category.class);

	public static Result index() {
		return redirect(routes.CategoryController.index(0, "name", "asc", ""));
	}
	
	public static Result index(int page, String sortBy, String order, String filter) {
		return ok(
            index.render(
                Category.page(page, 5, sortBy, order, filter),
                sortBy, order, filter
            )
        );
	}
	
	public static Result show(Long id) {
		Category category = Category.find.byId(id);
		return ok(show.render(category));
	}

	public static Result newCategory() {
		return ok(create.render(categoryForm));
	}
	
	public static Result edit(Long id) {
		Category category = Category.find.byId(id);
        Form<Category> computerForm = form(Category.class).fill(
            category
        );
        return ok(
            edit.render(category, computerForm)
        );
    }
	
	public static Result update(Long id) {
		Category category = Category.find.byId(id);
        Form<Category> filledForm = form(Category.class).bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(edit.render(category, filledForm));
        }
        filledForm.get().update(id);
        flash("success", "Computer " + filledForm.get().name + " has been updated");
        return index();
    }

	public static Result createCategory() {
		Form<Category> filledForm = categoryForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm));
		} else {
			Category.create(filledForm.get());
			flash("success", "Category " + filledForm.get().name
					+ " has been created.");
			return index();
		}
	}
}
