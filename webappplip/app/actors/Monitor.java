package actors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;
import play.libs.Akka;
import play.libs.Comet;
import play.libs.F.Callback0;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Monitor extends UntypedActor {
    
    public static ActorRef instance = Akka.system().actorOf(Props.create(Monitor.class));
    
    // Send a TICK message every 100 millis
//    static {
//        Akka.system().scheduler().schedule(
//            Duration.Zero(),
//            Duration.create(12200, MILLISECONDS),
//            instance, "TICK",  Akka.system().dispatcher(),
//            /* sender */ null
//        );
//    }    
    List<Comet> sockets = new ArrayList<Comet>();
    
    public void onReceive(Object message) {

        // Handle connections
        if(message instanceof Comet) {
            final Comet cometSocket = (Comet)message;
            
            if(sockets.contains(cometSocket)) {
                
                // Brower is disconnected
                sockets.remove(cometSocket);
                Logger.info("Browser disconnected (" + sockets.size() + " browsers currently connected)");
                
            } else {
                
                // Register disconnected callback 
                cometSocket.onDisconnected(new Callback0() {
                    public void invoke() {
                        getContext().self().tell(cometSocket, null);
                    }
                });
                
                // New browser connected
                sockets.add(cometSocket);
                Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");
                
            }
            
        } 
        
        // Tick, send time to all connected browsers
        if("TICK".equals(message)) {
            
            // Send the current time to all comet sockets
            List<Comet> shallowCopy = new ArrayList<Comet>(sockets); //prevent ConcurrentModificationException
            for(Comet cometSocket: shallowCopy) {
                //cometSocket.sendMessage(dateFormat.format(new Date()));
            	cometSocket.sendMessage("");
            }
            
        }

    }
    
}
