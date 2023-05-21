package com.demo.domain.service.login;

import com.demo.domain.member.Member;
import com.demo.domain.service.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberServiceImpl memberService;

    /**
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {
        Optional<Member> findMember = memberService.findByUsername(loginId);
        if (findMember.isPresent() && findMember.get().getPassword().equals(password)) {
            return findMember.get();
        }
        return null;
    }
}
