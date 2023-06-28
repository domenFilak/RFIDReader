package mainCode;

import UI.UIRfid;
import Exception.OpenLinkException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {

    private static final String VERSION = "v1.0";

    private static final String PROD_URL = "https://www.google.com/";

    private static final String TEST_URL = "localhost:8081/test/";
    private static final String COM_PORT_READER_NAME = "USB-SERIAL CH340";
    private static SerialPort serialPort;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static UIRfid uiRfid;
    private static Mode mode;
    private static String url;
    private static String id;


    public static void main(String[] args){

        Main.setSerialPort();

        if (Main.serialPort != null){
            startUIRfid();

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

    public static void startUIRfid(){
        initUIRfid();

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
                    Main.id = fromHexToAscii(id, ascii);
                    if (Main.mode == Mode.LOGIN){
                        try {
                            openLink();
                        } catch (OpenLinkException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (Main.mode == Mode.SHOW){
                        Main.uiRfid.showId(Main.id);
                    }

                }

                newData = null;
                hexChars = null;
                ascii = null;
                command = null;
                id = null;
            }
        });
    }

    public static void initUIRfid(){
        Main.uiRfid = new UIRfid(new UIRfidListener() {
            @Override
            public void modeChangedListener(Mode mode) {
                Main.mode = mode;
            }

            @Override
            public void envChangedListener(Env env) {
                switch (env){
                    case PROD:
                        Main.uiRfid.setCurrentAddress(PROD_URL);
                        Main.url = PROD_URL;
                        break;
                    case TEST:
                        Main.uiRfid.setCurrentAddress(TEST_URL);
                        Main.url = TEST_URL;
                        break;
                    default:
                        break;
                }
            }

        });
        Main.uiRfid.setVersion(Main.VERSION);
        Main.uiRfid.setCurrentAddress(Main.url);
        Main.uiRfid.setCurrentPortName(getReaderNameWithoutCom(Main.serialPort.getDescriptivePortName()));
        Main.uiRfid.setCurrentMode(Main.mode);
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

    public static void openLink() throws OpenLinkException {
        String url = String.format("%s%s", Main.url, Main.id);
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (URISyntaxException | IOException e) {
            throw new OpenLinkException(url);
        }
    }

    static {
        Main.mode = Mode.LOGIN;
        Main.url = TEST_URL;
        Main.serialPort = null;
        Main.id = null;
    }


}
