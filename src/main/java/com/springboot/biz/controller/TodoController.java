package com.springboot.biz.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.dto.PageRequestDto;
import com.springboot.biz.dto.PageResponseDto;
import com.springboot.biz.dto.TodoDto;
import com.springboot.biz.service.TodoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {
	
	private final TodoService todoService;
	
	@GetMapping("/{tno}")
	public TodoDto get(@PathVariable(name="tno") Long tno) {
		return todoService.get(tno);
	}
	
	@GetMapping("/list")
	public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto){
		return todoService.list(pageRequestDto);
	}
	
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody TodoDto todoDto){
		Long tno = todoService.register(todoDto);
		return Map.of("TNO", tno);
	}
	
	@PutMapping("/{tno}")
	public Map<String, String> modify(@PathVariable(name="tno") Long tno, @RequestBody TodoDto todoDto) {
		todoDto.setTno(tno);
		
		todoService.modify(todoDto);
		return Map.of("RESULT", "성공");
	}
	
	@DeleteMapping("/{tno}")
	public Map<String, String> remove(@PathVariable(name= "tno") Long tno){
		todoService.remove(tno);
		return Map.of("결과", "성공");
	}
	
	
	
	

}
