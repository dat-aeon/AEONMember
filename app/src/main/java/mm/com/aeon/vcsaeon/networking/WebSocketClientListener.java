package mm.com.aeon.vcsaeon.networking;

import android.content.Context;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import mm.com.aeon.vcsaeon.BuildConfig;

import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.WEB_SOCKET_CONNECTION_TIMEOUT;

public class WebSocketClientListener {

    public interface SocketListiner{
        void onOpen(ServerHandshake serverHandshake);
        void onMessage(String s);
        void onClose(int i, String s, boolean b);
    }

    SocketListiner listener;
    private Context context;
    private WebSocketClient socketClient;
    private String webSocketUrl;

    public WebSocketClientListener(Context context, SocketListiner listener, String webSocketUrl) {
        this.context = context;
        this.listener = listener;
        this.webSocketUrl = webSocketUrl;
    }

    public WebSocketClient connectWebsocket() throws URISyntaxException,OutOfMemoryError{

        URI uri = new URI(webSocketUrl);

        socketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                listener.onOpen(handshake);
            }

            @Override
            public void onMessage(final String message) {
                final String messageStr = message;
                listener.onMessage(messageStr);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                setConnectionLostTimeout(WEB_SOCKET_CONNECTION_TIMEOUT);
                listener.onClose(code, reason, remote);
            }

            @Override
            public void onError(Exception ex) { }
        };
        socketClient.connect();
        return socketClient;
    }
}
