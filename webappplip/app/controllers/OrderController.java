package controllers;

import models.Order;
import models.Tray;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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
                Tray.page(page, 5, sortBy, order, filter),
                sortBy, order, filter, plip_order
            )
        );
	}
	
	
	
}
