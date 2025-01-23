package com.springboot.biz;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.biz.domain.Todo;
import com.springboot.biz.repository.TodoRepository;

@SpringBootTest
class BackendApplicationTests {
	
	@Autowired
	private TodoRepository todoRepository;

	@Test
	void contextLoads() {
		
		
		
//		for (int i=0; i <=100; i++) {
//			
//			Todo todo= Todo.builder()
//					.title("제목 : " + i)
//					.writer("사용자")
//					.dueDate(LocalDate.now())
//					.build();
//			todoRepository.save(todo);
//		}
	}

}
