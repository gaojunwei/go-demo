package com.devproblems.controller.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorVo {

    private Long authorId;

    private String firstName;

    private BookVo bookVo;

    private List<BookVo> bookVoList;
}
