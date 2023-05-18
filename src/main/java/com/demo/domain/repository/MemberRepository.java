package com.demo.domain.repository;

import com.demo.domain.member.Member;
import com.demo.web.dto.member.MemberUpdateDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Member save(Member member);
    void update(Long memberId, MemberUpdateDto memberUpdateDto);
    void delete(Member member);
}