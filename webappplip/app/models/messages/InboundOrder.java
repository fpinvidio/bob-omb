package models.messages;

import models.Order;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InboundOrder {

	private Order order;

	public InboundOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
	
	public String getOrderJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode onode = mapper.createObjectNode();
		JsonNode jnode = Json.toJson(order);
		onode.put("type", "order");
		onode.put("object", jnode);
		return onode.toString();
	}

}
