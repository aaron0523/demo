package com.demo.service.member;

import com.demo.exception.DuplicateMemberException;
import com.demo.domain.member.Member;
import com.demo.web.dto.member.MemberUpdateDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Member save(Member member) throws DuplicateMemberException;
    void update(Long memberId, MemberUpdateDto memberUpdateDto);
    void delete(Member member);
}
