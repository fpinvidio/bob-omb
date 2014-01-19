package controllers;

import models.Page;
import models.WebSocketChannel;
import models.messages.InboundPage;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.monitor.index;
import actors.Monitor;

import com.avaje.ebean.FetchConfig;
import com.fasterxml.jackson.databind.JsonNode;

public class MonitorController extends Controller {
	public static Result index() {
		return ok(index.render(""));
	}

	public static WebSocket<JsonNode> messageWs() {
		return new WebSocket<JsonNode>() {
			public void onReady(WebSocket.In<JsonNode> in,
					WebSocket.Out<JsonNode> out) {
				Monitor.register(new WebSocketChannel<JsonNode>(in, out));
			}
		};
	}

	public static Result pageReceived() {
		JsonNode json = request().body().asJson();
		String page_id = json.findPath("page_id").textValue();
		if (page_id == null) {
			return badRequest("Missing parameter [name]");
		} else {
			Long id = Long.parseLong(page_id);
			Page page = Page.find.fetch("page_products", new FetchConfig().query()).fetch("page_products.product").where().eq("t0.id_page", id).findUnique();
			Monitor.instance.tell(new InboundPage(page), null);
			return ok("Hello ");
		}
	}
	/*
	 * @BodyParser.Of(BodyParser.Json.class) public static Result
	 * orderReceived() { JsonNode json = request().body().asJson(); String name
	 * = json.findPath("id").textValue(); ObjectMapper mapper = new
	 * ObjectMapper(); mapper.setDateFormat(new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); Order order = null; try { order
	 * = mapper.readValue(json.toString(), Order.class);
	 * Monitor.instance.tell(new InboundOrder("Order Received - " +
	 * order.client), null); } catch (JsonParseException e) {
	 * e.printStackTrace(); } catch (JsonMappingException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } if
	 * (name == null) { return badRequest("Missing parameter [name]"); } else {
	 * return ok("Hello " + json); } }
	 */
}
