package controllers;

import static play.data.Form.form;
import helpers.FileHandlerHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.Category;
import models.Image;
import models.Position;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import views.html.product.create;
import views.html.product.edit;
import views.html.product.index;
import views.html.product.show;

@Security.Authenticated(SecuredAuthenticator.class)
public class ProductController extends Controller {
	static Form<Product> productForm = Form.form(Product.class);
	static List<Category> categories = Category.find.all();
	
	public static Result index() {
		return redirect(routes.ProductController.index(0, "name", "asc", ""));
	}
	
	public static Result index(int page, String sortBy, String order, String filter) {
		return ok(
            index.render(
                Product.page(page, 5, sortBy, order, filter),
                sortBy, order, filter
            )
        );
	}
	
	public static Result show(Long id) {
		Product product = Product.find.byId(id);
		return ok(show.render(product));
	}

	public static Result newProduct() {
		return ok(create.render(productForm, categories));
	}
	
	public static Result edit(Long id) {
		Product product = Product.find.byId(id);
        Form<Product> computerForm = form(Product.class).fill(
        	product
        );
        return ok(
            edit.render(product, computerForm, categories)
        );
    }
	
	public static Result update(Long id) {
		Product product = Product.find.byId(id);
        Form<Product> filledForm = form(Product.class).bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(edit.render(product, filledForm, categories));
        }
        filledForm.get().update(id);
        flash("success", "Product " + filledForm.get().name + " has been updated");
        return index();
    }

	public static Result createProduct() {
		// DynamicForm requestData = Form.form().bindFromRequest();
		Form<Product> filledForm = productForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm, categories));
		} else {
			Product to_save = filledForm.get();
			Product.create(to_save);
			String[] files = request().body().asFormUrlEncoded().get("files[]");
			if (files != null && files.length > 0) {
				to_save.images = generateProductImages(files);
				to_save.save();
			}
			flash("success", "Product " + filledForm.get().name
					+ " has been created.");
			return index();
		}
	}

	private static List<Image> generateProductImages(String[] files_path) {
		List<Image> images = new ArrayList<Image>();
		for (String path : files_path) {
			Image image = new Image();
			image.name = "FakeName";
			image.path = path;
			image.position = Position.find.byId(new Long(1));
			images.add(image);
		}
		return images;
	}

	public static Result uploadImages() {
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> files = body.getFiles();
		List<File> saved_files = new ArrayList<File>();
		for (FilePart picture : files) {
			// FilePart picture = body.getFile("files");
			if (picture != null) {
				File saved_file = FileHandlerHelper.saveFile(picture);
				saved_files.add(saved_file);
			} else {
				flash("error", "Missing file");
				return index();
			}
		}
		return ok(FileHandlerHelper.generateResponse(saved_files));
	}

}
