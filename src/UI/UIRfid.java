package UI;

import mainCode.Env;
import mainCode.Mode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class UIRfid extends JFrame {

    private MyPanel myPanel;
    private final String[] OPTIONS = {"Da", "Ne"};

    private JLabel versionLabel, currentPortNameLabel, currentIdLabel;

    private JComboBox<Mode> modeCheckBox;

    private JComboBox<Env> envCheckBox;

    private JTextField addressField;

    private JTextArea logArea;

    public UIRfid(){
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

        this.versionLabel = new JLabel("Version: " + "v1.0");
        this.versionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.versionLabel.setBounds(0, 0, 500, 50);
        this.myPanel.add(this.versionLabel);

        this.currentPortNameLabel = new JLabel("Current port name: " + "USB-SERIAL CH340");
        this.currentPortNameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.currentPortNameLabel.setBounds(0, 50, 500, 50);
        this.myPanel.add(this.currentPortNameLabel);

        this.currentIdLabel = new JLabel("Current id: " + "126530");
        this.currentIdLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        this.currentIdLabel.setBounds(0, 100, 500, 50);
        this.myPanel.add(this.currentIdLabel);

        this.modeCheckBox = new JComboBox<Mode>();
        this.modeCheckBox.addItem(Mode.LOGIN);
        this.modeCheckBox.addItem(Mode.WRITE);
        this.modeCheckBox.setBounds(0, 150, 100, 30);
        this.myPanel.add(this.modeCheckBox);

        this.envCheckBox = new JComboBox<Env>();
        this.envCheckBox.addItem(Env.PROD);
        this.envCheckBox.addItem(Env.TEST);
        this.envCheckBox.setBounds(0, 200, 100, 30);
        this.myPanel.add(this.envCheckBox);

        this.addressField = new JTextField("localhost://8081");
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

}
