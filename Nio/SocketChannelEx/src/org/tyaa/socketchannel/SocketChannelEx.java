package org.tyaa.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 *
 * @author Юрий
 */
public class SocketChannelEx
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerSocketChannel srvSocketChannel = null;
        SocketChannel socketChannel = null;
        ByteBuffer byteBuffer = null;
        Selector selector = null;
        //Создаем селектор (мультиплексор)
        try {
            selector = Selector.open();
        } catch (IOException ex) {
            System.out.println("Error0");
        }
        //Создаем канал серверного сокета (можно создать на основе
        //экземпляра серверного сокета)
        try {
            srvSocketChannel = ServerSocketChannel.open();
        } catch (IOException ex) {
            System.out.println("Error1");
        }
        //Указываем порт прослушивания
        try {
            srvSocketChannel.bind(new InetSocketAddress(3000));
        } catch (IOException ex) {
            System.out.println("Error2");
        }
        //Настраиваем на неблокирующий режим
        try {
            srvSocketChannel.configureBlocking(false);
        } catch (IOException ex) {
            System.out.println("Error3");
        }
        //Регистрируемся на селекторе для приема запроса
        try {
            srvSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException ex) {
            System.out.println("Error4");
        }
        //Создаем нио-буфер        
        byteBuffer = ByteBuffer.allocate(1024 * 1024);
        //Бесконечный цикл ожидания запросов
        while (true) {
            //узнаем количество ожидающих клиент-сокетов
            int clientSocketsCount = 0;
            try {
                clientSocketsCount = selector.select();
            } catch (IOException ex) {
                System.out.println("Error5");
            }
            //очередь пуста - ничего не делаем, начинаем новую проверку
            if(clientSocketsCount == 0)
            {
                continue;
            }
            //иначе получаем набор ключей селектора
            Iterator<SelectionKey> selectedKeys =
                    selector.selectedKeys().iterator();
            //Пока есть ключи в коллекции, получаем по каждому из них
            //соответствующий канал
            while (selectedKeys.hasNext()) {                
                SelectionKey currentSelectionKey = selectedKeys.next();
                try {
                    //когда канал - именно наш,
                    //зарегистрированный на ожидание запросов
                    if (currentSelectionKey.channel() == srvSocketChannel) {
                        try {
                            //начинаем ожидание (прослушивание)
                            socketChannel = srvSocketChannel.accept();
                            //получаем запрос из очереди, настраиваем
                            //канал работы с клиентом неблокирующим,
                            //и регистрируем его на чтение данных от клиента
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } catch (IOException ex) {
                            System.out.println("Error5");
                        }
                    }
                    //иначе - соединение уже есть, читаем из него в буфер
                    else
                    {
                        try {
                            socketChannel.read(byteBuffer);
                        } catch (IOException ex) {
                            System.out.println("Error6");
                        }
                        //переставляем в буфере позицию после чтения
                        byteBuffer.flip();
                        //выводим в консоль принятую часть данных
                        System.out.println("Current data: " + new String(byteBuffer.array()
                                , byteBuffer.position()
                                , byteBuffer.remaining()));
                        byteBuffer.clear();
                    }
                }
                finally
                {
                    //удаляем набор ключей
                    selectedKeys.remove();
                }
            }
            
            
            
        }
    }
    
}
