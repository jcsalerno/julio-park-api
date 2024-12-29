package com.julio.park_api.web.dto.mapper;
import com.julio.park_api.entity.Cliente;
import com.julio.park_api.web.dto.ClienteCreateDto;
import com.julio.park_api.web.dto.ClienteResponseDto;
import lombok.*;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

}
