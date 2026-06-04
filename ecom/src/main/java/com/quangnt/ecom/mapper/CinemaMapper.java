package com.quangnt.ecom.mapper;

import com.quangnt.ecom.dto.CinemaRequest;
import com.quangnt.ecom.dto.CinemaResponse;
import com.quangnt.ecom.entity.Cinema;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    Cinema toEntity(CinemaRequest request);

    default List<CinemaResponse> toResponses(List<Cinema> cinemas){
        return cinemas.stream().map(this::toResponse).toList();
    }

    CinemaResponse toResponse(Cinema cinema);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cinema update(@MappingTarget Cinema cinema, CinemaRequest request);

    @Mapping(target = "deleted", expression = "java(true)")
    Cinema delete(Cinema cinema);
}
