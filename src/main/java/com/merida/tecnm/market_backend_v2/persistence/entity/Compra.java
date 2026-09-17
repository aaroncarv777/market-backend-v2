package com.merida.tecnm.market_backend_v2.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name="compras")

public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id compra")
    private Integer idCompra;

    @Column (name = "id cliente")
    private String idcliente;


    private LocalDateTime fecha;

    @Column (name = "medio pago")
    private String medioPago;


    private String comentario;
    private String estado;
}
