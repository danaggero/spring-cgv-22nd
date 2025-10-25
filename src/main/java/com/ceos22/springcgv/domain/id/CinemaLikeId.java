package com.ceos22.springcgv.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class CinemaLikeId implements Serializable { // 복합 기본키 클래스

    private Long user;

    private Long cinema;

}