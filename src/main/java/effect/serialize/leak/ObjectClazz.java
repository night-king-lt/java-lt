package effect.serialize.leak;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author liu.teng
 * @Date 2020/8/11 19:25
 * @Version 1.0
 */
public class ObjectClazz {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(10000);
        while (true) {
            Socket socket = server.accept();
            execute(socket);
        }
    }
    public static void execute(final Socket socket){
        new Thread(new Runnable() {
            public void run() {
                try {
                    ObjectInputStream is  = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Object obj = is.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
