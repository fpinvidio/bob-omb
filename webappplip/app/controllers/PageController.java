package controllers;

import models.Page;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.FetchConfig;

public class PageController extends Controller {

	public static Result getLatestPage() {
		Page page = Page.find.fetch("page_products", new FetchConfig().query())
				.fetch("page_products.product").findUnique();
		return ok(page != null ? page.toJson().toString() : "");
	}

}
