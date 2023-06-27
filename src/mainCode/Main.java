package mainCode;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;


public class Main {

    private static final String VERSION = "v1.0";

    private static final String PROD_URL = "localhost:8081";

    private static final String TEST_URL = "localhost:8081";
    private static final String COM_PORT_READER_NAME = "USB-SERIAL CH340";
    private static SerialPort serialPort = null;

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    public static void main(String[] args){

        Main.setSerialPort();

        if (Main.serialPort != null){
            Main.serialPort.openPort();
            System.out.println("Port initialized!");

            Main.serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    byte[] newData = new byte[Main.serialPort.bytesAvailable()];
                    int numRead = Main.serialPort.readBytes(newData, newData.length);
                    char[] hexChars = new char[0];
                    String ascii = "";

                    String command = fromByteToHex(newData, hexChars);

                    String id;
                    if (!command.equals("1B")){
                        id = onlyIdData(command);
                        System.out.println(fromHexToAscii(id, ascii));
                    }





                    newData = null;
                    hexChars = null;
                    ascii = null;
                    command = null;
                    id = null;
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

    public static String fromByteToHex(byte[] bytes, char[] data) {
        data = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            data[j * 2] = HEX_ARRAY[v >>> 4];
            data[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(data);
    }

    private static String fromHexToAscii(String hexValue, String ascii) {
        ascii = "";
        for (int i = 0; i < hexValue.length(); i += 2) {
            ascii += ((char) Integer.parseInt(hexValue.substring(i, i + 2), 16));
        }
        return ascii;
    }

    public static String onlyIdData(String command){
        return command.substring(2, 22);
    }


}
