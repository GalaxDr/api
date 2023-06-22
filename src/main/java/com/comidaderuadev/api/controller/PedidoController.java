package com.comidaderuadev.api.controller;

import com.comidaderuadev.api.entity.pedido.DTO.CriaPedidoDTO;
import com.comidaderuadev.api.entity.pedido.DTO.PedidoDTO;
import com.comidaderuadev.api.entity.pedido.DTO.PedidoDetalhadoDTO;
import com.comidaderuadev.api.entity.pedido.DTO.TipoPagamentoDTO;
import com.comidaderuadev.api.entity.pedido.Pedido;
import com.comidaderuadev.api.entity.pedido.TipoPagamento;
import com.comidaderuadev.api.service.ItensPedidoService;
import com.comidaderuadev.api.service.PedidoService;
import com.comidaderuadev.api.service.ProdutoService;
import com.comidaderuadev.api.service.TipoPagamentoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItensPedidoService itensPedidoService;

    @Autowired
    private TipoPagamentoService tipoPagamentoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<PedidoDTO> findAll() {
        return pedidoService
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @GetMapping("/detalhes")
    public List<PedidoDetalhadoDTO> findAllDetalhado() {
        return pedidoService
                .findAll()
                .stream()
                .map(this::convertToDetailedDTO)
                .toList();
    }

    @GetMapping("/tiposPagamentos")
    public List<TipoPagamentoDTO> findAllTiposPagamentos() {
        return tipoPagamentoService
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{pedidoId}")
    public PedidoDetalhadoDTO findByIdWithDetails(@PathVariable int pedidoId) {
        Pedido pedido = pedidoService.findById(pedidoId);
        return convertToDetailedDTO(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPedido(@Valid @RequestBody CriaPedidoDTO criaPedidoDTO) {
        TipoPagamento tipoPagamento = tipoPagamentoService.findByDescricao(criaPedidoDTO.getTipoPagamento());
        itensPedidoService.addProdutos(new Pedido(tipoPagamento), criaPedidoDTO.getItens());
    }

    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePedido(@PathVariable int pedidoId) {
        pedidoService.delete(pedidoId);
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO pedidoDTO = modelMapper.map(pedido, PedidoDTO.class);
        pedidoDTO.setTipoPagamento(pedido.getTipoPagamento().getDescricao());
        return pedidoDTO;
    }

    private PedidoDetalhadoDTO convertToDetailedDTO(Pedido pedido) {
        PedidoDetalhadoDTO pedidoDTO = modelMapper.map(pedido, PedidoDetalhadoDTO.class);
        pedidoDTO.setTipoPagamento(pedido.getTipoPagamento().getDescricao());
        return pedidoDTO;
    }

    private Pedido convertToEntity(PedidoDTO pedidoDTO) {
        return modelMapper.map(pedidoDTO, Pedido.class);
    }

    private TipoPagamentoDTO convertToDTO(TipoPagamento tipoPagamento) {
        return modelMapper.map(tipoPagamento, TipoPagamentoDTO.class);
    }

    private TipoPagamento convertToEntity(TipoPagamentoDTO tipoPagamentoDTO) {
        return modelMapper.map(tipoPagamentoDTO, TipoPagamento.class);
    }

}
