package com.comidaderuadev.api.entity.pedido.DTO;

import com.comidaderuadev.api.entity.DTO.ProdutoDTO;

public class ItemPedidoDTO {

    private ProdutoDTO produto;
    private int vendaId;

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

}
