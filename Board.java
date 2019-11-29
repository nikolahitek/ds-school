package com.nikolahitek;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Board extends Thread {

    Socket conn;
    ObjectOutputStream out;
    ObjectInputStream in;

    Board() throws IOException {
        conn = new Socket("localhost", 3000);
        out = new ObjectOutputStream(conn.getOutputStream());
        in = new ObjectInputStream(conn.getInputStream());
    }

    @Override
    public void run() {
        try {
            out.writeObject("BOARD");
            out.flush();

            out.writeObject(new Pair<>("DS", "K1"));
            out.flush();

            int n = (Integer) in.readObject();

            for(int i = 0; i < n; i++) {
                Pair<String, Integer> studentPoints = (Pair<String, Integer>) in.readObject();
                System.out.println(studentPoints);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        board.start();
    }
}
