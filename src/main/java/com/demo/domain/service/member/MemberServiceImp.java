package com.demo.domain.service.member;

import com.demo.domain.member.Member;
import com.demo.domain.repository.jpa.JpaMemberRepository;
import com.demo.web.controller.member.form.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImp implements MemberService {

    private final JpaMemberRepository jpaMemberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Member join(Member member) {
        validateDuplicateMember(member);
        log.info("=========> 회원가입 검증까지는 성공");
        jpaMemberRepository.save(member);
        log.info("=========> 회원 가입 = {}", member.getUsername());
        return member;
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = jpaMemberRepository.findByUsername(member.getUsername());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findAll() {
        return jpaMemberRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(jpaMemberRepository.findById(id).orElse(null));
    }


    /**
     * 회원 username 으로 조회
     */
    public Optional<Member> findByUsername(String loginId) {
        return Optional.ofNullable(jpaMemberRepository.findByUsername(loginId).orElse(null));
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long id, MemberUpdateDto updateParam) {
        Member findMember = jpaMemberRepository.findById(id).orElse(null);
        if (findMember != null) {
            jpaMemberRepository.update(id, updateParam);
        }
    }

    @Transactional
    public void delete(Member member) {
        jpaMemberRepository.delete(member);
    }
}