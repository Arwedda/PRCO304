/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.unused;
/*
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author jkell
 */
//@ClientEndpoint
public class WebsocketListener {
/*    
    Remnants of a research into implementing a Websock Listener rather than
    constantly sending API get requests.
    
    
    public Session session = null;
    private MessageHandler messageHandler;
    
    public WebsocketListener(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            System.out.println("[Info] Error: " + e);
        }
    }
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Opening websocket connection...");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Closing websocket connection...");
        this.session = null;
    }
    
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
    
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
    
    @OnMessage
    public void onMessage(String message){
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }
    
    public static interface MessageHandler {
        public void handleMessage(String message);
    }*/
}


/*  
    Instantiation in the PriceCollector class.


    private void initialiseWebsocketListener(){
        try {
            wsListener = new WebsocketListener(new URI("wss://ws-feed.gdax.com"));
        
            wsListener.addMessageHandler(new WebsocketListener.MessageHandler() {
                @Override
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            wsListener.sendMessage("{\"type\": \"subscribe\", \"product_ids\": [\"BCH-USD\", \"BTC-USD\", \"ETH-USD\", \"LTC-USD\"], \"channels\": [\"level2\", \"heartbeat\"] }");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
    }
*/