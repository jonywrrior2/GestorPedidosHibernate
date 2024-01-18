package com.example.gestorDePedidosHibernate.controllers;

import com.example.gestorDePedidosHibernate.App;
import com.example.gestorDePedidosHibernate.domain.Sesion;
import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.pedido.PedidoDAO;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista Detalles de un pedido
 */
public class DetallesPedidoController implements Initializable
{

    @FXML
    private TableView<Item> tablaDetallesPedido;
    @FXML
    private TableColumn<Item,String>  cNombre;
    @FXML
    private TableColumn<Item,String>  cPrecio;
    @FXML
    private TableColumn<Item,String>  cCantidad;
    @FXML
    private Label labelTitulo;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAtras;

    /**
     * Metodo inicializar
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       PedidoDAO dao = new PedidoDAO();
        List<Item> items = dao.detallesDeUnPedido(Sesion.getPedidoPulsado());

             //Cambiar titulo
        labelTitulo.setText("Pedido número " + Sesion.getPedidoPulsado().getId_pedido());

            //Rellenar la tabla
        cNombre. setCellValueFactory( (fila) -> {
            String nombre = fila.getValue().getProducto().getNombre();
            return new SimpleStringProperty(nombre);
        });
        cCantidad. setCellValueFactory( (fila) -> {
            int cantidad = fila.getValue().getCantidad();
            return new SimpleStringProperty(Integer.toString(cantidad));
        });
        cPrecio. setCellValueFactory( (fila) -> {
            double precio = fila.getValue().getProducto().getPrecio();
            return new SimpleStringProperty(Double.toString(precio));
        });
        ObservableList<Item> observableList = FXCollections.observableArrayList();
        observableList.addAll(items);
        tablaDetallesPedido.setItems(observableList);
}

    /**
     * Función botón atrás
     */
    @FXML
    public void atras() {
        App.loadFXML("pedidos-view.fxml", "Pedidos de " + Sesion.getUsuarioActual().getNombreusuario());
    }

    /**
     * Función botón logout
     */
    @FXML
    public void logout() {
        Sesion.logout();
        App.loadFXML("login-view.fxml", "Iniciar Sesión");
    }

    @FXML
    public void visualizarPedido(ActionEvent actionEvent) {
        String codigoPedido = String.valueOf(Sesion.getPedidoPulsado().getId_pedido());
        Stage primaryStage = new Stage();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/gestionpedidos", "root", "");

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("cod_pedido", codigoPedido);

            JasperPrint jasperPrint = JasperFillManager.fillReport("reportePedido.jasper", hashMap, connection);

            SwingNode swingNode = new SwingNode();
            createSwingContent(swingNode, jasperPrint);

            StackPane root = new StackPane();
            root.getChildren().add(swingNode);
            Scene scene = new Scene(root, 800, 600);

            primaryStage.setTitle("Detalles Pedido");
            primaryStage.setScene(scene);
            primaryStage.show();

            JRPdfExporter exp = new JRPdfExporter();
            exp.setExporterInput(new SimpleExporterInput(jasperPrint));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput("pedido.pdf"));
            exp.setConfiguration(new SimplePdfExporterConfiguration());
            exp.exportReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSwingContent(final SwingNode swingNode, JasperPrint jasperPrint) {
        SwingUtilities.invokeLater(() -> {
            JRViewer viewer = new JRViewer(jasperPrint);
            swingNode.setContent(viewer);
        });
    }
}