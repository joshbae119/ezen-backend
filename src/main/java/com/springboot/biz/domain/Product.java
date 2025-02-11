package com.springboot.biz.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pno;
	
	
	private String pname;
	
	private int price;
	
	private String pdesc;
	
	private boolean delFlag;
	
	
	public void changeDel(boolean delFlag) {
		this.delFlag = delFlag;
	}
	
	@ElementCollection //값 타입 객체  엔티티와 달리 PK가 없는 데이터 하나의 상품 데이터에는 여러개의 상품 이미지를 가지도록 구성
	//엔티티로는 product 라는 하아의 엔티티 객체지만 테이블에서는 2개의 테이블고 구성되기 때문에 JPA에서 이를 처리할 때 한번에 모든 테이블을 같이 로딩해서 처리하려면 (eager Loading)
	//필요한 테이블만 먼저 조회할 것인지 (lazy Loding) 를 결정할 필요가 있음 ElementCollection은 lazy Loading방식으로 동작함
	//데이터베이스에 두번 접근해야하므로 테스트파일과 service 파일에 @transcational 을 적용해야 함
	@Builder.Default
	private List<ProductImage> imageList = new ArrayList<>();
	
	public void changePrice(int price) {
		this.price = price;
	}
	
	public void changeDesc(String desc) {
		this.pdesc  = desc;
	}
	
	public void changeName(String name) {
		this.pname = name;
	}
	
	public void addImage(ProductImage image) {
		image.setOrd(this.imageList.size());
		imageList.add(image);
	}
	
	public void addImageString(String fileName) {
		
		ProductImage productImage = ProductImage.builder()
				.fileName(fileName)
				.build();
		addImage(productImage);
	}
	
	public void clearList() {
		this.imageList.clear();
	}
}