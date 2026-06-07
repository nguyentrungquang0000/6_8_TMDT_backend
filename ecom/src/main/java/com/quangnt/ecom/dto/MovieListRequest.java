package com.quangnt.ecom.dto;

import java.util.List;

public record MovieListRequest (
        List<Integer> movieIds
) {

}
