package com.example.gestorDePedidosHibernate.domain.producto;

import com.example.gestorDePedidosHibernate.domain.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * clase producto que representa un producto dentro de un item
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "producto")
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;
    private String nombre;
    private Double precio;
    private Integer cantidad;

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private List<Item> items;

    public Producto(Long id_producto, String nombre, Double precio, Integer cantidad) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id_producto=" + id_producto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", items size=" + items.size() +
                '}';
    }
}
