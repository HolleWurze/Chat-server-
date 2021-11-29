package services;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer implements Observable{
    public static final int PORT = 8080;
    public volatile static List<Observer> clients = new ArrayList<>();

    @SneakyThrows
    public void start() {
        System.out.println("==== SERVER IS STARTED ====");
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true){
            Socket socket = serverSocket.accept();

            if(socket != null) {
                new Thread(new ClientRunnable(socket, this)).start();
            }
        }
    }

    @Override
    public void addObserver(Observer o) {
        clients.add(o);
    }

    @Override
    public void deleteObserver(Observer o) {
        clients.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer client : clients) {
            client.notifyObserver(message);
        }
    }
}
