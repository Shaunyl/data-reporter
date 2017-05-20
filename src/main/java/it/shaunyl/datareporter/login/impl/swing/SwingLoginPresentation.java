package it.shaunyl.datareporter.login.impl.swing;

import it.shaunyl.datareporter.login.ui.LoginPresentation;
import it.shaunyl.datareporter.utils.swing.SwingBinder;
import it.tidalwave.role.ui.UserAction;
import java.util.List;
import javax.inject.Inject;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo
 */
@Service @Slf4j
public class SwingLoginPresentation extends JFrame implements LoginPresentation {

    private UserAction userNewAction, userConnectAction, userSelectAction;

    private final DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
    
    @Inject
    private SwingBinder binder;
    
    /**
     * Creates new form SwingLoginPresentation
     */
    public SwingLoginPresentation() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPnl = new javax.swing.JPanel();
        lblPasswd = new javax.swing.JLabel();
        cmbConnList = new javax.swing.JComboBox<>();
        btnNewConn = new javax.swing.JButton();
        btnConn = new javax.swing.JButton();
        txbPwd = new javax.swing.JPasswordField();
        lblWelcome = new javax.swing.JLabel();
        lblConnName = new javax.swing.JLabel();
        pnlStatus = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainPnl.setBackground(new java.awt.Color(255, 255, 255));

        lblPasswd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        lblPasswd.setText("Password:");

        cmbConnList.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        cmbConnList.setModel(comboModel);

        btnNewConn.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnNewConn.setText("New");

        btnConn.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnConn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/shaunyl/datareporter/login/impl/swing/connect.png"))); // NOI18N
        btnConn.setText("Login");

        txbPwd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txbPwd.setText("jPasswordField1");

        lblWelcome.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        lblWelcome.setText("Login");

        lblConnName.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        lblConnName.setText("Connection:");

        javax.swing.GroupLayout mainPnlLayout = new javax.swing.GroupLayout(mainPnl);
        mainPnl.setLayout(mainPnlLayout);
        mainPnlLayout.setHorizontalGroup(
            mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPnlLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblWelcome)
                    .addGroup(mainPnlLayout.createSequentialGroup()
                        .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPnlLayout.createSequentialGroup()
                                .addComponent(lblPasswd)
                                .addGap(37, 37, 37)
                                .addComponent(txbPwd))
                            .addGroup(mainPnlLayout.createSequentialGroup()
                                .addComponent(lblConnName)
                                .addGap(18, 18, 18)
                                .addComponent(cmbConnList, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConn)
                            .addComponent(btnNewConn))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        mainPnlLayout.setVerticalGroup(
            mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPnlLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(lblWelcome)
                .addGap(11, 11, 11)
                .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPnlLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblConnName))
                    .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbConnList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNewConn)))
                .addGap(11, 11, 11)
                .addGroup(mainPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPnlLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblPasswd))
                    .addComponent(txbPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(btnConn)
                .addContainerGap())
        );

        pnlStatus.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatus.setMinimumSize(new java.awt.Dimension(482, 27));

        lblStatus.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        lblStatus.setText("Ready");
        lblStatus.setToolTipText("");

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblStatus))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        dismiss();
    }//GEN-LAST:event_formWindowClosing

    @Override
    public void bind(UserAction userConnectAction, UserAction userNewAction, UserAction userSelectAction) {
        this.userSelectAction = userSelectAction;
        this.userConnectAction = userConnectAction;
        this.userNewAction = userNewAction;
    }

    @Override
    public void showUp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setLocationRelativeTo(null);
                setVisible(true);
                binder.bind(btnConn, userConnectAction);
                binder.bind(btnNewConn, userNewAction);
                binder.bind(cmbConnList, userSelectAction);
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
                binder.bind(cmbConnList, userSelectAction);
                for (String connection : connections) {
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
                btnNewConn.setAction(null);
                btnConn.setAction(null);
                ((JFrame) (SwingUtilities.getAncestorOfClass(JFrame.class, SwingLoginPresentation.this))).dispose();
            }
        });
    }
    
    @Override
    public String getPassword() {
        return new String(txbPwd.getPassword());
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConn;
    private javax.swing.JButton btnNewConn;
    private javax.swing.JComboBox<String> cmbConnList;
    private javax.swing.JLabel lblConnName;
    private javax.swing.JLabel lblPasswd;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel mainPnl;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JPasswordField txbPwd;
    // End of variables declaration//GEN-END:variables
}