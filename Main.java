public class Main {
    public static void main(String[] args) {
        int port = 22000;
        Server s = new Server(port);
        s.start();
        Client c1 = new Client(1, "localhost", port);
        c1.start();
        Client c2 = new Client(2, "localhost", port);
        c2.start();
        Client c3 = new Client(3, "localhost", port);
        c3.start();

    }
}
