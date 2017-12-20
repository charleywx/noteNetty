package gps.dispatcher.main;

import gps.dispatcher.client.Client;
import gps.dispatcher.config.Config;
import gps.dispatcher.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Server server = context.getBean(Server.class);
        server.start();
    
        Client client = context.getBean(Client.class);
        client.start();
    }

}
