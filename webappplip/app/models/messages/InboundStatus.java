package models.messages;

import models.TrayStatus;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InboundStatus {
	private TrayStatus status;

	public InboundStatus(TrayStatus status) {
		this.status = status;
	}

	public TrayStatus getStatus() {
		return status;
	}
	
	public String getStatusJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode onode = mapper.createObjectNode();
		JsonNode jnode = Json.toJson(status);
		onode.put("type", "status");
		onode.put("object", jnode);
		return onode.toString();
	}
}
