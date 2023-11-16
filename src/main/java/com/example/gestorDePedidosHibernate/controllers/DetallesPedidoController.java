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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
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
}