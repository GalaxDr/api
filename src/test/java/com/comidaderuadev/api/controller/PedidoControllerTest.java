package com.comidaderuadev.api.controller;

import com.comidaderuadev.api.entity.pedido.DTO.PedidoDTO;
import com.comidaderuadev.api.entity.pedido.Pedido;
import com.comidaderuadev.api.entity.pedido.TipoPagamento;
import com.comidaderuadev.api.entity.produto.Produto;
import com.comidaderuadev.api.service.ItensPedidoService;
import com.comidaderuadev.api.service.PedidoService;
import com.comidaderuadev.api.service.ProdutoService;
import com.comidaderuadev.api.service.TipoPagamentoService;
import com.comidaderuadev.api.service.implementation.PedidoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PedidoServiceImpl pedidoService;

    @MockBean
    ItensPedidoService itensPedidoService;

    @MockBean
    TipoPagamentoService tipoPagamentoService;

    @MockBean
    ProdutoService produtoService;

    List<PedidoDTO> pedidos;

    PedidoDTO validPedidoDTO;

    LocalDateTime date;

    @MockBean
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        pedidos = new ArrayList<>();
        date = LocalDateTime.now().minusDays(2);
        validPedidoDTO = PedidoDTO.builder()
                .pedidoId(1)
                .pedidoData(date)
                .pedidoTipoPagamento("PIX").build();

        pedidos.add(validPedidoDTO);

        given(modelMapper.map(any(Pedido.class), Mockito.eq(PedidoDTO.class))).willReturn(validPedidoDTO);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(pedidoService);
    }

    @Test
    void findAll() throws Exception {
        //given
        List<Pedido> list = new ArrayList<>();
        list.add(new Pedido(new TipoPagamento("PIX")));
        given(pedidoService.findAll()).willReturn(list);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(pedidos.size())))
                .andExpect(jsonPath("$.[0].tipoPagamento", is("PIX")))
                .andExpect(jsonPath("$.[0].idPedido", is(1)));

        //then
        then(pedidoService).should().findAll();
        then(pedidoService).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAllTiposPagamentos() {
    }

    @Test
    void findByIdWithDetails() {
    }

    @Test
    void createPedido() {
    }
}