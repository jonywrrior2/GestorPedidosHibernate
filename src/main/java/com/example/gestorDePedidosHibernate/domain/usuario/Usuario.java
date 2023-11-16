package com.example.gestorDePedidosHibernate.domain.usuario;

import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="usuario")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    private String nombreusuario;
    private String pass;
    private String email;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Pedido> pedidos;
}
