package controllers;

import play.libs.Comet;
import play.mvc.Controller;
import play.mvc.Result;
import actors.Monitor;
import akka.actor.ActorRef;

public class MonitorController extends Controller {
	
	final static ActorRef monitor = Monitor.instance;
	
	public static Result index() {
		return ok(views.html.monitor.index.render(""));
	}
	
	public static Result liveFeed() {
        return ok(new Comet("parent.productEntered") {  
            public void onConnected() {
               monitor.tell(this, null); 
            } 
        });
    }
	
	public static Result orderReceived() {
		monitor.tell("TICK", null);
		return ok("");
	}
}
