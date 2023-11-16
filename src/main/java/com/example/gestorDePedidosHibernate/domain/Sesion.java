package com.example.gestorDePedidosHibernate.domain;

import com.example.gestorDePedidosHibernate.domain.item.Item;
import com.example.gestorDePedidosHibernate.domain.pedido.Pedido;
import com.example.gestorDePedidosHibernate.domain.usuario.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa las variables de sesion
 */
@Data
public class Sesion {

    /**
     * Usuario actual logeado
     */
    @Setter
    @Getter
    private static Usuario usuarioActual;

    /**
     * Pedido que pulsa el usuario en la tabla de pedidos
     */
    @Setter
    @Getter
    private static Pedido pedidoPulsado;

    /**
     * Item pulsado por el usuario en la tabla productos
     */
    @Setter
    @Getter
    private static Item itemPulsado;

    /**
     * True si estamos añadiendo un nuevo producto false si estamos modificandolo
     */
    @Setter
    @Getter
    private static boolean esUnNuevoProducto;

    /**
     * True si estamos añadiendo un nuevo pedido false si estamos modificandolo
     */
    @Setter
    @Getter
    private static boolean esUnNuevoPedido;

    /**
     * Cerrar sesion y vacía las variables de sesion
     */
    public static void logout(){
        Sesion.setPedidoPulsado( null );
        Sesion.setUsuarioActual( null );
        Sesion.setItemPulsado( null );
    }



}