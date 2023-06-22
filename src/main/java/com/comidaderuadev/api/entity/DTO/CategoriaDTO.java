package com.comidaderuadev.api.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {

    @Schema(description = "Nome da categoria do produto", example = "COMIDA BRASILEIRA")
    private String descricao;

    public CategoriaDTO(String descricao) {
        this.descricao = descricao;
    }

}
