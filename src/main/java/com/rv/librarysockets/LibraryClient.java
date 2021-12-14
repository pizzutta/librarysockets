package com.rv.librarysockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rv.librarysockets.model.Book;
import com.rv.librarysockets.model.ClientRequest;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Vitória Pizzutti
 */
public class LibraryClient implements Runnable {

    private final String SERVER_ADRESS = "127.0.0.1";

    private ClientSocket clientSocket;
    private Scanner scanner;

    public LibraryClient() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try {
            //Instancia e inicia cliente
            LibraryClient client = new LibraryClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o cliente: " + ex.getMessage());
        }
    }

    public void start() throws IOException {
        Socket socket = new Socket(SERVER_ADRESS, LibraryServer.PORT);
        clientSocket = new ClientSocket(socket);
        System.out.println("Conectado ao servidor em " + SERVER_ADRESS + ":" + LibraryServer.PORT + "\n\n");

        System.out.println("Seja bem-vindo a esta Biblioteca!\n"
                + "Você pode realizar as seguintes operações:\n"
                + "1- Buscar livros pelo título\n"
                + "2- Adicionar novo livro\n"
                + "3- Editar livro (utilize o ISBN para selecioná-lo)\n"
                + "4- Excluir livro (utilize o ISBN para selecioná-lo)\n"
                + "5- Sair\n\n");

        new Thread(this).start();
        requestLoop();
    }

    private void requestLoop() throws IOException {
        int operation;
        //Espera que o cliente informe a operação até que ele deseje sair
        do {
            ClientRequest request = new ClientRequest();
            //Solicita operação
            System.out.println("Digite o número da operação que você quer executar:");
            operation = Integer.parseInt(scanner.nextLine());
            //Realiza ação conforme operação
            switch (operation) {
                case LibraryServer.SEARCH: {
                    System.out.println("Digite o nome do livro que você quer buscar:");
                    String value = scanner.nextLine();
                    request.setValue(value);
                    break;
                }
                case LibraryServer.INSERT: {
                    System.out.println("Digite as informações do livro que você quer adicionar");
                    Book book = getBookInformations(false);
                    request.setBook(book);
                    break;
                }
                case LibraryServer.UPDATE: {
                    System.out.println("Digite o ISBN do livro que você quer editar:");
                    String value = scanner.nextLine();
                    request.setValue(value);
                    System.out.println("Digite as novas informações do livro que você quer editar");
                    Book book = getBookInformations(true);
                    book.setIsbn(value);
                    request.setBook(book);
                    break;
                }
                case LibraryServer.DELETE: {
                    System.out.println("Digite o ISBN do livro que você quer excluir:");
                    String value = scanner.nextLine();
                    request.setValue(value);
                    break;
                }
            }
            request.setOperation(operation);
            ObjectMapper objectMapper = new ObjectMapper();
            //Envia requisição pro servidor
            clientSocket.sendMessage(objectMapper.writeValueAsString(request));
        } while (operation != LibraryServer.LOGOUT);
        clientSocket.close();
    }

    private Book getBookInformations(boolean isEditing) {
        Book book = new Book();
        System.out.println("Título:");
        book.setTitle(scanner.nextLine());
        if (!isEditing) {
            System.out.println("ISBN:");
            book.setIsbn(scanner.nextLine());
        }
        System.out.println("Gênero:");
        book.setGenre(scanner.nextLine());
        System.out.println("Edição:");
        book.setEdition(Integer.parseInt(scanner.nextLine()));
        System.out.println("Ano:");
        book.setYear(Integer.parseInt(scanner.nextLine()));
        System.out.println("Editora:");
        book.setPublishingCompany(scanner.nextLine());
        System.out.println("Autores (separe por /):");
        book.getAuthors().addAll(Arrays.asList(scanner.nextLine().split("/")));
        return book;
    }

    @Override
    public void run() {
        String response;
        while ((response = clientSocket.getMessage()) != null) {
            System.out.println(response);
        }
    }

}
