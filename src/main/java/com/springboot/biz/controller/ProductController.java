package com.springboot.biz.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.biz.dto.PageRequestDto;
import com.springboot.biz.dto.PageResponseDto;
import com.springboot.biz.dto.ProductDTO;
import com.springboot.biz.service.ProductService;
import com.springboot.biz.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

	
	private final CustomFileUtil fileUtil;
	private final ProductService productService;
	
	//코드 수정
	@PostMapping("/")
	public Map<String, Long> register(ProductDTO productDTO){
		
		log.info("register : " + productDTO);
		
		List<MultipartFile> files = productDTO.getFiles();
		
		List<String> uploadFileNames = fileUtil.saveFiles(files);
		
		productDTO.setUploadFileNames(uploadFileNames);
		
		log.info("업로드된 파일 이름 : " + uploadFileNames);
		
		Long pno = productService.register(productDTO);
		
		
		return Map.of("result", pno);
	}
	
	
	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET( @PathVariable(name ="fileName") String fileName){
		
		return fileUtil.getFile(fileName);
	}
	
	
	@GetMapping("/list")
	public PageResponseDto<ProductDTO> list(PageRequestDto pageRequestDTO){
		
		log.info("list--------------------" + pageRequestDTO);
		
		return productService.getList(pageRequestDTO);
	}
	
	@GetMapping("/{pno}")
	public ProductDTO read(@PathVariable(name = "pno") Long pno) {
		return productService.get(pno);
	}
	
	@PutMapping("/{pno}")
	public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDTO productDTO){
		
		productDTO.setPno(pno);
		
		ProductDTO oldProductDTO = productService.get(pno);
		
		//기존의 파일들 데베에 존재하는 파일들
		List<String> oldFileNames = oldProductDTO.getUploadFileNames();
		//새로 업로드 하는 파일들
		List<MultipartFile> files = productDTO.getFiles();
		//새로 업로드되어서 만들어진 파일들
		List<String>  currentUploadFileNames = fileUtil.saveFiles(files);
		//화면에서 변화없이 계속 유지되는 파일들
		List<String> uploadFileNames = productDTO.getUploadFileNames();
		//유지되는 파일들 + 새로 업로드 된 파일 이름들이 저장해야 하는 파일 목록 
		if(currentUploadFileNames != null && currentUploadFileNames.size() > 0 ) {
			uploadFileNames.addAll( currentUploadFileNames);
		}
		//수정
		productService.modify(oldProductDTO);
		
		if(oldFileNames != null &&  oldFileNames.size() > 0 ) {
			
			//삭제해야하는 파일 목록 찾기
			//예전 파일들 중에서 삭제할 파일이름
			List<String> removeFiles = oldFileNames
					.stream()
					.filter(fileName -> uploadFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
			//파일 삭제
			fileUtil.deleteFiles(removeFiles);
		}
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	
	@DeleteMapping("/{pno}")
	public Map<String, String> remove(@PathVariable(name = "pno") Long pno) {
		
		List<String> oldFileNames = productService.get(pno).getUploadFileNames();
		
		productService.remove(pno);
		
		fileUtil.deleteFiles(oldFileNames);
		
		return Map.of("RESULT", "SUCCESS");
	}

}




































