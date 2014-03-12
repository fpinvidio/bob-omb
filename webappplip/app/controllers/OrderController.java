package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Order;
import models.Page;
import models.PageProduct;
import models.Product;
import models.Tray;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.order.create;
import views.html.order.index;
import views.html.order.tray;


@Security.Authenticated(SecuredAuthenticator.class)
public class OrderController extends Controller{

	public static Result index() {
		return redirect(routes.OrderController.index(0, "client", "asc", ""));
	}
	
	public static Result index(int page, String sortBy, String order, String filter) {
		return ok(
            index.render(
                Order.page(page, 5, sortBy, order, filter),
                sortBy, order, filter
            )
        );
	}
	public static Result tray(Long order_id) {
		return redirect(routes.OrderController.trays(0, "code", "asc", "", order_id));
	}
	
	public static Result trays(int page, String sortBy, String order, String filter, Long order_id) {
		Order plip_order = Order.find.byId(order_id);
		return ok(
            tray.render(
                Tray.page(page, 5, sortBy, order, filter, plip_order),
                sortBy, order, filter, plip_order
            )
        );
	}
	
	public static Result create() {
		return ok(
	            create.render(
	                Product.find.all()
	            )
	        );
	}
	
	public static Result newOrder() {
		String[] params1 = request().body().asFormUrlEncoded().get("products[]");
		String[] params2 = request().body().asFormUrlEncoded().get("quantities[]");
		String code = request().body().asFormUrlEncoded().get("code")[0];
		String client = request().body().asFormUrlEncoded().get("client")[0];
		if ((params1 != null && params1.length > 0) && (params2 != null && params2.length > 0)) {
			List<PageProduct> products = new ArrayList<PageProduct>();
			int qty = 0;
			for(int j=0; j<params2.length; j++) {
				qty += Integer.parseInt(params2[j]);
			}
			Order order = new Order();
			order.code = code;
			order.client = client;
			order.insert_date = new Date();
			order.save();
			Page page = new Page();
			page.order = order;
			page.page_number = new Long(1);
			page.product_quantity = new Long(qty);
			for (int i=0; i<params1.length; i++) {
				PageProduct pprod = new PageProduct();
				pprod.product = Product.find.byId(Long.parseLong(params1[i]));
				pprod.quantity = Long.parseLong(params2[i]);
				products.add(pprod);
			}
			page.page_products = products;
			page.save();
			return create();
		}
		return badRequest("Missing parameter");
	}
	
	
	
}
