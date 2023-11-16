package com.example.gestorDePedidosHibernate.domain.item;

import com.example.gestorDePedidosHibernate.domain.DAO;
import com.example.gestorDePedidosHibernate.domain.HibernateUtils;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import com.example.gestorDePedidosHibernate.domain.usuario.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * DAO para consultas relacionadas con los items
 */
public class ItemDAO implements DAO<Item> {
    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public Item get(Long id) {
        return null;
    }

    @Override
    public Item save(Item data) {
        return null;
    }

    /**
     * Actualizar un Item
     * @param data item modificado
     */
    @Override
    public void update(Item data) {

        try(Session s = HibernateUtils.getSessionFactory().openSession()){
            Transaction t = s.beginTransaction();
            Item nuevoItem = s.get(Item.class, data.getId_item());
            Producto nuevoProducto = s.get(Producto.class, data.getProducto().getId_producto());
            nuevoItem.setCantidad(data.getCantidad());
            nuevoItem.setProducto(nuevoProducto);
            t.commit();
        }
    }

    /**
     * Borra un item
     * @param data Item que borrar
     */
    @Override
    public void delete(Item data) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Item i = session.get(Item.class, data.getId_item());
            session.remove(i);
        });
    }

    /**
     * Devuelve el Item si existe un producto en un pedido
     * @param p Pedido en el cual buscar
     * @param nombreProducto Nombre del producto a buscr
     * @return Item del pedido con ese producto o null si no existe
     */
    public Item itemEnPedidoPorNombre(Pedido p, String nombreProducto){
        Item result = null;
        try(Session s = HibernateUtils.getSessionFactory().openSession()) {
            Query<Item> q = s.createQuery("from Item i where i.producto.nombre =: n and i.pedido.id_pedido=: idPedido",Item.class);
            q.setParameter("n",nombreProducto);
            q.setParameter("idPedido",p.getId_pedido());
            result = q.getSingleResultOrNull();
        }
        return result;
    }
}
