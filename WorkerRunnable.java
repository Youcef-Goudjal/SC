import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class WorkerRunnable implements Runnable {
    Socket socket;
    Queue<Socket> fifo;
    boolean SC;

    WorkerRunnable(Socket socket, Queue<Socket> fifo, boolean SC) {
        this.socket = socket;
        this.fifo = fifo;
        this.SC = SC;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 2) {
            read_msg(socket);
        }
    }

    void process_req(Socket client) {
        if (fifo.size() == 0) {
            SC = true;
            send_ok(client);
        } else {
            // add client to fifo
            fifo.add(client);
        }

    }

    void send_ok(Socket client) {
        try {
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            output.writeUTF("OK:");
            fifo.add(client);
        } catch (IOException e) {
            System.out.println("Error in sending ok to client");
            e.printStackTrace();
        }
    }

    void read_msg(Socket client) {
        DataInputStream input;
        try {
            input = new DataInputStream(client.getInputStream());
            String msg = input.readUTF();
            // System.out.println("Server Received: " + msg);
            String[] pkt = msg.split(":");
            if (pkt[0].equals("REQUEST")) {
                // process request
                System.out.println("client " + pkt[1] + " requested SC");
                process_req(client);
            } else if (pkt[0].equals("BYE")) {
                // process bye
                // System.out.println("client " + pkt[1] + " leaved SC");
                // remove client from queue
                fifo.remove(client);
                SC = false;
                Socket next = fifo.peek();
                if (next != null) {
                    send_ok(next);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
