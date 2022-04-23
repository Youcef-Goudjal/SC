import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Server extends Thread {
    ServerSocket serverSocket;
    static boolean SC = false;
    static Queue<Socket> fifo;
    protected boolean isStopped = false;

    public Server(int port) {
        fifo = new LinkedList<Socket>();
        openServerSocket(port);
    }

    @Override
    public void run() {
        while (!isStopped()) {
            Socket client = null;
            try {
                client = this.serverSocket.accept();
                System.out.println("Connection accepted :" + client.getPort());
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(
                    new WorkerRunnable(client, fifo, SC)).start();
        }

        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    private void openServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }

    public synchronized void stopServer() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

}
