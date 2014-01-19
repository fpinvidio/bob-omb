package models.messages;

import models.Page;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InboundPage {

	private Page page;

	public InboundPage(Page page) {
		this.page = page;
	}

	public Page getPage() {
		return page;
	}
	
	public String getPageJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode onode = mapper.createObjectNode();
		JsonNode jnode = Json.toJson(page);
		onode.put("type", "page");
		onode.put("object", jnode);
		return onode.toString();
	}

}
