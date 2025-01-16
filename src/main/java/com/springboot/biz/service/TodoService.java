package com.springboot.biz.service;

import org.springframework.stereotype.Service;

import com.springboot.biz.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository todoRepository;
	

}
