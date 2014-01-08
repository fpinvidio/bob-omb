package models;

import play.mvc.WebSocket;

public class WebSocketChannel<A> {
	private WebSocket.In<A> in;
	private WebSocket.Out<A> out;
	

	public WebSocket.In<A> getIn() {
		return in;
	}

	public void setIn(WebSocket.In<A> in) {
		this.in = in;
	}

	public WebSocket.Out<A> getOut() {
		return out;
	}

	public void setOut(WebSocket.Out<A> out) {
		this.out = out;
	}

	public WebSocketChannel(WebSocket.In<A> in, WebSocket.Out<A> out) {
		this.in = in;
		this.out = out;
	}

}
