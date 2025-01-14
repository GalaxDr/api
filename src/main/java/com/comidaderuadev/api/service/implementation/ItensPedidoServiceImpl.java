package com.comidaderuadev.api.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comidaderuadev.api.entity.pedido.ItensPedido;
import com.comidaderuadev.api.entity.pedido.Pedido;
import com.comidaderuadev.api.exceptions.produto.NotFoundException;
import com.comidaderuadev.api.repository.ItensPedidoRepository;
import com.comidaderuadev.api.repository.PedidoRepository;
import com.comidaderuadev.api.repository.ProdutoRepository;
import com.comidaderuadev.api.service.ItensPedidoService;

import jakarta.transaction.Transactional;

@Service
public class ItensPedidoServiceImpl implements ItensPedidoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

//     @Override
//     public List<ItensPedido> findAll() {
//         return itensPedidoRepository.findAll();
//     }

    @Override
    public List<ItensPedido> findByPedido(Pedido pedido) {
        List<ItensPedido> carrinho = itensPedidoRepository.findByPedido(pedido);
        return carrinho;
    }

    @Transactional
    @Override
    public void removeProdutoCarrinho(int pedidoId, int produtoId) {
        if (!pedidoRepository.existsById(pedidoId))
            throw new NotFoundException("Pedido não encontrado. id: " + pedidoId);
        
        if (!produtoRepository.existsById(produtoId))
            throw new NotFoundException("Produto não encontrado. id: " + produtoId);

        itensPedidoRepository.removeByPedidoIdAndProdutoId(pedidoId, produtoId);
    }

//     @Override
//     public ItensPedido createItensPedido(Pedido pedido) {
//         if (!pedidoRepository.existsById(pedido.getId()))
//             throw new NotFoundException("Pedido não encontrado. Pedido: " + pedido);
        
//         ItensPedido carrinho = new ItensPedido(pedido);
//         carrinho.setId(0);
//         return itensPedidoRepository.save(carrinho);
//     }

//     @Override
//     public ItensPedido addProdutoCarrinho(Produto produto, ItensPedido carrinho) {
//         if (!itensPedidoRepository.existsById(carrinho.getId()))
//             throw new NotFoundException("Carrinho não encontrado. Carrinho: " + carrinho);
        
//         if (!produtoRepository.existsById(produto.getId()))
//             throw new NotFoundException("Produto não encontrado. Produto: " + produto);
        
        
//         carrinho.addProduto(produto);
        
//         return carrinho;
//     }

//     @Override
//     public void removeProdutoCarrinho(Produto produto, ItensPedido carrinho) {
//         if (!itensPedidoRepository.existsById(carrinho.getId()))
//             throw new NotFoundException("Carrinho não encontrado. Carrinho: " + carrinho);
        
//         if (!produtoRepository.existsById(produto.getId()))
//             throw new NotFoundException("Produto não encontrado. Produto: " + produto);
        
        
//         carrinho.addProduto(produto);
        
//         return carrinho;
//     }
}
