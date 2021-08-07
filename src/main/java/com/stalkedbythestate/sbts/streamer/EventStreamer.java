package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.eventlib.*;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventStreamer extends TextWebSocketHandler {
    private static final Logger logger = Logger.getLogger(EventStreamer.class);
    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();
    private Map<WebSocketSession, SocketEventListener> sessionMap = new ConcurrentHashMap<WebSocketSession, SocketEventListener>();
    private FreakApi freak = null;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        SocketEventListener listener = null;

        if (!sessionMap.containsKey(session)) {
            listener = new SocketEventListener(session);
            sessionMap.put(session, listener);
        } else
            listener = sessionMap.get(session);

        if (freak == null)
            freak = Freak.getInstance();

        String subscribeString = message.getPayload();

        Pattern p = Pattern.compile("^subscribe\\s+(\\S.*)\\s*$");
        Matcher m = p.matcher(subscribeString);
        if (m.find()) {
            freak.subscribeEvents(listener, m.group(1));
        } else {
            if (subscribeString.matches("^subscribe\\s*$"))
                freak.subscribeEvents(listener, "*");
        }
    }


    final class SocketEventListener implements EventListener {
        private final LinkedBlockingQueue<Notification> webSocketQueue = new LinkedBlockingQueue<Notification>();
        private final FreakApi freak = Freak.getInstance();
        private Thread handlerThread = null;

        public LinkedBlockingQueue<Notification> getWebSocketQueue() {
            return webSocketQueue;
        }

        @Override
        public void onEvent(Notification event) {
            if (webSocketQueue != null)
                webSocketQueue.add(event);
        }

        public SocketEventListener(WebSocketSession session) {
            handlerThread = new Thread() {

                @Override
                public void run() {
                    try {
                        while (true) {
                            Notification notification = webSocketQueue.take();
                            Event event = notification.getEvent();
                            if (notification.isClosing())
                                return;
                            String jsonString = "{\"eventName\":\""
                                    + ((Nameable) event).getEventName()
                                    + "\",\"eventType\":\""
                                    + EventType.websocketName(event.getEventType())
                                    + "\"" + ",\"eventTime\":"
                                    + event.getEventTime();

                            if (event instanceof ContainsClientEventTime) {
                                jsonString = jsonString
                                        + ",\"clientEventTime\":"
                                        + ((ContainsClientEventTime) event)
                                        .getClientEventTime();
                            }

                            if (event instanceof ContainsPacket) {
                                jsonString = jsonString
                                        + ",\"packet\":\""
                                        + ((ContainsPacket) event)
                                        .getPacketString() + "\"";
                            }

                            session.sendMessage(new TextMessage(CharBuffer.wrap(jsonString + "}")));

                        }
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    } finally {
                    }
                }
            };

            handlerThread.setName("websocket-handler-" + handlerThread.getId());
            handlerThread.start();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (sessionMap.containsKey(session)) {
            SocketEventListener listener = sessionMap.get(session);
            freak.unsubscribeEvents(listener);
            sessionMap.remove(session);
        }
    }

}
