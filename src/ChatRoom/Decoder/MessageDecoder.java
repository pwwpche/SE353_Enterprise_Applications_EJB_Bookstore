/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package ChatRoom.Decoder;

import ChatRoom.Message.Message;
import ChatRoom.Message.SystemMessage;
import ChatRoom.Message.TextMessage;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* Decode a JSON message into a JoinMessage or a ChatMessage.
 * For example, the incoming message:
 * {"type":"chat","name":"Peter","target":"Duke","message":"How are you?"}
 * is decoded as (new ChatMessage("Peter", "Duke", "How are you?"))
 */
public class MessageDecoder implements Decoder.Text<Message> {
    /* Stores the name-value pairs from a JSON message as a Map */
    private Map<String,String> messageMap;

    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    /* Create a new Message object if the message can be decoded */
    @Override
    public Message decode(String string) throws DecodeException {
        Message msg = null;
        if (willDecode(string)) {
            String s = messageMap.get("type");
            if (s.equals("SystemMessage")) {
                msg = new SystemMessage(messageMap.get("name"), messageMap.get("action"));
            } else if (s.equals("TextMessage")) {
                msg = new TextMessage(messageMap.get("name"),messageMap.get("message"));
            }
        } else {
            throw new DecodeException(string, "[Message] Can't decode.");
        }
        return msg;
    }

    /* Decode a JSON message into a Map and check if it contains
     * all the required fields according to its type. */
    @Override
    public boolean willDecode(String string) {
        messageMap = new HashMap<String, String>();
        JsonParser parser = Json.createParser(new StringReader(string));
        while (parser.hasNext()) {
            if (parser.next() == JsonParser.Event.KEY_NAME) {
                String key = parser.getString();
                parser.next();
                String value = parser.getString();
                messageMap.put(key, value);
            }
        }

        Set keys = messageMap.keySet();
        if (keys.contains("type")) {
            if(messageMap.get("type").equals("SystemMessage") ){
                if (keys.contains("name") && keys.contains("action"))
                    return true;
            }else if(messageMap.get("type").equals("TextMessage")){
                if(keys.contains("name") && keys.contains("message")){
                    return true;
                }
            }
        }
        return true;
    }
}
