package com.demo.domain.service.member;

import com.demo.domain.member.Member;
import com.demo.web.controller.member.form.MemberUpdateDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Member join(Member member);
    void update(Long memberId, MemberUpdateDto memberUpdateDto);
    void delete(Member member);
}
