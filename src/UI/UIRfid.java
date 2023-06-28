package UI;

import mainCode.Env;
import mainCode.Mode;
import mainCode.UIRfidListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class UIRfid extends JFrame {

    private MyPanel myPanel;
    private final String[] OPTIONS = {"Yes", "No"};

    private JLabel versionLabel, currentPortNameLabel;

    private JComboBox<Mode> modeCheckBox;

    private JComboBox<Env> envCheckBox;

    private JTextField addressField;

    private JTextArea logArea;

    private UIRfidListener uiRfidListener;

    public UIRfid(UIRfidListener uiRfidListener){
        this.uiRfidListener = uiRfidListener;
        this.setTitle("RFIDReader");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                onExit();
            }
        });

        this.myPanel = new MyPanel();
        this.getContentPane().add(this.myPanel, BorderLayout.CENTER);

        this.versionLabel = new JLabel();
        this.versionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.versionLabel.setBounds(0, 0, 500, 50);
        this.myPanel.add(this.versionLabel);

        this.currentPortNameLabel = new JLabel();
        this.currentPortNameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.currentPortNameLabel.setBounds(0, 50, 500, 50);
        this.myPanel.add(this.currentPortNameLabel);


        this.modeCheckBox = new JComboBox<Mode>();
        this.modeCheckBox.addItem(Mode.LOGIN);
        this.modeCheckBox.addItem(Mode.SHOW);
        this.modeCheckBox.setBounds(0, 150, 100, 30);
        this.myPanel.add(this.modeCheckBox);

        this.envCheckBox = new JComboBox<Env>();
        this.envCheckBox.addItem(Env.TEST);
        this.envCheckBox.addItem(Env.PROD);
        this.envCheckBox.setBounds(0, 200, 100, 30);
        this.myPanel.add(this.envCheckBox);

        this.addressField = new JTextField();
        this.addressField.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.addressField.setEditable(false);
        this.addressField.setBounds(0, 250, 500, 30);
        this.myPanel.add(this.addressField);

        this.logArea = new JTextArea();
        this.logArea.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.logArea.setEditable(false);
        this.logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.logArea.setBounds(0, 300, 500, 200);
        this.myPanel.add(this.logArea);

        this.modeCheckBox.addActionListener(e -> this.uiRfidListener.modeChangedListener((Mode) this.modeCheckBox.getSelectedItem()));
        this.envCheckBox.addActionListener(e -> this.uiRfidListener.envChangedListener((Env) this.envCheckBox.getSelectedItem()));


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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

    public void showId(String id){
        this.logArea.setText(this.logArea.getText() + "\n" + ("Current Id: " + id));
    }


}
