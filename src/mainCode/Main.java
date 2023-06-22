package mainCode;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;


public class Main {

    private static final String COM_PORT_READER_NAME = "USB-SERIAL CH340";
    private static SerialPort serialPort = null;

    public static void main(String[] args){

        Main.setSerialPort();

        if (Main.serialPort != null){
            Main.serialPort.openPort();
            System.out.println("Port initialized!");

            Main.serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    byte[] newData = new byte[Main.serialPort.bytesAvailable()];
                    int numRead = Main.serialPort.readBytes(newData, newData.length);
                    System.out.println("Read " + numRead + " bytes.");
                    System.out.println("Card info: " + fromByteToChar(newData));
                }
            });

        }
        else {
            System.out.println("Port not available!");
        }

    }

    public static String getReaderNameWithoutCom(String nameWithCom){
        return nameWithCom.substring(0, nameWithCom.length() - 7);
    }

    public static void setSerialPort(){
        SerialPort[] serialPortList = SerialPort.getCommPorts();

        for (SerialPort s : serialPortList){
            if (getReaderNameWithoutCom(s.getDescriptivePortName()).equals(Main.COM_PORT_READER_NAME)){
                Main.serialPort = s;
            }
        }

    }

    public static String fromByteToChar(byte[] bytes){
        String res = "";
        for (byte b : bytes){
            res += (char)b;
        }
        return res;
    }
    //add to extract only data of id
    public static byte[] onlyIdData(byte[] bytes){
        byte[] impData = new byte[bytes.length - 6];
        int counter = 0;
        for (int i = 2; i <= bytes.length - 4; i++){
            impData[counter] = bytes[i];
            counter++;
        }
        return impData;
    }


}
