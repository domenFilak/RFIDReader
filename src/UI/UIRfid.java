package UI;

import com.fazecast.jSerialComm.SerialPort;
import mainCode.Env;
import mainCode.Mode;
import mainCode.UIRfidListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UIRfid extends JFrame implements ActionListener {

    private MyPanel myPanel;
    private final String[] OPTIONS = {"Yes", "No"};

    private JLabel versionLabel, currentPortNameLabel, bannerLabel, logAreaLabel;

    private JComboBox<Mode> modeCheckBox;

    private JComboBox<Env> envCheckBox;

    private JComboBox<SerialPort> serialPortCheckBox;

    private JTextField addressField;

    private JTextArea logArea;

    private UIRfidListener uiRfidListener;
    private JPanel panelDesc, panelOptions, panelLog;
    private JScrollPane scrollPane;
    private JButton clearButton;
    private JButton soundButton;
    private final ImageIcon ICON = new ImageIcon(UIRfid.class.getResource("/icon_reader.png"));

    private Date currentDate;
    private SimpleDateFormat dateFormat;

    public UIRfid(UIRfidListener uiRfidListener){
        this.uiRfidListener = uiRfidListener;
        this.setTitle("RFID Reader");
        this.setIconImage(ICON.getImage());
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //this.setBackground(new Color(153, 255, 255));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                onExit();
            }
        });

        this.myPanel = new MyPanel();
        this.getContentPane().add(this.myPanel, BorderLayout.CENTER);

        this.bannerLabel = new JLabel("RFID Reader", SwingConstants.CENTER);
        this.bannerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        this.bannerLabel.setBackground(new Color(51, 255, 153));
        this.bannerLabel.setOpaque(true);
        this.bannerLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.bannerLabel.setBounds(0, 0, 500, 50);
        this.myPanel.add(this.bannerLabel);

        this.panelDesc = new JPanel();
        this.panelDesc.setLayout(null);
        this.panelDesc.setBounds(0, 50, 500, 100);
        this.panelDesc.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.myPanel.add(this.panelDesc);

        this.versionLabel = new JLabel();
        this.versionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.versionLabel.setBounds(5, 0, 500, 50);
        this.panelDesc.add(this.versionLabel);

        this.currentPortNameLabel = new JLabel();
        this.currentPortNameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.currentPortNameLabel.setBounds(5, 50, 500, 50);
        this.panelDesc.add(this.currentPortNameLabel);

        this.panelOptions = new JPanel();
        this.panelOptions.setLayout(null);
        this.panelOptions.setBounds(0, 150, 500, 170);
        this.panelOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.myPanel.add(this.panelOptions);

        this.serialPortCheckBox = new JComboBox<SerialPort>();
        this.serialPortCheckBox.setBounds(5, 10, 100, 30);
        this.panelOptions.add(this.serialPortCheckBox);

        this.modeCheckBox = new JComboBox<Mode>();
        this.modeCheckBox.addItem(Mode.LOGIN);
        this.modeCheckBox.addItem(Mode.SHOW);
        this.modeCheckBox.setBounds(5, 50, 100, 30);
        this.panelOptions.add(this.modeCheckBox);

        this.envCheckBox = new JComboBox<Env>();
        this.envCheckBox.addItem(Env.TEST);
        this.envCheckBox.addItem(Env.PROD);
        this.envCheckBox.setBounds(5, 90, 100, 30);
        this.panelOptions.add(this.envCheckBox);

        this.addressField = new JTextField();
        this.addressField.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.addressField.setEditable(false);
        this.addressField.setBounds(5, 130, 490, 30);
        this.panelOptions.add(this.addressField);

        this.panelLog = new JPanel();
        this.panelLog.setLayout(null);
        this.panelLog.setBounds(0, 320, 500, 220);
        this.panelLog.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.myPanel.add(this.panelLog);

        this.logAreaLabel = new JLabel("Log area:");
        this.logAreaLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.logAreaLabel.setBounds(5, 0, 250, 30);
        this.panelLog.add(this.logAreaLabel);

        this.clearButton = new JButton("Clear");
        this.clearButton.addActionListener(this);
        this.clearButton.setActionCommand("clearButton");
        this.clearButton.setFocusable(false);
        this.clearButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.clearButton.setBounds(425, 0, 70, 30);
        this.panelLog.add(this.clearButton);

        this.soundButton = new JButton("Sound");
        this.soundButton.addActionListener(this);
        this.soundButton.setActionCommand("soundButton");
        this.soundButton.setFocusable(false);
        this.soundButton.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        this.soundButton.setBounds(325, 0, 70, 30);
        this.panelLog.add(this.soundButton);

        this.logArea = new JTextArea();
        this.logArea.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.logArea.setEditable(false);

        this.scrollPane = new JScrollPane(this.logArea);
        this.scrollPane.setBounds(0, 30, 500, 190);
        this.panelLog.add(this.scrollPane);

        this.modeCheckBox.addActionListener(e -> this.uiRfidListener.modeChangedListener((Mode) this.modeCheckBox.getSelectedItem()));
        this.envCheckBox.addActionListener(e -> this.uiRfidListener.envChangedListener((Env) this.envCheckBox.getSelectedItem()));
        this.serialPortCheckBox.addActionListener(e -> this.uiRfidListener.serialPortChangedListener((SerialPort) this.serialPortCheckBox.getSelectedItem()));


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = ((JButton) e.getSource()).getActionCommand();

        switch (actionCommand) {
            case "clearButton":
                this.logArea.setText("");
                break;
            case "soundButton":
                makeSound();
                break;
            default:
                break;
        }
    }

    public void makeSound(){
        ((SerialPort) this.serialPortCheckBox.getSelectedItem()).writeBytes("A".getBytes(), "A".getBytes().length);
    }


    public void onExit(){
        int res = JOptionPane.showOptionDialog(this, "Do you really want to exit?", "RFIDReader", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, OPTIONS, OPTIONS[1]);
        if (res == 0){
            System.exit(0);
        }
    }

    public void setVersion(String version){
        this.versionLabel.setText((version != null) ? ("VERSION: " + version) : ("/"));
    }

    public void setCurrentAddress(String address){
        this.addressField.setText((address != null) ? (address) : "/");
    }

    public void setCurrentPortName(String portName){
        this.currentPortNameLabel.setText((portName != null) ? ("CURRENT PORT NAME: " + portName) : ("/"));
    }

    public void setCurrentMode(Mode mode){
        this.modeCheckBox.setSelectedItem(mode);
    }

    public void setAllSerialPorts(SerialPort[] serialPortList){
        for (SerialPort s : serialPortList){
            this.serialPortCheckBox.addItem(s);
        }
    }

    public SerialPort getSelectedSerialPort(){
        return ((SerialPort) this.serialPortCheckBox.getSelectedItem());
    }

    public void showId(String id){
        String time = "";
        this.logArea.setText(this.logArea.getText() + "\n" + (formatTime(time) + "Current Id: " + id));
        time = null;
    }

    public void setTextOnConnection(boolean initialized){
        String time = "";
        if (initialized){
            this.logArea.setText(formatTime(time) + "Connection initialized!");
        }
        else {
            this.logArea.setText(formatTime(time) + "Connection not initialized!");
        }
        time = null;
    }

    public String formatTime(String timeInString){
        timeInString = "";
        this.currentDate = new Date();
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
        timeInString += this.dateFormat.format(this.currentDate) + " | ";
        this.currentDate = null;
        this.dateFormat = null;

        return timeInString;

    }


}
