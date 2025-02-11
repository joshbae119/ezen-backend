package com.springboot.biz.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.biz.domain.Product;
import com.springboot.biz.domain.ProductImage;
import com.springboot.biz.dto.PageRequestDto;
import com.springboot.biz.dto.PageResponseDto;
import com.springboot.biz.dto.ProductDTO;
import com.springboot.biz.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductService {

	
	private final ProductRepository productRepository;
	
	
	public PageResponseDto<ProductDTO> getList(PageRequestDto pageRequestDTO){
		
		log.info("getList----------------------");
		
		Pageable pageable = PageRequest.of(
				pageRequestDTO.getPage() -1 , //페이지 시작번호가 0부터 시작하므로 -1을 해준다
				pageRequestDTO.getSize(),
				Sort.by("pno").descending());
		
		Page<Object[]> result = productRepository.selectList(pageable);
		
//		ProductRepository를 통해서 Page<Object[]> 타입의 결과 데이터를 가져온다
//		각 Object[] 의 내용물은 Product 객체와 ProductImage 객체
//		반복처리로 Product와 ProductImage를 ProductDTO 타입으로 변환
//		변환된 ProductDTO를 List<ProductDTO> 로 처리하고 전체 데이터의 개수를 이용해서 PageResponseDTO 타입으로 생성하고 반환시킴
		
		List<ProductDTO> dtoList = result.get().map(arr -> {
			
			Product product = (Product) arr[0];
			ProductImage productImage = (ProductImage) arr[1];
			
			ProductDTO productDTO = ProductDTO.builder()
					.pno(product.getPno())
					.pname(product.getPname())
					.pdesc(product.getPdesc())
					.price(product.getPrice())
					.build();
			
			String imageStr = productImage.getFileName();
			productDTO.setUploadFileNames(List.of(imageStr));
			
			return productDTO;
 		}).collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		return PageResponseDto.<ProductDTO>withAll()
				.dtoList(dtoList)
				.totalCount(totalCount)
				.pageRequestDTO(pageRequestDTO)
				.build();
	}
	
	public Long register(ProductDTO productDTO) {
		
		Product product = dtoToEntity(productDTO);
		
		Product result = productRepository.save(product);
		
		return result.getPno();
	}
	
	private Product dtoToEntity(ProductDTO productDTO) {
		
		Product product = Product.builder()
				.pno(productDTO.getPno())
				.pname(productDTO.getPname())
				.pdesc(productDTO.getPdesc())
				.price(productDTO.getPrice())
				.build();
		
		List<String>  uploadFileNames = productDTO.getUploadFileNames();
		
		if(uploadFileNames == null) {
			return product;
		}
		
		uploadFileNames.stream().forEach(uploadName -> {
			
			product.addImageString(uploadName);
		});
		
		return product;
	}
	
	public ProductDTO get(Long pno) {
		
		Optional<Product> result = productRepository.selectOne(pno);
		
		Product product = result.orElseThrow();
		
		ProductDTO productDTO = entityToDTO(product);
		
		return productDTO;
	}
	
	private ProductDTO entityToDTO(Product product) {
		
		ProductDTO productDTO = ProductDTO.builder()
				.pno(product.getPno())
				.pname(product.getPname())
				.pdesc(product.getPdesc())
				.price(product.getPrice())
				.build();
		
		List<ProductImage> imageList = product.getImageList();
		
		if(imageList == null ||  imageList.size() == 0) {
			return productDTO;
		}
		
		List<String> fileNameList = imageList.stream().map(productImage -> 
		productImage.getFileName()).toList();
		
		productDTO.setUploadFileNames(fileNameList);
		
		return productDTO;
	}
	
	public void modify(ProductDTO productDTO) {
		
		
		Optional<Product> result = productRepository.findById(productDTO.getPno());
		
		Product product = result.orElseThrow();
		
		product.changeName(productDTO.getPname());
		product.changeDesc(productDTO.getPdesc());
		product.changePrice(productDTO.getPrice());
		
		product.clearList();
		
		List<String> uploadFileNames = productDTO.getUploadFileNames();
		
		if(uploadFileNames != null  &&  uploadFileNames.size() > 0 ) {
			uploadFileNames.stream().forEach(uploadName -> {
				product.addImageString(uploadName);
			});
		}
		
		productRepository.save(product);
	}
	
	public void remove(Long pno) {
		
		productRepository.updateToDelete(pno, true);
	}
}
