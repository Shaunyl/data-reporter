package it.shaunyl.datareporter;

import it.shaunyl.datareporter.login.ui.LoginPresentationControl;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Entry point
 *
 */
@Slf4j
public class Main {

    private static final AbstractApplicationContext beanFactory;

    static {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
        ContextManager.Locator.set(new DefaultContextManagerProvider());
        beanFactory = new ClassPathXmlApplicationContext("/beans/Beans.xml");
        beanFactory.registerShutdownHook();
        
    }

    public static void main(String[] args) {
        LoginPresentationControl loginPresentationControl
                = beanFactory.getBean(LoginPresentationControl.class);
        loginPresentationControl.login();
    }
}
