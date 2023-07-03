package mainCode;

import com.fazecast.jSerialComm.SerialPort;

public interface UIRfidListener {

    public void modeChangedListener(final Mode mode);

    public void envChangedListener(final Env env);

    public void serialPortChangedListener(final SerialPort serialPort);

}
