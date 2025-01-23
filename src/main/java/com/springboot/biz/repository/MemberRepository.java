package com.springboot.biz.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.biz.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
	
	@EntityGraph(attributePaths = {"memberRoleList"})
	@Query("select m FROM Member m WHERE m.email = :email") //JPQL JPA 전용 QSL 검색대상이 테이블이 아니라 엔티티객체
	Member getWithRoles(@Param("email") String email);

}
