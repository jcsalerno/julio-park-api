package com.julio.park_api.web.dto.mapper;

import com.julio.park_api.entity.ClienteVaga;
import com.julio.park_api.web.dto.EstacionamentoCreateDto;
import com.julio.park_api.web.dto.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return modelMapper.map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
        return modelMapper.map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
