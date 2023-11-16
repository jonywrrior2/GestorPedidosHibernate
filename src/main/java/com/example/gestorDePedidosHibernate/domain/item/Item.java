package com.example.gestorDePedidosHibernate.domain.item;

import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.producto.Producto;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Corresponde a un Item de un pedido, contiene un producto, el numero de unidades del producto y el pedido al que pertenece
 */
@Data
@Entity
@Table(name = "item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_item;
//    private Long pedido_id;
    private Integer cantidad;
//    private Long producto_id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Override
    public String toString() {
        return "Item{" +
                "id_item=" + id_item +
                ", cantidad=" + cantidad +
                ", pedido_id=" + pedido.getId_pedido() +
                ", producto_id=" + producto.getId_producto() +
                '}';
    }
}
