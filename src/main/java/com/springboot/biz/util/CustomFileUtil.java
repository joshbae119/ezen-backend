package com.springboot.biz.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {
	
	@Value("${org.zerock.upload.path}")
	private String uploadPath;
	
	@PostConstruct
	public void init() {
		File temFolder = new File(uploadPath);
		
		if(temFolder.exists()== false) {
			temFolder.mkdir();
		}
		
		uploadPath= temFolder.getAbsolutePath();
		
		log.info("-----------------------------");
		log.info(uploadPath);
	}
	
	public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
		
		if(files == null) {
			return List.of();
		}
		
		List<String> uploadNames = new ArrayList<>();
		
		for(MultipartFile multipartFile : files) {
			String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
			
			Path savePath = Paths.get(uploadPath, savedName);
			
			try {
				Files.copy(multipartFile.getInputStream(), savePath);
				uploadNames.add(savedName);
			}catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return uploadNames;
		
		
		
	}
	
	

}
