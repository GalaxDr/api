package com.comidaderuadev.api.controller;

import java.text.ParseException;
import java.util.List;

import com.comidaderuadev.api.entity.mapper.MapStructMapperProdutos;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.comidaderuadev.api.entity.produto.Categoria;
import com.comidaderuadev.api.entity.produto.Produto;
import com.comidaderuadev.api.entity.DTO.CategoriaDTO;
import com.comidaderuadev.api.entity.DTO.ProdutoDTO;
import com.comidaderuadev.api.service.CategoriaService;
import com.comidaderuadev.api.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private ProdutoService produtoService;

    private CategoriaService categoriaService;

    private MapStructMapperProdutos mapStructMapperProdutos;

    public ProdutoController(ProdutoService produtoService,
                             CategoriaService categoriaService,
                             MapStructMapperProdutos mapStructMapperProdutos) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.mapStructMapperProdutos = mapStructMapperProdutos;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProdutoDTO> findAll() {
        return produtoService
                .findAll()
                .stream()
                .map(p -> mapStructMapperProdutos.produtoToProdutoDTO(p))
                .toList();

    }

    @GetMapping("/categorias")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaDTO> findAllCategorias() {
        return categoriaService
                .findAll()
                .stream()
                .map(c -> mapStructMapperProdutos.categoriaToCategoriaDTO(c))
                .toList();
    }

    @GetMapping("/{produtoId}")
    public ProdutoDTO findById(@PathVariable int produtoId) {
        Produto p = produtoService.findById(produtoId);
        return mapStructMapperProdutos.produtoToProdutoDTO(p);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDTO addProduto(@Valid @RequestBody ProdutoDTO produtoDTO) throws ParseException {
        Produto produto = mapStructMapperProdutos.produtoDTOToProduto(produtoDTO, categoriaService);
        Produto ProdutoCriado = produtoService.add(produto);
        return mapStructMapperProdutos.produtoToProdutoDTO(ProdutoCriado);
    }

    @PutMapping("/{produtoId}")
    public ProdutoDTO updateProduto(@Valid @RequestBody ProdutoDTO produtoDTO, @PathVariable int produtoId) throws ParseException {
        Produto produto = mapStructMapperProdutos.produtoDTOToProduto(produtoDTO, categoriaService);
        produto.setId(produtoId);
        Produto produtoAtualizado = produtoService.update(produto);
        return mapStructMapperProdutos.produtoToProdutoDTO(produtoAtualizado);
    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduto(@PathVariable int produtoId) {
        produtoService.delete(produtoId);
    }

}
