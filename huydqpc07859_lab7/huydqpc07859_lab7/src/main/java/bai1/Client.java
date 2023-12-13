/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package bai1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author huyyd
 */
public class Client {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Client is Connecting....");

                try (Socket socket = new Socket("localhost", port)) {
                    System.out.println("Client connected");
                    System.out.println("Client dang lang nghe o cong " + port);

                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    while (true) {
                        Double a;
                        while (true) {
                            System.out.print("Nhap vao so thu 1: ");
                            try {
                                a = new Scanner(System.in).nextDouble();
                                break;
                            } catch (Exception ex) {
                                System.out.println("So thu 1 phai la so");
                            }
                        }
                        outputStream.writeDouble(a);
                        outputStream.flush();

                        Double b;
                        while (true) {
                            System.out.print("Nhap vao so thu 2: ");
                            try {
                                b = new Scanner(System.in).nextDouble();
                                break;
                            } catch (Exception ex) {
                                System.out.println("So thu 2 phai la so");
                            }
                        }
                        outputStream.writeDouble(b);
                        outputStream.flush();

                        System.out.println("Tong 2 so: " + inputStream.readDouble());

                        System.out.print("Nhan nut bat ki de tiep tuc, nhan n de thoat khoi chuong trinh?:  ");
                        String traloi = new Scanner(System.in).nextLine();

                        if (traloi.equalsIgnoreCase("n")) {
                            break;
                        }
                    }
                }
                System.out.println("Chuong trinh ket thuc. Server se tu tat");
                return;
            } catch (IOException ex) {
                if (ex instanceof ConnectException) {
                    System.out.println("Server o cong " + port + " chua mo, vui long cho cong khac");
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
                    System.out.println("Server da ngat ket noi, Client ngat ket noi");
                    break;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
    }
}
