package com.ceos22.springcgv.dto.cinema;

import com.ceos22.springcgv.domain.Cinema;
import lombok.Getter;

@Getter
public class CinemaDto {
    private final Long cinemaId;
    private final String name;
    private final String address;

    public CinemaDto(Cinema cinema) {
        this.cinemaId = cinema.getId();
        this.name = cinema.getName();
        this.address = cinema.getAddress();
    }
}