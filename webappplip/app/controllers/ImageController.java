package controllers;

import static play.data.Form.form;
import helpers.FileHandlerHelper;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Image;
import models.Position;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import views.html.image.create;
import views.html.image.edit;
import views.html.image.index;
import views.html.image.show;

@Security.Authenticated(SecuredAuthenticator.class)
public class ImageController extends Controller {
	static List<Position> positions = Position.find.all();

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
	
	public static Result show(Long id, Long product_id) {
		Image image = Image.find.byId(id);
		Product product = Product.find.byId(product_id);
		return ok(show.render(image, product));
	}

	public static Result newImage(Long product_id) {
		Product product = Product.find.byId(product_id);
		return ok(create.render(Form.form(Image.class), product, positions));
	}
	
	public static Result edit(Long id, Long product_id) {
		Image image = Image.find.byId(id);
		Product product = Product.find.byId(product_id);
        Form<Image> computerForm = form(Image.class).fill(
            image
        );
        return ok(
            edit.render(product, image, computerForm, positions)
        );
    }
	
	public static Result update(Long id, Long product_id) {
		Image image = Image.find.byId(id);
		Product product = Product.find.byId(product_id);
        Form<Image> filledForm = form(Image.class).bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(edit.render(product, image, filledForm, positions));
        }
        filledForm.get().update(id);
        flash("success", "Image " + filledForm.get().name + " has been updated");
        return show(id, product_id);
    }
	
	public static Result delete(Long id, Long product_id) {
		Image image = Image.find.byId(id);
		String name = "" + image.name;
		Product product = Product.find.byId(product_id);
		product.images.remove(image);
		product.image_number--;
		product.save();
		image.delete();
        flash("success", "Image " + name + " has been deleted");
        return product(product_id);
    }

	public static Result createImage(Long product_id) {
		Product product = Product.find.byId(product_id);
		MultipartFormData body = request().body().asMultipartFormData();
		Map<String, String[]> bodynew = body.asFormUrlEncoded();
		FilePart picture = body.getFile("picture");
		String name = bodynew.get("name")[0];
		int angle = Integer.parseInt(bodynew.get("angle")[0]);
		int face = ("off".equals(bodynew.get("angle")[0]))?0:1;
		if (picture != null && (name != null && !"".equals(name))) {
			File saved_file = FileHandlerHelper.saveFile(picture);
			Position pos = Position.find.where().eq("angle", angle).eq("face", face).findUnique();
			Image image = new Image();
			image.name = name;
			image.position = pos;
			image.trained = false;
			image.product = product;
			image.path = saved_file.getPath();
			image.save();
			product.images.add(image);
			product.image_number++;
			product.save();
			flash("success", "Image " + name + " has been created");
			return product(product_id);
		} else {
			String error = "";
			error += picture == null ? "Missing file":"";
			error += (name == null || "".equals(name))? ", Name required":"";
			flash("error", error);
			return redirect(routes.ImageController.newImage(product.id));    
		}
	}

}
