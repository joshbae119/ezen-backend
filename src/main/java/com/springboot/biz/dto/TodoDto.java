package com.springboot.biz.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
	
	private Long tno;
	
	private String title;
	
	private String writer;
	
	private boolean complete;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd") //dto는 날짜 포멧을 맞춰야 한다.
	private LocalDate dueDate;


}
