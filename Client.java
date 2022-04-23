import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client extends Thread {
    int id;
    Socket socket;

    DataOutputStream output;
    DataInputStream input;

    public Client(int id, String host, int port) {
        this.id = id;
        try {
            socket = new Socket(host, port);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        send_req();
        read_msg();
    }

    private void read_msg() {
        try {
            String msg = input.readUTF();
            System.out.println("Client " + id + " Received: " + msg);
            String[] pkt = msg.split(":");
            if (pkt[0].equals("OK")) {
                // System.out.println("client recived ok ");
                section_critique();
                send_bye();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // send bye
    private void send_bye() {
        try {
            output.writeUTF("BYE:" + id);
            // close all resources
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // send request to server and wait for response to end section critique
    public void send_req() {
        try {
            output.writeUTF("REQUEST:" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void section_critique() {
        try {
            System.out.println("Client " + id + ":" + socket.getLocalPort() + "  enter SC");
            Thread.sleep(5 * 1000);
            System.out.println("Client " + id + ":" + socket.getLocalPort() + "  leave SC");
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }
}
