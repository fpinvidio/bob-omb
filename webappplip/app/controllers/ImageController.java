package controllers;

import static play.data.Form.form;
import models.Category;
import models.Image;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.category.create;
import views.html.category.edit;
import views.html.image.index;
import views.html.category.show;

@Security.Authenticated(SecuredAuthenticator.class)
public class ImageController extends Controller {
	static Form<Category> categoryForm = Form.form(Category.class);

	public static Result index() {
		return redirect(routes.ImageController.index(0, "name", "asc", "", 1));
	}
	
	public static Result product(Long product_id) {
		return redirect(routes.ImageController.index(0, "name", "asc", "", product_id));
	}
	
	public static Result index(int page, String sortBy, String order, String filter, Long product_id) {
		Product product = Product.find.byId(product_id);
		return ok(
            index.render(
                Image.page(page, 5, sortBy, order, filter, product),
                sortBy, order, filter, product
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
        flash("success", "Category " + filledForm.get().name + " has been updated");
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
