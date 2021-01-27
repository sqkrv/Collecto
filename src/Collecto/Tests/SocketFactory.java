package Collecto.Tests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFactory implements Runnable {

    private Socket socket1 = null;
    private Socket socket2 = null;
    private ServerSocket serverSocket;
    private static int port;

    public SocketFactory(int port) {
        this.port = port;
        setServerSocket();
    }
    private void setServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ignored) {

        }
    }

    @Override
    public void run() {
        while (socket1 == null) {
            try {
                socket1 = new Socket(InetAddress.getByName("localhost"), port);
            } catch (IOException ignored) {
            }
        }
    }

    public Socket[] newSockets() {
        try {
            Thread thread = new Thread(this);
            thread.start();
            socket2 = serverSocket.accept();
            thread.join();
        } catch (IOException | InterruptedException ignored) {
        }
        return new Socket[]{socket1, socket2};
    }
}
