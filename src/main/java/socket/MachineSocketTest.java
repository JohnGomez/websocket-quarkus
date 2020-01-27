package socket;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/vmpay/queue")
@ApplicationScoped
public class MachineSocketTest {

    private static  Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("Olá bb <3");
        System.out.println("Usuário conectado: " + session.getId());
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        System.out.println("Mensagem recebida do usuário: " + session.getId());

        Thread.sleep(3000);
        session.getAsyncRemote().sendText("Recebi sua mensagem ;)");
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
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
    }

}
