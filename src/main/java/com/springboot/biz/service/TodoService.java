package com.springboot.biz.service;

import com.springboot.biz.domain.Todo;
import com.springboot.biz.dto.TodoDto;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.biz.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository todoRepository;
	
	private final ModelMapper modelMapper;
	
	public Long register(TodoDto todoDto) {
		Todo todo = modelMapper.map(todoDto, Todo.class);
		Todo savedTodo = todoRepository.save(todo);
		
		return savedTodo.getTno();
		
	}
	
	public TodoDto get(Long tno) {
		Optional<Todo> result = todoRepository.findById(tno);
		Todo todo = result.orElseThrow();
		
		TodoDto dto = modelMapper.map(todo, TodoDto.class);
		
		return dto;
	}
	

}
