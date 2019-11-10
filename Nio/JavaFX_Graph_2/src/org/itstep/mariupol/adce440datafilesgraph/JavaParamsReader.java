package org.itstep.mariupol.adce440datafilesgraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import org.itstep.mariupol.adce440datafilesgraph.model.Channel;

//import static java.lang.System.out;
public class JavaParamsReader {

    private ArrayList<Channel> mChannelsArrayList;

    public ArrayList<Channel> getParams(String filePathString) {
        mChannelsArrayList = new ArrayList<>();
        JavaParamsReader paramsReader = new JavaParamsReader();
        int[] paramsArray = new int[66];
        getParamsJ(filePathString, paramsArray);
        //paramsArray = paramsReader.getParamsC(filePathString);
        int channelsQuantity = paramsArray[0];
        for (int i_channel = 1; i_channel <= channelsQuantity; i_channel++) {
            Channel currentChannel = new Channel(
                    paramsArray[i_channel],
                    paramsArray[i_channel + 33]);
            mChannelsArrayList.add(currentChannel);
        }

//		return paramsReader.getParamsC(filePathString);
        return mChannelsArrayList;
    }

    private void getParamsJ(String filePathString, int[] paramsArray) {
        try (@SuppressWarnings("resource") FileChannel channel
                = new FileInputStream(filePathString + ".prm").getChannel()) {
            int i_channels = 0;
            ByteBuffer buffer =
                    ByteBuffer.allocate(128 * 1024)
                            .order(ByteOrder.nativeOrder());
            while (channel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    paramsArray[i_channels] = buffer.getShort();
                    //out.println("\n" + i_channels + " = " + paramsArray[i_channels]);
                    i_channels++;
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
