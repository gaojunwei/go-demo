package com.ahucoding.rocket.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {


	public record Book(List<String> isbn, String title, List<String> authorName) {
	}

	@Tool(description = "Get list of Books by title")
	public List<Book> getBooks(String title) {
		System.out.printf("调用了 根据书名获取数的列表 > title=%s\n", title);
		// 这里模拟查询DB操作
		return List.of(new Book(List.of("ISBN-88888888888"), "SpringAI教程", List.of("红专写的书")));
	}

	@Tool(description = "Get book titles by author")
	public List<String> getBookTitlesByAuthor(String authorName) {
		System.out.printf("调用了 根据书作者获取数的列表 > title=%s\n", authorName);
		// 这里模拟查询DB操作
		return List.of(authorName+"SpringAI教程");
	}

}
