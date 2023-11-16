package com.example.gestorDePedidosHibernate;

import com.example.gestorDePedidosHibernate.domain.item.ItemDAO;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.pedido.PedidoDAO;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import com.example.gestorDePedidosHibernate.domain.producto.ProductoDAO;
import com.example.gestorDePedidosHibernate.domain.usuario.UsuarioDAO;
import jakarta.persistence.GeneratedValue;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

/**
 * Aplicacion principal
 */
public class App extends Application {
    @Getter
    @Setter
    private static Stage myStage;
    @Override
    public void start(Stage stage) throws IOException {
        myStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setMinHeight( 600 );
        stage.setMinWidth( 1000 );
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cargar escena y cambiar el titulo.
     * @param fxml nombre del archivo donde está la nueva escena
     * @param titulo Nombre del nuevo título
     */
    public static void loadFXML(String fxml, String titulo){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/"+fxml));
            Scene scene = new Scene(fxmlLoader.load());
            myStage.setTitle(titulo);
            myStage.setScene(scene);
            myStage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo "+fxml);
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo main
     * @param args No se usan argumentos
     */
    public static void main(String[] args) {
        launch();
    }
}