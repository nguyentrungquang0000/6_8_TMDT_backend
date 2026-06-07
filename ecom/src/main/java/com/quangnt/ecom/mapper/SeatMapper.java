package com.quangnt.ecom.mapper;

import com.quangnt.ecom.dto.SeatCreateRequest;
import com.quangnt.ecom.dto.SeatResponse;
import com.quangnt.ecom.entity.Seat;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomId", source = "roomId")
    @Mapping(target = "seatNumber", source = "seatNumber")
    @Mapping(target = "rowNumber", source = "rowNumber")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "priceMultiplier", constant = "1.0")
    Seat toEntity(Integer roomId, Integer rowNumber, Integer seatNumber);

    default List<SeatResponse> toResponses(List<Seat> saved){
        return saved.stream().map(this::toResponse).toList();
    }

    SeatResponse toResponse(Seat seat);

    default List<Seat> update(Map<Integer, Seat> seatOldMap, Map<Integer, SeatCreateRequest> requestMap){
        return seatOldMap.values().stream()
            .map(seat -> {
                SeatCreateRequest request = requestMap.get(seat.getId());
                if (request == null) {
                    return seat;
                }
                return toEntity(seat, request);
            })
            .toList();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Seat toEntity(@MappingTarget Seat entity, SeatCreateRequest request);
}
