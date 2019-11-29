package com.nikolahitek;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Teacher extends Thread {

    Socket conn;
    ObjectOutputStream out;
    ObjectInputStream in;

    Teacher() throws IOException {
        conn = new Socket("localhost", 3000);
        out = new ObjectOutputStream(conn.getOutputStream());
        in = new ObjectInputStream(conn.getInputStream());
    }

    @Override
    public void run() {
        try {
            out.writeObject("TEACHER");
            out.flush();

            out.writeObject(new Review("161009", "DS", "K1", 100));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Teacher teacher = new Teacher();
        teacher.start();
    }
}
