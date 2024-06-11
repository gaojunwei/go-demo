package com.devproblems.service;

import com.devproblems.controller.dto.AuthorVo;
import com.devproblems.controller.dto.BookVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class IndexService {

    public AuthorVo one(String one) {
        AuthorVo authorVo = new AuthorVo();
        authorVo.setAuthorId(101L);
        authorVo.setFirstName("java如何炼成" + (Objects.isNull(one) ? "" : one));
        authorVo.setBookVo(two());

        BookVo authorVo1 = new BookVo();
        authorVo1.setBookId(1L);
        authorVo1.setBookName("bookName 1");

        BookVo authorVo2 = new BookVo();
        authorVo2.setBookId(2L);
        authorVo2.setBookName("书名2");

        authorVo.setBookVoList(Arrays.asList(authorVo1, authorVo2));
        return authorVo;
    }

    public BookVo two() {
        BookVo authorVo = new BookVo();
        authorVo.setBookId(1L);
        authorVo.setBookName("bookName 1");
        return authorVo;
    }
}