package com.springboot.biz.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.biz.domain.Product;

public interface ProductRepository  extends JpaRepository<Product, Long>{

	@EntityGraph(attributePaths = "imageList")
	@Query("select p from Product p where p.pno = :pno")
	Optional<Product> selectOne(@Param("pno") Long pno);
	
	@Modifying
	@Query("update Product p set p.delFlag = :flag where p.pno = :pno")
	void updateToDelete(@Param("pno") Long pno , @Param("flag") boolean flag);
	
	
	@Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
	Page<Object[]> selectList(Pageable pageable);
}



//엔티티 그래프를 비유적으로 설명하겠습니다.
//
//비유: 쇼핑 카트와 고객 정보
//
//상황: 당신은 온라인 쇼핑몰에서 쇼핑 카트와 고객 정보를 처리하는 시스템을 개발 중입니다. 쇼핑 카트에는 여러 개의 상품이 담길 수 있고, 고객 정보에는 고객의 개인 정보와 주문 내역이 포함됩니다.
//
//일반적인 로딩 (N+1 문제)
//
//상황: 쇼핑 카트에 10개의 상품이 담겨 있고, 이를 데이터베이스에서 로드하려고 합니다.
//일반적인 방식:
//먼저 쇼핑 카트 정보를 가져옵니다 (1번의 쿼리).
//그 후, 각 상품 정보를 개별적으로 가져옵니다 (10번의 쿼리).
//결과: 총 11번의 쿼리가 실행됩니다. 이를 N+1 문제라고 합니다.
//엔티티 그래프 (해결책)
//
//상황: 같은 쇼핑 카트 정보를 로드하려고 합니다.
//엔티티 그래프 사용:
//쇼핑 카트와 연관된 모든 상품 정보를 한 번에 가져오는 쿼리를 실행합니다.
//결과: 단 1번의 쿼리로 쇼핑 카트와 모든 상품 정보를 가져올 수 있습니다.
//비유 설명
//
//비유 대상: 쇼핑 카트와 고객 정보.
//쇼핑 카트: 메인 엔티티 (예: Product).
//상품들: 연관된 엔티티들 (예: ProductImage).
//일반적인 로딩:
//쇼핑 카트를 가져온 후, 각 상품을 개별적으로 가져옴.
//여러 번의 데이터베이스 쿼리가 실행됨.
//엔티티 그래프 사용:
//쇼핑 카트와 모든 상품 정보를 한 번에 가져옴.
//단 1번의 데이터베이스 쿼리로 모든 관련 정보를 가져옴.
// 
//
//계산대 앞에 선 쇼핑카트를 예로 들면
//
//일반적인 로딩:
//특정 쇼핑카트를 가져옵니다.
//그 쇼핑카트에 담긴 상품들을 개별적으로 하나씩 찍어서 정보를 가져옵니다.
//여러 번의 데이터베이스 접근(쿼리)이 필요합니다.
//엔티티 그래프 사용:
//특정 쇼핑카트와 그 카트에 담긴 모든 상품을 한 번에 목록에 작성해둡니다.
//한 번에 찍어서 모든 정보를 가져옵니다.
//단 한 번의 데이터베이스 접근(쿼리)으로 필요한 모든 정보를 가져옵니다.
//이 비유는 엔티티 그래프를 사용하여 연관된 엔티티를 효율적으로 로드하고, 여러 번의 데이터베이스 쿼리를 줄여 성능을 최적화하는 방법을 잘 설명해줍니다.
//
// 
//@EntityGraph 어노테이션의 attributePaths 속성은 엔티티 그래프를 통해 페치(fetch)할 연관된 엔티티의 경로를 지정합니다. 
//즉, 어떤 연관된 엔티티를 함께 로드할지 명시하는 것입니다.
//
//비유 설명
//
//비유에서 attributePaths는 "특정 쇼핑카트와 그 카트에 담긴 모든 상품"을 지정하는 역할을 합니다.
//
//요약
//
//@EntityGraph(attributePaths = "imageList"):
//attributePaths는 엔티티 그래프를 통해 페치할 연관된 엔티티의 경로를 지정합니다.
//여기서는 Product 엔티티의 imageList 필드를 지정하여, Product와 연관된 ProductImage 엔티티들을 한 번에 로드합니다.
//이를 통해 여러 번의 데이터베이스 접근을 줄이고, 한 번의 쿼리로 필요한 모든 데이터를 가져옵니다.
//이렇게 하면 엔티티 그래프를 사용하여 연관된 엔티티를 효율적으로 로드할 수 있으며, 성능 최적화에 도움이 됩니다.

 




























