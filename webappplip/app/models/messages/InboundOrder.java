package models.messages;

public class InboundOrder {

	private String text;

	public InboundOrder(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
