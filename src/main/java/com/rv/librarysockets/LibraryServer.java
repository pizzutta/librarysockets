package com.rv.librarysockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rv.librarysockets.model.Book;
import com.rv.librarysockets.model.ClientRequest;
import com.rv.librarysockets.service.BookService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;

/**
 *
 * @author Vitória Pizzutti
 */
public class LibraryServer {

    public static final int PORT = 4000;

    public static final int SEARCH = 1;
    public static final int INSERT = 2;
    public static final int UPDATE = 3;
    public static final int DELETE = 4;
    public static final int LOGOUT = 5;

    private ServerSocket serverSocket;
    private static BookService bookService;

    public static void main(String[] args) {
        try {
            //Instancia o service e já guarda as informações em memória
            bookService = new BookService();
            bookService.readJson();
            //Instancia e inicia o servidor
            LibraryServer server = new LibraryServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
        }
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta " + PORT);
        clientConnection();
    }

    private void clientConnection() throws IOException {
        try {
            while (true) {
                System.out.println("Aguardando conexão");
                ClientSocket clientSocket;

                try {
                    //Recebe conexão do cliente
                    clientSocket = new ClientSocket(serverSocket.accept());
                    System.out.println("Cliente " + clientSocket.getRemoteSocketAddress());
                } catch (SocketException e) {
                    System.err.println("Erro ao aceitar conexão do cliente. ");
                    System.err.println(e.getMessage());
                    continue;
                }
                new Thread(() -> clientRequest(clientSocket)).start();
            }
        } finally {
            stop();
        }
    }

    private void clientRequest(ClientSocket clientSocket) {
        try {
            String requestString;
            //Recebe requisição do cliente
            while ((requestString = clientSocket.getMessage()) != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ClientRequest request = objectMapper.readValue(requestString, ClientRequest.class);

                    //Se o cliente desconectar, escreve o que está armazenado em memória no arquivo JSON
                    if (request.getOperation() == LOGOUT) {
                        bookService.writeJson();
                        return;
                    }

                    String response = "";
                    switch (request.getOperation()) {
                        case SEARCH: {
                            List<Book> books = bookService.findBooksByTitle(request.getValue());
                            if (books.isEmpty()) {
                                response = "Não foram encontrados livros conforme este título.\n\n";
                            } else {
                                for (Book book : books) {
                                    response += book.toString();
                                }
                            }
                            break;
                        }
                        case INSERT: {
                            bookService.addNewBook(request.getBook());
                            response = "Novo livro adicionado.\n\n";
                            break;
                        }
                        case UPDATE: {
                            Book book = bookService.findBookByIsbn(request.getValue());
                            if (book != null) {
                                book.setTitle(request.getBook().getTitle());
                                book.setGenre(request.getBook().getGenre());
                                book.setEdition(request.getBook().getEdition());
                                book.setYear(request.getBook().getYear());
                                book.setPublishingCompany(request.getBook().getPublishingCompany());
                                book.getAuthors().removeAll(book.getAuthors());
                                book.getAuthors().addAll(request.getBook().getAuthors());
                                response = "Livro editado.\n\n";
                            } else {
                                response = "Não foi encontrado livro com o seguinte ISBN: " + request.getValue();
                            }
                            break;
                        }
                        case DELETE: {
                            boolean deleted = bookService.deleteBookByIsbn(request.getValue());
                            response = (deleted) ? "Livro excluído."
                                    : "Não foi encontrado livro com o seguinte ISBN: " + request.getValue();
                            break;
                        }
                    }
                    clientSocket.sendMessage(response);
                } catch (JsonProcessingException ex) {
                    System.out.println("Erro ao ler requisição: " + ex.getMessage());
                }
            }
        } finally {
            clientSocket.close();
        }
    }

    private void stop() {
        try {
            System.out.println("Finalizando servidor");
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket do servidor: " + e.getMessage());
        }
    }

}
