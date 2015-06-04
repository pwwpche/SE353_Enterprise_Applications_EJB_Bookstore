/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package ChatRoom.Encoder;

import ChatRoom.Message.SystemMessage;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SystemMessageEncoder implements Encoder.Text<SystemMessage> {

    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public String encode(SystemMessage systemMessage) throws EncodeException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("type", "SystemMessage")
                .add("name", systemMessage.getName())
                .add("action", systemMessage.getAction());
        return objectBuilder.build().toString();
    }
}
