package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Category;
import models.Image;
import models.Position;
import models.Product;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.product.create;
import views.html.product.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	public static Result newProduct() {
		return ok(create.render(productForm, categories));
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
				File saved_file = saveFile(picture);
				saved_files.add(saved_file);
			} else {
				flash("error", "Missing file");
				return index();
			}
		}
		return ok(generateResponse(saved_files));
	}

	public static JsonNode generateResponse(List<File> files) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode onode = mapper.createObjectNode();
		ArrayNode anode = mapper.createArrayNode();
		for (File file : files) {
			ObjectNode obj = mapper.createObjectNode();
			if (files != null) {
				obj.put("name", file.getName());
				obj.put("size", file.getTotalSpace());
				obj.put("url", file.getPath());
				anode.add(obj);
			} else {
				obj.put("error", "Filetype not allowed");
				anode.add(obj);
			}
		}
		onode.put("files", anode);
		return onode;
	}

	public static File saveFile(FilePart picture) {
		String fileName = picture.getFilename();
		//String contentType = picture.getContentType();
		File file = picture.getFile();
		String path = getDirectoryPath(fileName);
		File destination = new File(path);
		try {
			FileUtils.forceMkdir(destination);
			destination = new File(path + fileName);
			FileUtils.moveFile(file, destination);
		} catch (FileExistsException e) {
			int counter = 1;
			while (true) {
				try {
					destination = getNewFileName(path, fileName, counter);
					FileUtils.moveFile(file, destination);
					break;
				} catch (FileExistsException f) {
					counter++;
				} catch (IOException f) {
					e.printStackTrace();
					destination = null;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			destination = null;
		}
		return destination;
	}

	public static File getNewFileName(String path, String fileName, int counter) {
		final int lastPeriodPos = fileName.lastIndexOf('.');
		if (lastPeriodPos <= 0) {
			return null;
		} else {
			String renamed_path = path + fileName.substring(0, lastPeriodPos)
					+ counter
					+ fileName.substring(lastPeriodPos, fileName.length());
			File renamed = new File(renamed_path);
			return renamed;
		}
	}

	public static String getDirectoryPath(String filename) {
		int hashcode = filename.hashCode();
		int mask = 255;
		int firstDir = hashcode & mask;
		int secondDir = (hashcode >> 8) & mask;
		StringBuilder path = new StringBuilder(Play.application().path()
				+ File.separator + "public/uploads/");
		path.append(String.format("%03d", firstDir));
		path.append(File.separator);
		path.append(String.format("%03d", secondDir));
		path.append(File.separator);
		// path.append(filename);

		return path.toString();
	}
}
