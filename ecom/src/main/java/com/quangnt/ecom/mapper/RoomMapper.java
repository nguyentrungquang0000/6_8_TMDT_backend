package com.quangnt.ecom.mapper;

import com.quangnt.ecom.dto.RoomRequest;
import com.quangnt.ecom.dto.RoomResponse;
import com.quangnt.ecom.entity.Cinema;
import com.quangnt.ecom.entity.Room;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomRequest request);

    RoomResponse toResponse(Room room);

    @Mapping(target = "cinemaId", source = "room.cinemaId")
    @Mapping(target = "cinemaName", source = "cinemaName")
    RoomResponse toResponse(Room room, String cinemaName);

    default List<RoomResponse> toResponses(List<Room> content, Map<Integer, Cinema> cinemaMap){
        if (content == null || cinemaMap == null) {
            return null;
        }
        return content.stream()
                .map(room -> toResponse(room, cinemaMap.get(room.getCinemaId()).getName()))
                .collect(Collectors.toList());
    }

    @Mapping(target = "deleted", expression = "java(true)")
    Room delete(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Room update(@MappingTarget Room room, RoomRequest request);
}
