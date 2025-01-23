package com.springboot.biz.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {
	// 회원정보와 함계 권한목록을 가질 수 있도록 설계
	@Id
	private String email;
	private String nickname;
	private String pw;
	private boolean social;
	
	@ElementCollection(fetch = FetchType.LAZY)   //필요할 때, 나중에 EAGET 즉시조회
	@Builder.Default
	private List<MemberRole> memberRoleList = new ArrayList<>();
	
	public void addRole(MemberRole memberRole) {
		memberRoleList.add(memberRole);
	}
	
	public void clearRole() {
		memberRoleList.clear();
	}
	
	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void changePw(String pw) {
		this.pw = pw;
	}
	
	public void changeSocial(boolean social) {
		this.social = social;
	}
	
	
	
	

}
