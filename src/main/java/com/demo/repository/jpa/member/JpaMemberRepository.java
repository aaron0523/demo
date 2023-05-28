package com.demo.repository.jpa.member;

import com.demo.domain.member.Member;
import com.demo.repository.support.MemberRepository;
import com.demo.repository.support.QuerydslRepositorySupport;
import com.demo.web.dto.member.MemberUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.domain.member.QMember.member;

@Repository
public class JpaMemberRepository extends QuerydslRepositorySupport implements MemberRepository {

    private final SpringDataJpaMemberRepository jpaMemberRepository;

    public JpaMemberRepository(SpringDataJpaMemberRepository jpaMemberRepository) {
        super(Member.class);
        this.jpaMemberRepository = jpaMemberRepository;
    }

    public Optional<Member> findById(Long id) {
        return jpaMemberRepository.findById(id);
        /*return Optional.ofNullable(selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne());*/
        //fetchOne() 메소드는 쿼리 결과가 하나 이상일 때는 NonUniqueResultException 예외를 던지며, 결과가 없을 때는 null을 반환
    }

    public List<Member> findAll() {
        return jpaMemberRepository.findAll();
    }

    public Optional<Member> findByUsername(String loginId) {
        return Optional.ofNullable(
                selectFrom(member)
                        .where(member.username.eq(loginId))
                        .fetchOne()
        );
    }

    public Member save(Member member) {
        return jpaMemberRepository.save(member);
    }

    public void update(Long memberId, MemberUpdateDto updateParam) {
        getQueryFactory().update(member)
                .set(member.password, updateParam.getPassword())
                .set(member.name, updateParam.getName())
                .set(member.nickName, updateParam.getNickName())
                .where(member.id.eq(memberId))
                .execute();
    }

    public long count() {
        return jpaMemberRepository.count();
    }

    public void delete(Member member) {
        jpaMemberRepository.delete(member);
    }
}

