package com.rv.librarysockets;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

/**
 *
 * @author Vitória Pizzutti
 */
public class ClientSocket implements Closeable {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientSocket(final Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public boolean sendMessage(String message) {
        out.println(message);
        return !out.checkError();
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket: " + e.getMessage());
        }
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public boolean isOpen() {
        return !socket.isClosed();
    }
}
