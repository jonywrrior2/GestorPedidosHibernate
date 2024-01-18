package com.example.gestorDePedidosHibernate.controllers;

import com.example.gestorDePedidosHibernate.App;
import com.example.gestorDePedidosHibernate.domain.HibernateUtils;
import com.example.gestorDePedidosHibernate.domain.Sesion;
import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.item.ItemDAO;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.pedido.PedidoDAO;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import com.example.gestorDePedidosHibernate.domain.producto.ProductoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista Editar un pedido
 */
public class EditarPedidoController implements Initializable {
    private static PedidoDAO pedidoDAO;
    private static ItemDAO itemDAO;
    private static ProductoDAO productoDAO;

    @FXML
    private TableView<Item> tablaDetallesPedido;
    @FXML
    private TableColumn<Item, String> cNombre;
    @FXML
    private TableColumn<Item, String> cPrecio;
    @FXML
    private TableColumn<Item, String> cCantidad;
    @FXML
    private Label labelTitulo;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAtras;
    @FXML
    private VBox menuLateral;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnAniadir;
    @FXML
    private ComboBox<String> comboNombre;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize( URL url , ResourceBundle resourceBundle ) {
        pedidoDAO = new PedidoDAO( );
        itemDAO = new ItemDAO( );
        productoDAO = new ProductoDAO( );

//        Si es un pedidoNuevo
        if (Sesion.isEsUnNuevoPedido( )) {
            Pedido nuevoPedido = new Pedido(  );
            nuevoPedido.setUsuario( Sesion.getUsuarioActual() );
            nuevoPedido.setFecha( LocalDate.now().toString() );
            pedidoDAO.save( nuevoPedido );
            Sesion.setPedidoPulsado( nuevoPedido );
            //Cambiar titulo
            labelTitulo.setText( "Añadir pedido " );


        }
//        Si no es un nuevo pedido
        else{
            //Rellenar la tabla
            rellenarTabla( );
            //Cambiar titulo
            if (Sesion.getPedidoPulsado()!=null) labelTitulo.setText( "Editar pedido " + Sesion.getPedidoPulsado( ).getId_pedido( )  );

        }

        //listener de la tabla
        aniadirListenerTabla( );

        //Rellenar comboBox
        comboNombre.getItems( ).addAll( pedidoDAO.todosLosProductos( ) );

    }

    /**
     * Definición de un listener para la tabla
     */
    private void aniadirListenerTabla( ) {
        tablaDetallesPedido.getSelectionModel( ).selectedItemProperty( ).addListener( ( observableValue , producto , t1 ) -> {

            menuLateral.setDisable( false );
            if (t1 != null) Sesion.setItemPulsado( t1 );
            comboNombre.setValue( Sesion.getItemPulsado( ).getProducto( ).getNombre( ) );
            spinnerCantidad.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 1 , 1000 , Sesion.getItemPulsado( ).getCantidad( ) , 1 ) );
            btnEditar.setText( "Editar" );
            comboNombre.setDisable( true );
        } );
    }

    /**
     * Llenar la tabla de valores de la base de datos
     */
    private void rellenarTabla( ) {
        List<Item> items = pedidoDAO.detallesDeUnPedido( Sesion.getPedidoPulsado( ) );
        cNombre.setCellValueFactory( ( fila ) -> {
            String nombre = fila.getValue( ).getProducto( ).getNombre( );
            return new SimpleStringProperty( nombre );
        } );
        cCantidad.setCellValueFactory( ( fila ) -> {
            int cantidad = fila.getValue( ).getCantidad( );
            return new SimpleStringProperty( Integer.toString( cantidad ) );
        } );
        cPrecio.setCellValueFactory( ( fila ) -> {
            double precio = fila.getValue( ).getProducto( ).getPrecio( );
            return new SimpleStringProperty( Double.toString( precio ) );
        } );
        ObservableList<Item> observableList = FXCollections.observableArrayList( );
        observableList.addAll( items );
        tablaDetallesPedido.setItems( observableList );
    }

    /**
     * Función botón Atrás
     */
    @FXML
    public void atras( ) {
        App.loadFXML( "pedidos-view.fxml" , "Pedidos de " + Sesion.getUsuarioActual( ).getNombreusuario( ) );
        Sesion.setItemPulsado( null );
    }

    /**
     * Función botón logout
     */
    @FXML
    public void logout( ) {
        Sesion.logout();
        App.loadFXML( "login-view.fxml" , "Iniciar Sesión" );
    }

    /**
     * Función botón editar
     */
    @FXML
    public void editar( ) {
        try {
            if (Sesion.isEsUnNuevoProducto( )) {
                if (pedidoDAO.estaProductoEnPedido( comboNombre.getValue( ) , Sesion.getPedidoPulsado( ) )) {
                    Item item = itemDAO.itemEnPedidoPorNombre( Sesion.getPedidoPulsado( ) , comboNombre.getValue( ) );
                    modificaItem( spinnerCantidad.getValue( ) + item.getCantidad( ) );
                } else {
                    Producto producto = productoDAO.productoPorNombre( comboNombre.getValue( ) );
                    pedidoDAO.insertarItemAPedido( Sesion.getPedidoPulsado( ) , spinnerCantidad.getValue( ) , producto );
                }
                Sesion.setEsUnNuevoProducto( false );
            } else {
                modificaItem( spinnerCantidad.getValue( ) );
            }
            pedidoDAO.actualizarFechaYTotal(Sesion.getPedidoPulsado());
            this.rellenarTabla( );
            this.menuLateral.setDisable( true );
            this.tablaDetallesPedido.setDisable( false );

        } catch ( NumberFormatException e ) {

            System.out.println( e.getMessage( ) );
        }
    }

    /**
     * Modifica la cantidad de un item
     * @param cantidad Nueva cantidad del item
     */
    private void modificaItem( Integer cantidad ) {
        Item itemModificado = itemDAO.itemEnPedidoPorNombre( Sesion.getPedidoPulsado( ) ,
                                                             comboNombre.getValue( ) );
        Producto productoModificado = productoDAO.productoPorNombre( comboNombre.getValue( ) );
        itemModificado.setCantidad( cantidad );
        itemModificado.setProducto( productoModificado );
        itemDAO.update( itemModificado );
    }

    /**
     * Función botón Añadir
     */
    @FXML
    public void aniadir( ) {
        Sesion.setEsUnNuevoProducto( true );
        comboNombre.setDisable( false );
        menuLateral.setDisable( false );
        tablaDetallesPedido.setDisable( true );
        btnEditar.setText( "Añadir" );
        comboNombre.setValue( comboNombre.getItems( ).getFirst( ) );
        spinnerCantidad.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 1 , 100 , 1 , 1 ) );
    }

    /**
     * Función botón eliminar
     */
    @FXML
    public void eliminar( ) {
        if (Sesion.getItemPulsado( ) != null) {
            itemDAO.delete( Sesion.getItemPulsado( ) );
            this.rellenarTabla( );
            this.menuLateral.setDisable( true );
        }
    }

    /**
     * Función botón cancelar
     */
    @FXML
    public void cancelar( ) {
        this.menuLateral.setDisable( true );
        this.tablaDetallesPedido.setDisable( false );
    }

}
