package actors;

import java.util.ArrayList;
import java.util.List;

import models.WebSocketChannel;
import models.messages.InboundPage;
import models.messages.InboundStatus;
import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Monitor extends UntypedActor {

	public static ActorRef instance = Akka.system().actorOf(
			Props.create(Monitor.class));

	private static List<WebSocketChannel<JsonNode>> members = new ArrayList<WebSocketChannel<JsonNode>>();

	public void onReceive(Object message) {
		if (message instanceof InboundPage) {
			notifyAll(((InboundPage) message).getPageJSON());
		} else if (message instanceof InboundStatus) {
			notifyAll(((InboundStatus) message).getStatusJSON());
		} else {
			unhandled(message);
		}
	}

	public static void register(final WebSocketChannel<JsonNode> channel) {
		channel.getIn().onMessage(new Callback<JsonNode>() {
			public void invoke(JsonNode event) {
				System.out.println(event);
			}
		});

		channel.getIn().onClose(new Callback0() {
			public void invoke() {
				members.remove(channel);
				System.out.println("Clients left: " + members.size());
				System.out.println("Disconnected");
			}
		});
		members.add(channel);
	}

	private void notifyAll(String text) {
		for (WebSocketChannel<JsonNode> member : members) {
			ObjectNode event = Json.newObject();
			event.put("text", text);
			member.getOut().write(event);
		}
	}
}
