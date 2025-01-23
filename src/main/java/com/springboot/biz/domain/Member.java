package com.springboot.biz.domain;

import java.util.List;
import java.util.ArrayList;
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
    @Id
    private String email;
    private String password;
    private String nickname;
    private boolean social;
    @ElementCollection(fetch = FetchType.LAZY) // Lazy: 필요할 때만 가져옴 (Eager: 미리 다 가져옴. 즉시조회)
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
    public void changePw(String password) {
        this.password = password;
    }
    public void changeSocial(boolean social) {
        this.social = social;
    }
}