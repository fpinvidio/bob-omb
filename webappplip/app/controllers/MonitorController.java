package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Page;
import models.TrayStatus;
import models.WebSocketChannel;
import models.messages.InboundPage;
import models.messages.InboundStatus;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
		String[] params = request().body().asFormUrlEncoded().get("page_id");
		if (params != null && params.length > 0) {
			String page_id = params[0];
			if (page_id == null) {
				return badRequest("Missing parameter [page_id]");
			} else {
				Long id = Long.parseLong(page_id);
				Page page = Page.find
						.fetch("page_products", new FetchConfig().query())
						.fetch("page_products.product").where()
						.eq("t0.id_page", id).findUnique();
				Monitor.instance.tell(new InboundPage(page), null);
				return ok("Received Page");
			}
		}
		return badRequest("Missing parameter [page_id]");
	}
	
	public static Result statusReceived() {
		String[] params = request().body().asFormUrlEncoded().get("status_id");
		if (params != null && params.length > 0) {
			String status_id = params[0];
			if (status_id == null) {
				return badRequest("Missing parameter [page_id]");
			} else {
				Long id = Long.parseLong(status_id);
				TrayStatus status = TrayStatus.find.byId(id);
				Monitor.instance.tell(new InboundStatus(status), null);
				return ok("Received Page");
			}
		}
		return badRequest("Missing parameter [status_id]");
	}

	public static Result execRequest() {

		String url = "http://127.0.0.1:9000/monitor/page";
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost request = new HttpPost(url);
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("page_id", "1"));
			request.setEntity(new UrlEncodedFormEntity(urlParameters));
			httpClient.execute(request);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return ok("");
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
