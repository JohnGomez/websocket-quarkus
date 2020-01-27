package socket;
import io.quarkus.scheduler.Scheduled;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class WebSocketController {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        System.out.println("Sessao do usuário que entrou: "+session.getId());
        //broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, Session session) throws IOException {
        System.out.println("Sessao que enviou a mensagem: "+session.getId());
        session.getAsyncRemote().sendText("Vai tomar no cu arrombado da porra");
        broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    @Scheduled(every = "10s")
    public void sendMessage() {
        Session session = (Session) sessions.values().toArray()[0];
        session.getAsyncRemote().sendText("Só a primeira aplicação conectada recebe essa mensagem");
        System.out.println("Quantidade de usuário conectados: "+sessions.size());
    }


}