package it.shaunyl.datareporter;

import it.shaunyl.datareporter.login.ui.LoginPresentationControl;
import java.awt.Color;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Entry point
 *
 */
@Slf4j
public class Main {
    
    private static final BeanFactory beanFactory;

    static { 
        beanFactory = new ClassPathXmlApplicationContext("/beans/Beans.xml");
    }    
    
    public static void main(String[] args) {

        UIManager.put("Label.foreground", new Color(81, 81, 81));
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            log.error("", e);
        }
        LoginPresentationControl loginPresentationControl = beanFactory.getBean(LoginPresentationControl.class);
        loginPresentationControl.login();
    }
}
