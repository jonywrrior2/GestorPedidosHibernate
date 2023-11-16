package com.example.gestorDePedidosHibernate.domain.producto;

import com.example.gestorDePedidosHibernate.domain.DAO;
import com.example.gestorDePedidosHibernate.domain.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * DAO para consultas relacionadas con los productos
 */
public class ProductoDAO implements DAO<Producto> {
    @Override
    public List<Producto> getAll() {
        return null;
    }

    @Override
    public Producto get(Long id) {
        return null;
    }

    @Override
    public Producto save(Producto data) {
        return null;
    }

    @Override
    public void update(Producto data) {

    }

    @Override
    public void delete(Producto data) {

    }

    /**
     * Devuelve una instancia de un producto por su nombre
     * @param nombreProducto Nombre a buscar
     * @return Devuelve un objeto del producto con ese nombre o null si no existe
     */
    public Producto productoPorNombre(String nombreProducto){
        Producto result = null;
        try(Session s = HibernateUtils.getSessionFactory().openSession()) {
            Query<Producto> q = s.createQuery("from Producto p where p.nombre =: n",Producto.class);
            q.setParameter("n",nombreProducto);
            result = q.getSingleResultOrNull();
        }
        return result;
    }
}
