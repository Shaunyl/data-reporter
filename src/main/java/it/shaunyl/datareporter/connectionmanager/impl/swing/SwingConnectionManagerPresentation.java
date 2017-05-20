package it.shaunyl.datareporter.connectionmanager.impl.swing;

import it.shaunyl.datareporter.connectionmanager.ConnectionModel;
import it.shaunyl.datareporter.connectionmanager.ui.ConnectionManagerPresentation;
import it.shaunyl.datareporter.renderer.JButtonMetro;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class SwingConnectionManagerPresentation implements ConnectionManagerPresentation {

    private JDialog mainDialog;
    private JList lstConns;
    private JMenuItem lstMenuItm;
    private JPopupMenu lstMenuPpp;
    private JTextField txbHost, txbSID, txbUser, txbPwd, txbConnName;
    private JSpinner spnPort;
    private JLabel lblStatus;
    private JButtonMetro btnSave;
    private DefaultListModel lstDfModel;
    private Action saveConnectionAction, closeDialogAction, selectItemAction, removeConnectionAction;

    public void bind(Action saveConnectionAction, Action closeDialogAction, Action selectItemAction, Action removeConnectionAction) {
        this.saveConnectionAction = saveConnectionAction;
        this.closeDialogAction = closeDialogAction;
        this.selectItemAction = selectItemAction;
        this.removeConnectionAction = removeConnectionAction;
    }

    public void showUp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createView();
            }
        });
    }

    public void addAllConnectionsToList(final java.util.List<String> connections) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (Iterator<String> it = connections.iterator(); it.hasNext();) {
                    String connection = it.next();
                    lstDfModel.addElement(connection);
                }
            }
        });
    }

    public void dismiss() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainDialog.dispose();
                btnSave.setAction(null);
                lstMenuItm.setAction(null);
            }
        });
    }

    public void removeConnectionFromList(final String connectionName) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lstDfModel.removeElement(connectionName);
            }
        });
    }

    public void loadConnectionFieldsIntoTextFields(final ConnectionModel connectionModel) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txbHost.setText(connectionModel.getHost());
                txbSID.setText(connectionModel.getSid());
                spnPort.setValue(connectionModel.getPort());
                txbUser.setText(connectionModel.getUsername());
                txbPwd.setText(connectionModel.getPassword());
                txbConnName.setText(connectionModel.getName());
            }
        });
    }

    public String getHost() {
        return txbHost.getText();
    }

    public String getSID() {
        return txbSID.getText();
    }

    public String getPassword() {
        return txbPwd.getText();
    }

    public String getUsername() {
        return txbUser.getText();
    }

    public int getPort() {
        return (Integer) spnPort.getValue();
    }

    public String getConnectionName() {
        return txbConnName.getText();
    }

    public void notifyStatus(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lblStatus.setText(text);
            }
        });
    }

    public void addConnectionToList(final String connectionName) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lstDfModel.addElement(connectionName);
            }
        });
    }

    public String getConnectionCurrentlySelected() {
        return (String) lstConns.getSelectedValue();
    }

    public void createView() {
        JPanel pnlMain = new JPanel();
        pnlMain.setBackground(new Color(247, 247, 247));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 35));

        JPanel pnlAllConns = new JPanel();
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(lowerEtched);//, "", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 16), Color.DARK_GRAY);
        pnlAllConns.setBorder(title);
        pnlAllConns.setBackground(Color.WHITE);

        JPanel pnlConnProps = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        pnlConnProps.setBackground(new Color(247, 247, 247));

        JLabel lblTitle = new JLabel("Specify following to connect to Oracle server:      ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        lblTitle.setForeground(Color.DARK_GRAY);
        constraints.insets = new Insets(25, 0, 0, 0);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        pnlConnProps.add(lblTitle, constraints);

        JLabel lblHost = new JLabel("Host:");
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(30, 20, 0, 0);
        lblHost.setFont(new Font("Calibri", Font.PLAIN, 18));
        pnlConnProps.add(lblHost, constraints);

        txbHost = new JTextField();
        constraints.gridwidth = 3;
        constraints.gridx += 1;
        txbHost.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlConnProps.add(txbHost, constraints);

        JLabel lblSID = new JLabel("SID:");
        constraints.insets = new Insets(22, 20, 0, 0);
        constraints.gridx -= 1;
        constraints.gridy++;
        constraints.gridwidth = 1;
        lblSID.setFont(new Font("Calibri", Font.PLAIN, 18));
        pnlConnProps.add(lblSID, constraints);

        txbSID = new JTextField();
        constraints.gridx += 1;
        txbSID.setPreferredSize(new Dimension(120, 23));
        txbSID.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlConnProps.add(txbSID, constraints);

        JLabel lblPort = new JLabel("Port:");
        constraints.gridx += 1;
        lblPort.setFont(new Font("Calibri", Font.PLAIN, 18));
        pnlConnProps.add(lblPort, constraints);

        spnPort = new JSpinner();
        constraints.gridx += 1;
        spnPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlConnProps.add(spnPort, constraints);

        JLabel lblUser = new JLabel("User:");
        constraints.gridy++;
        constraints.gridx -= 3;
        constraints.gridwidth = 1;
        lblUser.setFont(new Font("Calibri", Font.PLAIN, 18));
        pnlConnProps.add(lblUser, constraints);

        txbUser = new JTextField();
        constraints.gridx += 1;
        constraints.gridwidth = 3;
        txbUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlConnProps.add(txbUser, constraints);

        JLabel lblPwd = new JLabel("Password:");
        constraints.gridy++;
        constraints.gridx -= 1;
        constraints.gridwidth = 1;
        lblPwd.setFont(new Font("Calibri", Font.PLAIN, 18));
        pnlConnProps.add(lblPwd, constraints);

        txbPwd = new JPasswordField();
        constraints.gridx += 1;
        constraints.gridwidth = 3;
        txbPwd.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlConnProps.add(txbPwd, constraints);

        JLabel lblConname = new JLabel("Name: ");
        constraints.gridx -= 1;
        constraints.gridy += 1;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(48, 20, 0, 0);
        lblConname.setFont(new Font("Calibri", Font.BOLD, 18));
        pnlConnProps.add(lblConname, constraints);

        txbConnName = new JTextField();
        constraints.gridx += 1;
        constraints.gridwidth = 3;
        txbConnName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txbConnName.getDocument().addDocumentListener(null);
        pnlConnProps.add(txbConnName, constraints);

        btnSave = new JButtonMetro("Save");
        constraints.gridy += 1;
        constraints.gridx += 2;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10, 49, 0, 0);
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnSave.setAction(saveConnectionAction); // Meglio del listener perchè può essere abilitàta o meno dal controllore..
        pnlConnProps.add(btnSave, constraints);

        lstMenuPpp = new JPopupMenu();
        lstMenuItm = new JMenuItem("Remove");
        lstMenuItm.setAction(removeConnectionAction);
        lstMenuPpp.add(lstMenuItm);

        lstDfModel = new DefaultListModel();
        lstConns = new JList(lstDfModel);
        lstConns.setPreferredSize(new Dimension(230, 322));
        lstConns.setFont(new Font("Calibri", Font.PLAIN, 18));
        lstConns.setCellRenderer(new FileListCellRenderer());
        lstConns.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                int selIdx = lstConns.getSelectedIndex();
                if (selIdx != -1) {
                    lstMenuPpp.show(lstConns, e.getPoint().x, e.getPoint().y + 10);
                }
            }
        });

        lstConns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstConns.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectItemAction.actionPerformed(new ActionEvent(e.getSource(), -1, null));
                }
            }
        });

        pnlAllConns.add(lstConns);

        mainDialog = new JDialog(new JFrame(), "Connection Manager");
        mainDialog.setIconImage(new ImageIcon(getClass().getResource("ManageConnection.png")).getImage());
        mainDialog.setModal(true);
        mainDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                closeDialogAction.actionPerformed(new ActionEvent(e.getSource(), -1, null));
            }
        });
        mainDialog.setLayout(new BorderLayout());

        mainDialog.getContentPane().add(pnlMain);
        pnlMain.add(pnlAllConns);
        pnlMain.add(Box.createRigidArea(new Dimension(10, 1)));
        pnlMain.add(pnlConnProps);

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(new Color(241, 241, 241));
        statusPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainDialog.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(mainDialog.getWidth(), 32));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        lblStatus = new JLabel("Ready..");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setForeground(new Color(0, 102, 204));
        lblStatus.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(lblStatus);

        mainDialog.setResizable(false);
        mainDialog.pack();
        mainDialog.setLocationRelativeTo(null);
        mainDialog.setVisible(true);
    }
}

class FileListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -7799441088157759804L;
    private JLabel label;
    private Color textSelectionColor = Color.WHITE;
    private Color backgroundSelectionColor = new Color(102, 179, 255);
    private Color textNonSelectionColor = Color.DARK_GRAY;
    private Color backgroundNonSelectionColor = Color.WHITE;

    FileListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        label.setFont(new Font("Calibri", Font.PLAIN, 18));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded) {

        label.setIcon(new ImageIcon(getClass().getResource("NewConnection.png")));
        label.setText(" " + (String) value);

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}
