package controllers;

import models.WebSocketChannel;
import models.messages.InboundOrder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import actors.Monitor;
import views.html.monitor.index;

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

	public static Result orderReceived() {
		Monitor.instance.tell(new InboundOrder("Order Received"), null);
		return ok("");
	}
}
