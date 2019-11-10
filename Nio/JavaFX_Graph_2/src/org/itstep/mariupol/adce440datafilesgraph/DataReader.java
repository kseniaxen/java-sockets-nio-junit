package org.itstep.mariupol.adce440datafilesgraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.itstep.mariupol.adce440datafilesgraph.model.Channel;

public class DataReader {
    
    

    private static final String PATH = "D:/Temp/10f.dat";

    private ArrayList<Channel> mChannelsArrayList;

//	static {
//		System.load("D:/Temp/GetData.dll");
//	}
    public void getData(String filePathString, ArrayList<Channel> channelsArrayList) {

        mChannelsArrayList = channelsArrayList;
        int channelsQuantity = mChannelsArrayList.size();

        try (@SuppressWarnings("resource") FileChannel channel = new FileInputStream(filePathString + ".dat").getChannel()) {
            int i_channels = 0;
            ByteBuffer buffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.nativeOrder());
            while (channel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    mChannelsArrayList.get(i_channels).addDataItem(buffer.getShort() * 10D / 8192D);
                    if (i_channels == channelsQuantity - 1) {
                        i_channels = 0;
                    } else {
                        i_channels++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
