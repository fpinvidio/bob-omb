package controllers;

import java.util.List;

import models.Category;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.product.create;
import views.html.product.index;


public class ProductController extends Controller {
	static Form<Product> productForm = Form.form(Product.class);
	static List<Category> categories = Category.find.all();

	public static Result index() {
		return ok(index.render("", Product.find.all()));
	}

	public static Result newProduct() {
		return ok(create.render(productForm, categories));
	}

	public static Result createProduct() {
		Form<Product> filledForm = productForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm, categories));
		} else {
			Product.create(filledForm.get());
			flash("success", "Product " + filledForm.get().name + " has been created.");
			return redirect(routes.ProductController.index());
		}
	}

}
