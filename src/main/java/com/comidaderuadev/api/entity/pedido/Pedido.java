package com.comidaderuadev.api.entity.pedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "tipo_pagamento_id")
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name = "pedido_status_id")
    private StatusPedido status;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pedido_data")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date dataPedido;

    @OneToMany(mappedBy = "pedido")
    private List<ItensPedido> itens;

    Pedido() {}

    public Pedido(TipoPagamento tipoPagamento, StatusPedido status) {
        this.tipoPagamento = tipoPagamento;
        this.status = status;
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<ItensPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItensPedido> itens) {
        this.itens = itens;
    }

    public void addItem(ItensPedido item) {
        itens.add(item);
    }

    public void removeItem(ItensPedido item) {
        itens.remove(item);
    }
    
}
