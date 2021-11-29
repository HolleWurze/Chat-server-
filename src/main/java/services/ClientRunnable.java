package services;

import dao.UserDao;
import dao.UserDaoImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor //аннотация только final полей
public class ClientRunnable implements Runnable, Observer {
    private final Socket clientSocket;
    private final MyServer server;
    private User client;
    private final UserDao dao = new UserDaoImpl();

    @SneakyThrows
    @Override
    public void run() {
        server.addObserver(this);
        BufferedReader readerFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        String messageFromUser = "";
        while (!clientSocket.isClosed() && (messageFromUser = readerFromUser.readLine()) != null) {
            if (messageFromUser.contains("Registration ")) {
                registration(messageFromUser);
            } else if (messageFromUser.contains("Authorization ")) {
                authorization(messageFromUser);
            } else {
                break;
            }
        }
        if (!clientSocket.isClosed()) {
            do {
                System.out.println(messageFromUser);
                server.notifyObservers(messageFromUser);
            } while ((messageFromUser = readerFromUser.readLine()) != null);
        }
    }

    @SneakyThrows
    private void authorization(String messageFromUser) {
        String loginFromClient = messageFromUser.split(" ")[1];
        String passwordFromClient = messageFromUser.split(" ")[2];

        User userFromDao;
        if ((userFromDao = dao.findByName(loginFromClient)) != null) {
            if (userFromDao.getPassword().equals(passwordFromClient)) {
                client = userFromDao;
                notifyObserver("Authorization successfully");
                System.out.println("Authorization for " + client.getName() + " successful");
            } else {
                System.out.println("Authorization for " + loginFromClient + " failed");
                notifyObserver("Authorization failed: wrong password");
                server.deleteObserver(this);
                clientSocket.close();
            }
        } else {
            System.out.println("Authorization for " + loginFromClient + " failed");
            notifyObserver("Authorization failed: wrong name");
            server.deleteObserver(this);
            clientSocket.close();
        }
    }

    @SneakyThrows
    private void registration(String messageFromUser) {
        System.out.println("Reg");
        if (dao.findByName(messageFromUser.split(" ")[1]) != null) {
            System.out.println("Registration for " + messageFromUser.split(" ")[1] + " failed");
            notifyObserver("Registration failed: wrong name");
            server.deleteObserver(this);
            clientSocket.close();
        } else {
            client = new User(messageFromUser.split(" ")[1], messageFromUser.split(" ")[2]);
            System.out.println("Registration for " + client.getName() + " success");
            notifyObserver("Registration for " + client.getName() + " success");
            dao.createUser(client);
        }
    }

    @SneakyThrows
    @Override
    public void notifyObserver(String message) {
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
        if (client != null) {
            printWriter.println(message);
            printWriter.flush();
        }
    }
}
