package com.comidaderuadev.api.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.comidaderuadev.api.entity.pedido.StatusPedido;
import com.comidaderuadev.api.entity.pedido.DTO.StatusPedidoDTO;
import com.comidaderuadev.api.exceptions.produto.NotFoundException;
import com.comidaderuadev.api.repository.StatusPedidoRepository;

@RestController
@RequestMapping("/statusPedidos")
public class StatusPedidoController {

    @Autowired
    private StatusPedidoRepository statusPedidoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<StatusPedidoDTO> findAll() {
        return statusPedidoRepository
                .findAll()
                .stream()
                .map((status) -> convertToDto(status))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusPedidoDTO addStatusPedido(@RequestBody StatusPedidoDTO statusPedidoDTO) throws ParseException {
        StatusPedido statusPedido = convertToEntity(statusPedidoDTO);
        statusPedido.setId(0);
        return convertToDto(statusPedidoRepository.save(statusPedido)); 
    }

    @DeleteMapping("/{descricao}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStatusPedido(@PathVariable String descricao) {
        StatusPedido statusPedido = statusPedidoRepository.findByDescricao(descricao);
        if (statusPedido == null) throw new NotFoundException("Produto não encontrado. Descricao: " + descricao);

        statusPedidoRepository.delete(statusPedido);
    }

    private StatusPedidoDTO convertToDto(StatusPedido statusPedido) {
        return modelMapper.map(statusPedido, StatusPedidoDTO.class);
    }

    private StatusPedido convertToEntity(StatusPedidoDTO statusPedidoDTO) throws ParseException {
        StatusPedido statusPedido = statusPedidoRepository.findByDescricao(statusPedidoDTO.getDescricao());

        if (statusPedido == null) {
            return modelMapper.map(statusPedidoDTO, StatusPedido.class);
        }

        return statusPedido;

    }

}
