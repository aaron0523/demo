package com.demo.domain.service;

import com.demo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberService memberService;

    /**
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {
        Member findMember = memberService.findByUsername(loginId);
        if(findMember.getPassword().equals(password)) {
            return findMember;
        }
        return null;
    }
}
