package com.springboot.biz.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.biz.domain.Todo;
import com.springboot.biz.dto.PageRequestDto;
import com.springboot.biz.dto.PageResponseDto;
import com.springboot.biz.dto.TodoDto;
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
	
	public void modify(TodoDto todoDto) {
		Optional<Todo> result = todoRepository.findById(todoDto.getTno());
		
		Todo todo = result.orElseThrow();
		
		todo.setTitle(todoDto.getTitle());
		todo.setWriter(todoDto.getWriter());
		todo.setComplete(todoDto.isComplete());
		
		todoRepository.save(todo);
		
	}
	
	public void remove(Long tno) {
		todoRepository.deleteById(tno);
		
	}
	
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);
        List<TodoDto> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDto.class)) // Todo -> TodoDto 변환
                .collect(Collectors.toList()); // 변환된 객체를 리스트로 수집
        long totalCount = result.getTotalElements();
        PageResponseDto<TodoDto> responseDTO = PageResponseDto.<TodoDto>withAll()
                .dtoList(dtoList) // 변환된 TodoDto 리스트 설정
                .pageRequestDTO(pageRequestDto) // 요청 DTO 설정
                .totalCount(totalCount) // 총 데이터 개수 설정
                .build();
        return responseDTO;
    }
	

}
