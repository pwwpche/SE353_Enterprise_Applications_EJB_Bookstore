/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package ChatRoom.Endpoint;

import ChatRoom.Decoder.MessageDecoder;
import ChatRoom.Encoder.SystemMessageEncoder;
import ChatRoom.Encoder.TextMessageEncoder;
import ChatRoom.Message.Message;
import ChatRoom.Message.SystemMessage;
import ChatRoom.Message.TextMessage;
import Servlets.Filters.XssRequestWrapper;
import org.owasp.validator.html.*;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Websocket endpoint */
@ServerEndpoint(
        value = "/chat",
        decoders = {MessageDecoder.class},
        encoders = {TextMessageEncoder.class, SystemMessageEncoder.class}
)
public class ChatEndpoint {
    private static final Logger logger = Logger.getLogger("ChatEndpoint");

    @OnOpen
    public void openConnection(Session session) {
        logger.log(Level.INFO, "Connection opened.");
        logger.log(Level.INFO, "new session id is " + session.getId());
        logger.log(Level.INFO, "Open sessions :" + session.getOpenSessions().size());
    }

    @OnMessage
    public void message(final Session session, Message msg) {
        System.out.println("On Message");

        if (msg instanceof SystemMessage) {
            /* Add the new user and notify everybody */
            SystemMessage jmsg = (SystemMessage) msg;
            session.getUserProperties().put("name", jmsg.getName());
            session.getUserProperties().put("active", "true");
            sendAll(session, new SystemMessage(jmsg.getName(), "active"));
        } else if (msg instanceof TextMessage) {
            /* Forward the message to everybody */
            final TextMessage textMsg = (TextMessage) msg;
            textMsg.setMessage(performXssFilter(((TextMessage) msg).getMessage()));
            sendAll(session, textMsg);
        }

    }

    @OnClose
    public void closedConnection(Session session) {
        /* Notify everybody */
        System.out.println("closed connection");

        session.getUserProperties().put("active", "false");
        if (session.getUserProperties().containsKey("name")) {
            String name = session.getUserProperties().get("name").toString();
            sendAll(session, new SystemMessage(name, "inactive"));
        }

        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        logger.log(Level.INFO, "Connection error ({0})", t.toString());
        String name = session.getUserProperties().get("name").toString();
        sendAll(session, new SystemMessage(name, "inactive"));
    }

    /* Forward a message to all connected clients
     * The endpoint figures what encoder to use based on the message type */
    public void sendAll(Session session, Object msg) {

        System.out.println("sendAll");
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(msg);
                    logger.log(Level.INFO, "Sent: {0}", msg.toString());
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    public List<String> getUserList(Session session) {
        System.out.println("getUserList");
        List<String> users = new ArrayList<>();
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                if (s.getUserProperties().get("active").toString().equals("true")) {
                    users.add(s.getUserProperties().get("name").toString());
                }
            }
        }
        return users;
    }
    private String performXssFilter(String dirtyHTML){
        AntiSamy antiSamy = new AntiSamy();
        try {
            String path = XssRequestWrapper.class.getClassLoader().getResource("antisamy-ebay-1.4.4.xml").getFile();
            path = path.replace("%20", " ");
            Policy policy = Policy.getInstance(path);
            final CleanResults cr = antiSamy.scan(dirtyHTML, policy);
            return cr.getCleanHTML();
        } catch (ScanException | NullPointerException | PolicyException e) {
            e.printStackTrace();
        }
        return dirtyHTML;
    }

}
