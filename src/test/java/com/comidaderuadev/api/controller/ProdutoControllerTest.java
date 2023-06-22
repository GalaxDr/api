package com.comidaderuadev.api.controller;

import com.comidaderuadev.api.entity.DTO.CategoriaDTO;
import com.comidaderuadev.api.entity.mapper.MapStructMapperProdutos;
import com.comidaderuadev.api.entity.mapper.MapStructMapperProdutosImpl;
import com.comidaderuadev.api.entity.produto.Categoria;
import com.comidaderuadev.api.entity.DTO.ProdutoDTO;
import com.comidaderuadev.api.entity.produto.Produto;
import com.comidaderuadev.api.service.CategoriaService;
import com.comidaderuadev.api.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @MockBean
    ProdutoService produtoService;

    @MockBean
    CategoriaService categoriaService;

    @MockBean
    MapStructMapperProdutos mapStructMapperProdutos;

    @Autowired
    MockMvc mockMvc;

    @Nested
    public class ProdutoTests {
        ProdutoDTO validProdutoDTO;

        ProdutoDTO anotherValidProdutoDTO;

        Produto produto;

        Produto anotherProduto;

        List<Produto> produtos = new ArrayList<>();

        @Captor
        ArgumentCaptor<Produto> produtoArgumentCaptor;

        @Captor
        ArgumentCaptor<ProdutoDTO> produtoDTOArgumentCaptor;

        @Captor
        ArgumentCaptor<String> stringArgumentCaptor;

        ObjectWriter ow;

        @BeforeEach
        void setUp() {

            produto = Produto.builder()
                    .id(1)
                    .produtoDescricao("PAO")
                    .categoria(new Categoria("ALEMA"))
                    .produtoValor(15.0)
                    .build();

            anotherProduto = Produto.builder()
                    .id(2)
                    .produtoDescricao("TORTA")
                    .categoria(new Categoria("ARABE"))
                    .produtoValor(15.0)
                    .build();

            produtos.add(produto);
            produtos.add(anotherProduto);

            // Converte de entidade para DTO
            given(mapStructMapperProdutos.produtoToProdutoDTO(produtoArgumentCaptor.capture()))
                    .willAnswer( invocationOnMock -> {
                        Produto capturedProduto = invocationOnMock.getArgument(0);
                        return ProdutoDTO.builder()
                                .produtoId(capturedProduto.getId())
                                .produtoDescricao(capturedProduto.getProdutoDescricao())
                                .produtoValor(capturedProduto.getProdutoValor())
                                .categoria(capturedProduto.getCategoria().getDescricao())
                                .build();
                    });

            // Converte de DTO para entidade
            given(mapStructMapperProdutos.produtoDTOToProduto(produtoDTOArgumentCaptor.capture(),
                    Mockito.eq(categoriaService)))

                    .willAnswer( invocationOnMock -> {
                        ProdutoDTO capturedProdutoDTO = invocationOnMock.getArgument(0);
                        return Produto.builder()
                                .id(capturedProdutoDTO.getProdutoId())
                                .produtoDescricao(capturedProdutoDTO.getProdutoDescricao())
                                .produtoValor(capturedProdutoDTO.getProdutoValor())
                                .categoria(categoriaService.findByDescricao(capturedProdutoDTO.getCategoria()))
                                .build();
                        });

            given(categoriaService.findByDescricao(stringArgumentCaptor.capture()))
                    .willAnswer(invocationOnMock -> {
                        String descricao = invocationOnMock.getArgument(0);
                        return new Categoria(descricao);
                    });

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ow = mapper.writer().withDefaultPrettyPrinter();
        }

        @AfterEach
        void tearDown() {
            Mockito.reset(produtoService);
        }

        @Test
        void findAll() throws Exception {
            //given
            given(produtoService.findAll()).willReturn(produtos);

            //when
            mockMvc.perform(MockMvcRequestBuilders.get("/produtos")
                            .accept(MediaType.APPLICATION_JSON))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$.[0].produtoId", is(produto.getId())))
                    .andExpect(jsonPath("$.[0].produtoDescricao", is(produto.getProdutoDescricao())))
                    .andExpect(jsonPath("$.[0].produtoValor", is(produto.getProdutoValor())))
                    .andExpect(jsonPath("$.[0].categoria", is(produto.getCategoria().getDescricao())))
                    .andExpect(jsonPath("$.[1].produtoId", is(anotherProduto.getId())))
                    .andExpect(jsonPath("$.[1].produtoDescricao", is(anotherProduto.getProdutoDescricao())))
                    .andExpect(jsonPath("$.[1].produtoValor", is(anotherProduto.getProdutoValor())))
                    .andExpect(jsonPath("$.[1].categoria", is(anotherProduto.getCategoria().getDescricao())));

            then(produtoService).should().findAll();
            then(produtoService).shouldHaveNoMoreInteractions();
        }

        @Test
        void findById() throws Exception {
            //given
            given(produtoService.findById(anyInt())).willReturn(produto);

            //when
            mockMvc.perform(get("/produtos/" + produto.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.produtoId", is(produto.getId())))
                    .andExpect(jsonPath("$.produtoDescricao", is(produto.getProdutoDescricao())))
                    .andExpect(jsonPath("$.produtoValor", is(produto.getProdutoValor())))
                    .andExpect(jsonPath("$.categoria", is(produto.getCategoria().getDescricao())));

            then(produtoService).should().findById(anyInt());
            then(produtoService).shouldHaveNoMoreInteractions();

        }

        @Test
        void addProduto() throws Exception {
            //given
            String requestJson = ow.writeValueAsString(
                    mapStructMapperProdutos.produtoToProdutoDTO(produto)
            );
            given(produtoService.add(any(Produto.class))).willReturn(produto);

            //when
            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    //then
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.produtoId", is(produto.getId())))
                    .andExpect(jsonPath("$.produtoDescricao", is(produto.getProdutoDescricao())))
                    .andExpect(jsonPath("$.produtoValor", is(produto.getProdutoValor())))
                    .andExpect(jsonPath("$.categoria", is(produto.getCategoria().getDescricao())));

            then(produtoService).should().add(any(Produto.class));
            then(produtoService).shouldHaveNoMoreInteractions();
        }

        @Test
        void deleteProduto() throws Exception {
            //when
            mockMvc.perform(delete("/produtos/" + anyInt()))
                    .andExpect(status().isOk());

            //then
            then(produtoService).should().delete(anyInt());
            then(produtoService).shouldHaveNoMoreInteractions();
        }

        @Test
        void updateProduto() throws Exception {
            //given
            Produto expectedProduto = Produto.builder()
                    .produtoDescricao("VACA ATOLADA")
                    .produtoValor(15.0)
                    .categoria(new Categoria("BRASILEIRA"))
                    .build();

            String requestJson = ow.writeValueAsString(
                mapStructMapperProdutos.produtoToProdutoDTO(expectedProduto)
            );

            given(produtoService.update(any(Produto.class)))
                    .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));


            //when
            mockMvc.perform(MockMvcRequestBuilders.put("/produtos/" + produto.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.produtoId", is(produto.getId())))
                    .andExpect(jsonPath("$.produtoDescricao", is(expectedProduto.getProdutoDescricao())))
                    .andExpect(jsonPath("$.produtoValor", is(expectedProduto.getProdutoValor())))
                    .andExpect(jsonPath("$.categoria", is(expectedProduto.getCategoria().getDescricao())));

        }
    }

    @Nested
    public class CategoriaTests {

        @Captor
        ArgumentCaptor<Categoria> categoriaArgumentCaptor;

        List<Categoria> categorias = new ArrayList<>();

        Categoria categoria;

        Categoria categoria2;

        @BeforeEach
        void setUp() {
            given(mapStructMapperProdutos.categoriaToCategoriaDTO(categoriaArgumentCaptor.capture()))
                    .willAnswer(
                    invocationOnMock -> {
                        Categoria categoria = invocationOnMock.getArgument(0);
                        return new CategoriaDTO(categoria.getDescricao());
                    }
            );

            categoria = new Categoria("ALEMA");
            categoria2 = new Categoria("BRASILEIRA");

            categorias.add(categoria);
            categorias.add(categoria2);
        }

        @AfterEach
        void tearDown() {
            reset(categoriaService);
        }

        @Test
        void findAll() throws Exception {
            //Given
            given(categoriaService.findAll()).willReturn(categorias);


            //when
            mockMvc.perform(MockMvcRequestBuilders.get("/produtos/categorias")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$.[0].descricao", is(categorias.get(0).getDescricao())))
                    .andExpect(jsonPath("$.[1].descricao", is(categorias.get(1).getDescricao())));

            //then
            then(categoriaService).should(times(1)).findAll();
            then(categoriaService).shouldHaveNoMoreInteractions();
        }

    }

}