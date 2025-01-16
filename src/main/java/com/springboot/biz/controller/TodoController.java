package com.springboot.biz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.dto.TodoDto;
import com.springboot.biz.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {
	
	private final TodoService todoService;
	
	@GetMapping("/{tno}")
	public TodoDto get(@PathVariable(name="tno") Long tno) {
		return todoService.get(tno);
	}

}
