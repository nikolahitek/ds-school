package com.nikolahitek;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    static volatile List<Review> reviews;

    synchronized static void addReviw(Review review) {
        reviews.add(review);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        reviews = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(3000);

        while (true) {
            Socket conn = serverSocket.accept();
            Thread thread = new Thread(new ServeRunnable(conn));
            thread.start();
            thread.join();
        }
    }
}

class ServeRunnable implements Runnable {

    private Socket conn;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    ServeRunnable(Socket conn) throws IOException {
        this.conn = conn;
        in = new ObjectInputStream(conn.getInputStream());
        out = new ObjectOutputStream(conn.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String req = (String) in.readObject();

            switch (req) {
                case "TEACHER":
                    Review review = (Review) in.readObject();
                    Server.addReviw(review);
                    break;

                case "BOARD":
                    Pair<String, String> id = (Pair<String, String>) in.readObject();
                    List<Pair<String, Integer>> studentsPoints = Server.reviews.stream()
                            .filter(r -> r.courseID.equals(id.getKey()) && r.activityID.equals(id.getValue()))
                            .map(r -> new Pair<>(r.index, r.points))
                            .collect(Collectors.toList());

                    out.writeObject(studentsPoints.size());
                    out.flush();

                    for (Pair<String, Integer> sp : studentsPoints) {
                        out.writeObject(sp);
                        out.flush();
                    }
                    break;
            }

            in.close();
            out.close();
            conn.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
