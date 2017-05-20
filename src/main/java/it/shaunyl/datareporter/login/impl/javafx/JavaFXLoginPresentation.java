//package it.shaunyl.datareporter.login.impl.javafx;
//
//import it.shaunyl.datareporter.login.ui.LoginPresentation;
//import it.tidalwave.role.ui.UserAction;
//import java.util.List;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javax.swing.Action;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author Filippo
// */
//@Service(value = "javafx-login-presentation")
//public class JavaFXLoginPresentation extends Application implements LoginPresentation {
//
//    private Stage stage;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        this.stage = primaryStage;
//
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
//
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/Styles.css");
//
//        stage.setTitle("Data Reporter");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    @Override
//    public void showUp() {
//        launch();
//    }
//
//    @Override
//    public void bind(UserAction connectAction) {
//        //
//    }
//    
//    @Override
//    public void bind(Action connectAction, Action newConnectionAction, Action selectConnectionAction) {
////        btn.setOnAction(value);
//    }
//
//    @Override
//    public void addAllConnectionsToCombo(List<String> connNames) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void setPassword(String pwd) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void dismiss() {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getPassword() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void notifyFailedLogin(String text) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getUsername() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getConnectionName() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
