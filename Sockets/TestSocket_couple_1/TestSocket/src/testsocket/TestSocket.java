/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Юрий
 */
public class TestSocket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Переменная серверного сокета - для прослушивания сетевых запросов
        ServerSocket serverSocket = null;
        //Переменная сокета - для получения информации от клиента
        Socket acceptSocket = null;
        //Переменные потоков ввода-вывода для чтения/записи в сеть/из сети и
        //для буферизации
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;
        //ObjectInputStream objectInputStream = null;
        BufferedReader bufferedReader = null;
        //Строка для сохранения данных от клиента
        String resultLineString = null;
        //Запускаем серверный сокет на прослушивание
        try {
            serverSocket = new ServerSocket(3000);
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Получаем объект сокет для приема данных от клиента
        try {
            acceptSocket = serverSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем поток ввода
        try {
            inputStream = acceptSocket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем поток вывода (в примере не используется)
        try {
            outputStream = acceptSocket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем буфферизированный поток ввода
        try {
            /*try {
            objectInputStream = new ObjectInputStream(inputStream);
            } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
            }*/            
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //запускаем бесконечный цикл
        for (int i = 0; i > -1; i++) {
            //На каждой итерации читаем из буфферизированного потока ввода строку 
            try {
                resultLineString = bufferedReader.readLine();
                System.out.println(i + ": " + resultLineString);
                if (resultLineString.equals("end")) {
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            acceptSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(TestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
