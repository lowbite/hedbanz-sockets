import com.hedbanz.sockets.transfer.UserToRoomDto;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

import static com.hedbanz.sockets.constant.SocketEvents.JOINED_USER_EVENT;
import static com.hedbanz.sockets.constant.SocketEvents.JOIN_ROOM_EVENT;

@SpringBootTest
public class SocketsTest {
    private Socket socket;
    private int RECONNECTION_ATTEMPT = 10;
    private long CONNECTION_TIMEOUT = 30000;

    @Before
    public void init() {
        try {
            socket = IO.socket("http://localhost:9092/game");
            socket.connect();
            socket.on(JOINED_USER_EVENT, objects -> {
                JSONObject object = (JSONObject) objects[0];
                System.out.println(object.toString());
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void joinToRoomTest() {
        if(socket.connected()) {
            socket.emit(JOIN_ROOM_EVENT, new UserToRoomDto(4L, 186L));
        }
    }

}
