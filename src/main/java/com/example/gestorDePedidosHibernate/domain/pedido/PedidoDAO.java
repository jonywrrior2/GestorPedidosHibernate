package com.example.gestorDePedidosHibernate.domain.pedido;

import com.example.gestorDePedidosHibernate.domain.DAO;
import com.example.gestorDePedidosHibernate.domain.HibernateUtils;
//import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.Sesion;
import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import com.example.gestorDePedidosHibernate.domain.usuario.Usuario;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para consultas relacionadas con los pedidos
 */
@Log
public class PedidoDAO implements DAO<Pedido> {
    @Override
    public ArrayList<Pedido> getAll( ) {
        return null;
    }

    @Override
    public Pedido get( Long id ) {
        return null;
    }

    /**
     * Guardar un nuevo pedido en la base de datos
     * @param data Pedido nuevo
     * @return Devuelve el nuevo pedido
     */
    @Override
    public Pedido save( Pedido data ) {

        Pedido salida = null;
        try ( org.hibernate.Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Transaction t = s.beginTransaction( );
            s.persist( data );
            t.commit( );
            salida = data;
        } catch ( Exception e ) {
            log.severe( "Error al guardar. " + data.toString( ) );
        }
        return salida;
    }

    @Override
    public void update( Pedido data ) {

    }

    /**
     * Borrar un pedido
     * @param data Pedido a borrar
     */
    @Override
    public void delete( Pedido data ) {

        HibernateUtils.getSessionFactory().inTransaction(s -> {
            Query<Item> q = s.createQuery( "from Item where pedido =: ped" , Item.class );
            q.setParameter( "ped" , data );
            List<Item> items = q.getResultList( );
            Pedido i = s.get( Pedido.class , data.getId_pedido( ) );
            items.forEach( it -> s.remove( it ) );

            s.remove( i );
        });
    }

    /**
     * Pedidos que corresponde a un usuario
     * @param usuario Usuario por el cual filtrar los pedidos
     * @return Lista de los pedidos del usuario
     */
    public List<Pedido> pedidosDeUnUsuario( Usuario usuario ) {

        List<Pedido> salida = new ArrayList<>( );
        try ( Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Query<Usuario> q = s.createQuery( "from Usuario where id_usuario =: id" , Usuario.class );
            q.setParameter( "id" , usuario.getId_usuario( ) );
            salida = q.getSingleResult( ).getPedidos( );
        }
        for (Pedido pedido : salida) {
            Double total = 0.0;
            for (Item item : pedido.getItems( )) {
                total = total + item.getCantidad( ) * item.getProducto( ).getPrecio( );
            }
            DecimalFormat formato = new DecimalFormat( "#0.00" );
            pedido.setTotal( formato.format( total ) );
        }
        return salida;
    }

    /**
     * Items correspondiente a un pedido
     * @param pedidoPulsado Pedido por el que filtrar los items
     * @return Lista  de Items correspondiente a un pedido
     */
    public List<Item> detallesDeUnPedido( Pedido pedidoPulsado ) {
        List<Item> result;
        try ( Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Query<Pedido> q = s.createQuery( "from Pedido where id_pedido =: id" , Pedido.class );
            q.setParameter( "id" , pedidoPulsado.getId_pedido( ) );
            result = new ArrayList<>( q.getSingleResult( ).getItems( ) );
        }
        return result;
    }

    /**
     * Todos los productos de la base de datos
     * @return Lista de todos los productos que estan en la base de datos
     */
    public List<String> todosLosProductos( ) {
        List<String> resultado = new ArrayList<String>( );
        try ( Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Query<String> q = s.createQuery( "select distinct p.nombre from Producto p" , String.class );
            resultado = q.getResultList( );
        }

        return resultado;
    }

    /**
     * Añadir nuevo item a un pedido
     * @param ped Pedido al cual añadir el item
     * @param cant Cantidad del producto añadido
     * @param prod Producto correspondiente al nuevo item
     */
    public void insertarItemAPedido( Pedido ped , Integer cant , Producto prod ) {
        try ( org.hibernate.Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Transaction t = s.beginTransaction( );
            //Crear u nuevo item
            Item item = new Item( );
            item.setCantidad( cant );
            item.setPedido( ped );
            item.setProducto( prod );
            s.persist( item );
            t.commit( );
        } catch ( Exception e ) {
            log.severe( "Error al insertar un nuevo item" );
        }
    }

    /**
     * Busca un producto en un pedido
     * @param nombreProducto Nombre del producto a buscar
     * @param pedido Pedido donde buscar el producto
     * @return Producto si esta en el pedido o null si no está
     */
    public Producto buscaProductoEnPedido( String nombreProducto , Pedido pedido ) {
        Producto producto = null;
        try ( org.hibernate.Session s = HibernateUtils.getSessionFactory( ).openSession( ) ) {
            Query<Producto> q = s.createQuery(
                    "select i.producto from Item i where i.producto.nombre =: nombre and i.pedido.id_pedido =: idPedido" , Producto.class );
            q.setParameter( "nombre" , nombreProducto );
            q.setParameter( "idPedido" , pedido.getId_pedido( ) );
            producto = q.getSingleResultOrNull( );
        }
        return producto;
    }

    /**
     * Checkea si el producto esta en el pedido
     * @param nombreProducto Nombre del producto a buscar
     * @param pedido Pedido donde buscar el producto
     * @return True si existe o false si no existe el producto
     */
    public boolean estaProductoEnPedido( String nombreProducto , Pedido pedido ) {
        return buscaProductoEnPedido( nombreProducto , pedido ) != null;
    }

    /**
     * Actualiza la fecha del pedido pulsado
     */
    public void actualizarFecha(Pedido ped){
        HibernateUtils.getSessionFactory().inTransaction(s -> {
            Pedido p = s.get( Pedido.class , ped.getId_pedido() );
            p.setFecha( LocalDate.now().toString() );
        });
    }



}
