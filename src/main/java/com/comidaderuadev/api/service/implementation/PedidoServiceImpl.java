package com.comidaderuadev.api.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comidaderuadev.api.entity.pedido.ItensPedido;
import com.comidaderuadev.api.entity.pedido.Pedido;
import com.comidaderuadev.api.entity.produto.Produto;
import com.comidaderuadev.api.exceptions.produto.NotFoundException;
import com.comidaderuadev.api.repository.ItensPedidoRepository;
import com.comidaderuadev.api.repository.PedidoRepository;
import com.comidaderuadev.api.repository.ProdutoRepository;
import com.comidaderuadev.api.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido findById(int id) {
        Pedido p = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido nao encontrado. Id: " + id));

        return p;
    }

    @Override
    public Pedido add(Pedido pedido) {
        pedido.setId(0);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido update(Pedido pedido) {
        Optional<Pedido> p = pedidoRepository.findById(pedido.getId());

        if (p.isPresent()) {
            pedido.setDataPedido(p.get().getDataPedido());
            return pedidoRepository.save(pedido);
        }

        throw new NotFoundException("Pedido não encontrado. Pedido: " + pedido);
    }

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Pedido addProduto(Pedido pedido, Produto produto) {
        if (!produtoRepository.existsById(produto.getId()))
            throw new NotFoundException("Produto não encontrado");

        if (!pedidoRepository.existsById(pedido.getId()))
            throw new NotFoundException("Pedido não encontrado");

        ItensPedido item = new ItensPedido(pedido, produto);
        itensPedidoRepository.save(item);
        return pedido;
    }

    @Override
    public List<Pedido> findAllWithDetail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllWithDetail'");
    }

}
