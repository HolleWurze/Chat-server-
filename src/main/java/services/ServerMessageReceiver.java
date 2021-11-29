package services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@AllArgsConstructor
public class ServerMessageReceiver {
    private  final BufferedReader reader;

    public ServerMessageReceiver(InputStream inputStream){
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }
    @SneakyThrows
    public String readMessage(){
        return reader.readLine();
    }
}
