package com.comidaderuadev.api.entity.mapper;

import com.comidaderuadev.api.entity.DTO.CategoriaDTO;
import com.comidaderuadev.api.entity.DTO.ProdutoDTO;
import com.comidaderuadev.api.entity.produto.Categoria;
import com.comidaderuadev.api.entity.produto.Produto;
import com.comidaderuadev.api.service.CategoriaService;
import lombok.Setter;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MapStructMapperProdutos {

    MapStructMapperProdutos INSTANCE = Mappers.getMapper(MapStructMapperProdutos.class);

    @Mapping(target = "produtoId", source = "id")
    ProdutoDTO produtoToProdutoDTO(Produto produto);

    CategoriaDTO categoriaToCategoriaDTO(Categoria categoria);

    @Mapping(target = "categoria", source = "produtoDTO.categoria", qualifiedByName = "mapStringToCategoria")
    Produto produtoDTOToProduto(ProdutoDTO produtoDTO, @Context CategoriaService service);

    /*
     Comentei pq na versão atual não estamos recebendo categorias, apenas enviando, não havendo necessidade
     de converter um DTO para entidade.
        Categoria categoriaDTOtoCategoria(CategoriaDTO categoriaDTO);
    */
    default String mapCategoriaToString(Categoria categoria) {
        return categoria.getDescricao();
    }

    @Named("mapStringToCategoria")
    default Categoria mapStringToCategoria(String categoria, @Context CategoriaService service) {
        return service.findByDescricao(categoria);
    }

}
