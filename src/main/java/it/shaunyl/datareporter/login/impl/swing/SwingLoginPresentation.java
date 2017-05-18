package it.shaunyl.datareporter.login.impl.swing;

import it.shaunyl.datareporter.login.ui.LoginPresentation;
import it.shaunyl.datareporter.renderer.JButtonMetro;
import it.shaunyl.datareporter.renderer.JComboBoxMetro;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class SwingLoginPresentation implements LoginPresentation {

    private JFrame frmMain;

    private JButtonMetro btnConn, btnNewConn;

    private JComboBoxMetro<String> cmbConnList;

    private JLabel lblStatus;

    private DefaultComboBoxModel<String> comboModel;

    private JPasswordField txbPwd;

    private static final Font DEFAULT_LOGIN_FONT = new Font("Tahoma", Font.PLAIN, 16);

    private static final Font DEFAULT_LOGIN_BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);

    private Action connectAction, newConnectionAction, selectConnectionAction;

    @Override
    public void bind(Action connectAction, Action newConnectionAction, Action selectConnectionAction) {
        this.connectAction = connectAction;
        this.newConnectionAction = newConnectionAction;
        this.selectConnectionAction = selectConnectionAction;
    }
    
    @Override
    public void showUp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createView();
                btnConn.setAction(connectAction);
                btnNewConn.setAction(newConnectionAction);
                cmbConnList.setAction(selectConnectionAction);
            }
        });
    }

    @Override
    public void addAllConnectionsToCombo(final List<String> connections) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cmbConnList.setAction(null);
                comboModel.removeAllElements();
                cmbConnList.setAction(selectConnectionAction);
                for (Iterator<String> it = connections.iterator(); it.hasNext();) {
                    String connection = it.next();
                    comboModel.addElement(connection);
                }
            }
        });
    }

    @Override
    public void setPassword(final String pwd) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txbPwd.setText(pwd);
            }
        });
    }

    @Override
    public void dismiss() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frmMain.dispose();
                btnConn.setAction(null);
                btnNewConn.setAction(null);
            }
        });
    }

    public String getUrl() {
        return null;
    }

    @Override
    public String getPassword() {
        return new String(txbPwd.getPassword());
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getConnectionName() {
        return (String) this.cmbConnList.getSelectedItem();
    }

    @Override
    public void notifyFailedLogin(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblStatus.setText(text);
            }
        });
    }

    public void createView() {

        JPanel pnlMain = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        pnlMain.setPreferredSize(new Dimension(550, 290));
        pnlMain.setBackground(Color.WHITE);

        JLabel lblWelcome = new JLabel("Welcome");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 34));
        pnlMain.add(lblWelcome, constraints);

        JLabel lblConnName = new JLabel("Connection: ");
        constraints.gridy++;
        constraints.insets = new Insets(25, 0, 0, 0);
        lblConnName.setFont(new Font("Calibri", Font.PLAIN, 20));
        pnlMain.add(lblConnName, constraints);

        JLabel pwd = new JLabel("Password: ");
        constraints.gridy++;
        constraints.insets = new Insets(15, 0, 0, 0);
        pwd.setFont(new Font("Calibri", Font.PLAIN, 20));
        pnlMain.add(pwd, constraints);

        cmbConnList = new JComboBoxMetro<>();
        comboModel = new DefaultComboBoxModel<>();
        cmbConnList.setModel(comboModel);
        constraints.gridx++;
        constraints.gridy--;
        cmbConnList.setPrototypeDisplayValue("FCSDB - Production DB");
        cmbConnList.setFont(DEFAULT_LOGIN_FONT);
        pnlMain.add(cmbConnList, constraints);

        txbPwd = new JPasswordField();
        constraints.gridy++;
        txbPwd.setFont(DEFAULT_LOGIN_FONT);
        txbPwd.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(221, 221, 221)), BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        pnlMain.add(txbPwd, constraints);

        btnNewConn = new JButtonMetro("New..");
        constraints.gridx += 1;
        constraints.gridy--;
        constraints.insets = new Insets(15, 47, 0, 0);
        btnNewConn.setFont(DEFAULT_LOGIN_BUTTON_FONT);
        pnlMain.add(btnNewConn, constraints);

        btnConn = new JButtonMetro("Connect");
        constraints.gridy += 2;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(15, 20, 0, 0);
        btnConn.setFont(DEFAULT_LOGIN_BUTTON_FONT);
        pnlMain.add(btnConn, constraints);

        frmMain = new JFrame("Login stage..");
        frmMain.setLayout(new BorderLayout());

        JPanel pnlStatus = new JPanel();
        pnlStatus.setBackground(new Color(248, 248, 248));
        pnlStatus.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        frmMain.add(pnlStatus, BorderLayout.SOUTH);
        pnlStatus.setPreferredSize(new Dimension(frmMain.getWidth(), 32));
        pnlStatus.setLayout(new BoxLayout(pnlStatus, BoxLayout.X_AXIS));

        lblStatus = new JLabel();
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setForeground(new Color(0, 102, 204));
        lblStatus.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
        pnlStatus.add(lblStatus);

        frmMain.setIconImage(new ImageIcon(getClass().getResource("Login.png")).getImage());
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.getContentPane().add(pnlMain);
        frmMain.setResizable(false);
        frmMain.pack();
        frmMain.setLocationRelativeTo(null);
        frmMain.setVisible(true);
    }
}