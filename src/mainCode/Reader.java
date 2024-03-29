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


public class Reader {

    private final String VERSION = "v1.0";

    private final String PROD_URL = "https://www.google.com/";

    private final String TEST_URL = "http://localhost:8081/wms/update/";
    private final String COM_PORT_READER_NAME = "USB-SERIAL CH340";
    private SerialPort serialPort = null;
    private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private UIRfid uiRfid;
    private Mode mode = Mode.LOGIN;;
    private String url = TEST_URL;
    private  String id = null;
    private boolean isConnected = false;

    private static Reader readerInstance = null;

    private Reader(){
        setSerialPort();
        startUIRfid();
    }

    public static Reader instance(){
        if (readerInstance == null){
            readerInstance = new Reader();
        }
        return readerInstance;
    }


    private String getReaderNameWithoutCom(SerialPort serialPort){
        return ((this.serialPort != null) ? this.serialPort.getDescriptivePortName().substring(0, this.serialPort.getDescriptivePortName().length() - 7) : "/");
    }

    private SerialPort[] getSerialPortsList(){
        return SerialPort.getCommPorts();
    }

    private void setSerialPort(){
        if (getSerialPortsList().length > 0){
            this.serialPort = getSerialPortsList()[0];
        }
        else {
            this.serialPort = null;
        }
    }

    private void startUIRfid(){
        initUIRfid();
        readData();
    }

    private void readData(){

        if (this.serialPort != null){
            this.serialPort.openPort();
            this.uiRfid.setTextOnConnection(true);

            this.serialPort.addDataListener(new SerialPortDataListener() {

                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    byte[] newData = new byte[serialPort.bytesAvailable()];
                    int numRead = serialPort.readBytes(newData, newData.length);
                    char[] hexChars = new char[0];
                    String ascii = "";

                    String command = fromByteToHex(newData, hexChars);

                    String idFormat;
                    if (!command.equals("1B")){
                        idFormat = onlyIdData(command);
                        id = fromHexToAscii(idFormat, ascii);
                        String urlLink = "";

                        if (mode == Mode.LOGIN){
                            try {
                                openLink(urlLink);
                            } catch (OpenLinkException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (mode == Mode.SHOW){
                            if (id.equals("0113914EBD")){
                                alarmSound();
                                uiRfid.showId(id);
                            }
                            else {
                                uiRfid.showId(id);
                            }

                        }
                        urlLink = null;
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
            this.uiRfid.setTextOnConnection(false);
        }
    }

    private void initUIRfid(){

        this.uiRfid = new UIRfid(new UIRfidListener() {
            @Override
            public void modeChangedListener(Mode modeNew) {
                mode = modeNew;
            }

            @Override
            public void envChangedListener(Env envNew) {
                switch (envNew){
                    case PROD:
                        uiRfid.setCurrentAddress(PROD_URL);
                        url = PROD_URL;
                        break;
                    case TEST:
                        uiRfid.setCurrentAddress(TEST_URL);
                        url = TEST_URL;
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void serialPortChangedListener(SerialPort serialPortNew){
                serialPort.closePort();
                serialPort = serialPortNew;
                readData();
                uiRfid.setCurrentPortName(getReaderNameWithoutCom(serialPort));
            }

        });

        this.uiRfid.setAllSerialPorts(getSerialPortsList());
        this.uiRfid.setVersion(this.VERSION);
        this.uiRfid.setCurrentAddress(this.url);
        this.uiRfid.setCurrentPortName(getReaderNameWithoutCom(this.serialPort));
        this.uiRfid.setCurrentMode(this.mode);
    }

    private void alarmSound(){
        byte[] alarmCommand = new byte[]{0x41};

        this.serialPort.writeBytes(alarmCommand, 1);

    }

    private String fromByteToHex(byte[] bytes, char[] data) {
        data = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            data[j * 2] = HEX_ARRAY[v >>> 4];
            data[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(data);
    }

    private String fromHexToAscii(String hexValue, String ascii) {
        ascii = "";
        for (int i = 0; i < hexValue.length(); i += 2) {
            ascii += ((char) Integer.parseInt(hexValue.substring(i, i + 2), 16));
        }
        return ascii;
    }

    private String onlyIdData(String command){
        return command.substring(2, 22);
    }

    private void openLink(String url) throws OpenLinkException {
        url = "";
        url = String.format("%s%s", this.url, this.id);
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (URISyntaxException | IOException e) {
            throw new OpenLinkException(url);
        }
    }


}
