package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Login;
import models.Order;
import models.Page;
import models.Tray;
import models.TrayStatus;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ApplicationController extends Controller {
	static Form<Login> loginForm = Form.form(Login.class);

	public static Result index() {
		int x = -500;
		Calendar cal = GregorianCalendar.getInstance();
		Calendar newcal = GregorianCalendar.getInstance();
		cal.add( Calendar.DAY_OF_YEAR, x);
		newcal.add(Calendar.DAY_OF_YEAR, -1);
		newcal.set(Calendar.HOUR_OF_DAY, 23);
		newcal.set(Calendar.MINUTE, 59);
		newcal.set(Calendar.SECOND, 59);
		Date now = new Date();
		Date days_ago = cal.getTime();
		Date yesterday = newcal.getTime();
		List<Tray> invalid_trays = Tray.find.fetch("tray_status").fetch("tray_status.status").where().eq("t1.id_status", "9").findList();
		int valid = Tray.find.all().size() - invalid_trays.size();
		int analyzed_trays = Tray.find.all().size();
		//List<Order> orders = Order.find.fetch("pages").fetch("pages.tray").fetch("pages.tray.tray_status").fetch("pages.tray.tray_status.status").where().between("insert_date", days_ago, now).findList();
		List<Order> orders = Order.find.where().where().between("insert_date", days_ago, now).findList();
		List<Order> today_orders = Order.find.fetch("pages").fetch("pages.tray").where().between("insert_date", yesterday, now).findList();
		int today_trays = 0;
		for (Order order : today_orders) {
			today_trays += order.pages.size();
		}
		return ok(index.render("", invalid_trays, valid, buildOrderJson(orders), analyzed_trays, orders.size(), today_trays));
	}

	public static Result login() {
		return ok(login.render(loginForm));
	}

	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.ApplicationController.login());
	}

	public static Result authenticate() {
		Form<Login> filledForm = loginForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(login.render(filledForm));
		} else {
			String username = filledForm.get().username;
			session().clear();
			session("username", username);
			return redirect(routes.ApplicationController.index());
		}
	}
	
	private static String buildOrderJson(List<Order> orders) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode anode = mapper.createArrayNode();
		for (Order order : orders) {
			List<Page> pages = order.getPages();
			ObjectNode onode = mapper.createObjectNode();
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = order.getInsert_date();        
			String reportDate = df.format(date);
			onode.put("date", reportDate);
			int count = 0;
			for (Page page : pages) {
				if (page.getTray() != null) {
					List<TrayStatus> statuses = page.getTray().getTray_status();
					for (TrayStatus status : statuses) {
						if (status.getStatus().getId() == 9) {
							count ++;
						}
					}
				}
			}
			onode.put("count", count);
			anode.add(onode);
		}
		return anode.toString();
	}

}
