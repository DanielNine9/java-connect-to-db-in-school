/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author huyyd
 */
public class Server {

    public static void main(String[] args) {
        int port = 8888;
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Server is connecting...");
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server dang chay tren cong " + port);
                System.out.println("Dang cho client ket noi");
                Socket socket = serverSocket.accept();
                System.out.println("Setver connected");
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    double a = input.readDouble();
                    double b = input.readDouble();
                    System.out.println("So thu 1: " + a);
                    System.out.println("So thu 2 " + b);
                    double sum = a + b;
                    System.out.println("Tong: " + sum);
                    output.writeDouble(sum);
                    output.flush();
                }
            } catch (IOException ex) {
                if (ex instanceof BindException) {
                    System.out.println("Cong " + port + " da duoc su dung, vui long chon cong khac");
                    while (true) {
                        try {
                            System.out.print("Moi ban nhap cong: ");
                            port = Integer.parseInt(sc.nextLine());
                            break;
                        } catch (Exception exx) {
                            System.out.println("Port phai la so;");
                        }
                    }
                } else {
                    System.out.println("Client da ngat ket noi, server ngat ket noi");
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Ngoại lệ không mong đợi: " + ex);
            }
        }

    }
}
