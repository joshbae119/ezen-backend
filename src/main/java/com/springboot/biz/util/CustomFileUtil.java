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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

	//의미: 실제 파일이 저장될 디렉토리 경로를 사용자 정의 프로퍼티로 설정합니다. 이 값은 애플리케이션 내에서 @Value("${org.zerock.upload.path}") 등으로 주입받아 사용할 수 있습니다.
	@Value("${org.zerock.upload.path}")
	private String uploadPath;
	
	@PostConstruct //의존성 주입을 먼저 수행하고 클래스 객체가 만들어 져야 할때 사용하는 어노테이션 
	//생성자가 호출되었을때 빈은 초기화되지 않았을수도 있음. 이때 어노테이션을 사용하면 의존성 주입을 완료하고 나서 매소드가 실행되므로 안전하게 실행 가능함. 즉 어플리케이션이 실행될때 한번만 실행됨
	public void init() {
		//지정한 경로에 파일 폴터를 만들어줌
		File tempFolder = new File(uploadPath);
		//exists() 파일이 존재하는지 검사하는 메소드
		if(tempFolder.exists() == false) {
			//mkdir() 파일에 지정된 경로로 폴터를 생성 성공하면 true 반환
			tempFolder.mkdir();
		}
		
		//getAbsolutePath() 파일의 절대 경로를  String 으로 반환
		uploadPath = tempFolder.getAbsolutePath();
		
		log.info("-------------------------------");
		log.info(uploadPath);
	}
	
	//파일 저장
	public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
		
		if(files == null || files.size() == 0 ) {
			return List.of();
		}
		
		List<String> uploadNames = new ArrayList<>();
		
		for(MultipartFile multipartFile : files) {
			//UUID (Universally Unique Identifier)  다른 값과 중복되지 않는 유니크한 값을 생성할땨 사용하는 난수 생성기  실제로는 중복된 값이 발생할수 있지만 굉장히 희박해서 사실상 고유값이라고 판단하고 사용
			String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
			
			//저장할 파일의 경로 생성 uploadPathsms 는 파일을 저장할 디텍토리경로, savedName 은 저장할 파일의 이름
			Path savePath = Paths.get(uploadPath, savedName);
			
//			MultipartFile 인터페이스
//			getName() : 넘어온 파라미터 명
//			getOriginalFilename() : 업로드 파일명
//			getContentType : 파일의 ContentType
//			isEmpty() : 업로드된 파일이 비어있는지 확인
//			getSize() : 파일의 바이트 사이즈
//			getBytes() : 바이트 배열로 저장된 파일의 내용
//			getInputStream() : 파일의 내용을 읽기 위한 InputStream 반환
//			transferTo() : 파일 저장
			
			try {
				//multipartFile의 입력 스트림에서 읽어와서 savePath로 복사한다. 즉, 업로드된 파일을 저장 경로에 복사하여 저장
				Files.copy(multipartFile.getInputStream(), savePath);
				
				String contentType = multipartFile.getContentType();
				//파일 형식이 이미지 인지 확인
				if(contentType != null && contentType.startsWith("image")) {
					
					Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
					//썸네일을 생성할 이미지 파일을 지정  savePath.toFile()는 원본 이미지 파일을 가리키는 File 객체 이를 통해 써멘일을 생성할 원본 이미지를 지정할수 있다
					Thumbnails.of(savePath.toFile())
					.size(200, 200)
					//실제 썸내일을 저장할 파일의 경로를 지정하여 실제로 썸내일을 생성학고 저장한다.
					.toFile(thumbnailPath.toFile());
					
				}
				
				uploadNames.add(savedName);
				
			}catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return uploadNames;
	}
	
	//파일 조회 
	//Resource 스프링 프레임워크에서 제공하는 추상화된 리소스 인터페이스, 다양한 유형의 리소소를 표현할수 있다 
	public ResponseEntity<Resource> getFile(String fileName){
		//uploadPath + File.separator +fileName 이 경로의 파일을 가져오는 역할을 함다  File.separator은 / 를 의미한다  즉 Resource 객체를 이용하여 C:/upload/aaa.jpg 경로의 파일을 읽어오는 것이다
		Resource resource = new FileSystemResource(uploadPath + File.separator +fileName);
		
//		Resource extends InputStreamSource
//		getInputStream()
//		exitst() : 리소스가 존재하는지 확인
//		isReadable() : 리소스를 읽을 수 있는지 확인
//		isFile() : 리소스가 파일인지 확인
//		isOpen() : 리소스가 열려있는지 확인
//		getDescription() : 전체 경로 포함한 파일 이름 또는 실제 URL
		if(! resource.isReadable()) {
			//검색할 이미지가 없을 경우 검색할 기본 이미지파일 저장
			resource = new FileSystemResource(uploadPath + File.separator + "default.PNG");
		}
		// HttpHeaders를 통해 해당 파일의 MIME유형을 추가해주면 된다.
		//headers 를 통해 MIME 유형을 추가해주지 않으면 클라이언트가 서버로부터 전송되는 데이터의 형식을 정확히 인식할 수 없기 때문에 문제가 발생  이미지가 깨져서 나오는 현상이 발생한다
		HttpHeaders headers = new HttpHeaders();
		
		try {
			//파일의 MIME 타입을 판별
//			🧐 MIME TYPE 이란?(Multipurpose Internet Mail Extensions)
//					MIME TYPE은 인터넷에서 전송되는 다양한 종류의 데이터를 식별하기 위한 형식
//					,주로 웹 브라우저가 웹 서버로부터 받은 데이터를 해석할 때 사용된다.
//					 
//					 예를 들어, HTML 문서의 MIME TYPE은 "text/html"이고, JPEG 이미지의 MIME TYPE은 "image/jpeg"이다.
//					 MIME TYPE은 파일의 확장자나 내용에 따라 결정됩니다.
//					 
//					MIME TYPE은 HTTP 헤더에서 Content-Type 등으로 지정됩니다.
				//probeContentType 을 통해 해달 파일의 mime 파일 형식을 알아낼수 있음 	
			headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
		}catch (Exception e) {
			//internalServerError() 응용 프로그램이나 서버 내부에서 오류가 발생했을때 발생하는 응듭 코드를 만드는 메서드
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	//파일 삭제
	public void deleteFiles(List<String> fileNames) {
		
		if(fileNames == null ||  fileNames.size() == 0) {
			return;
		}
		
		fileNames.forEach(fileName -> {
			
			//썸네일이 있는지 확인하고 삭제
			String thumbnailFileName = "s_" + fileNames;
			Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
			Path filePath = Paths.get(uploadPath, fileName);
			
			try {
				Files.deleteIfExists(filePath);
				Files.deleteIfExists(thumbnailPath);
				}catch(IOException e) {
					throw new RuntimeException(e.getMessage());
				}
		});
	}
}













