package com.demo.domain.repository.jpa;

import com.demo.domain.member.Member;
import com.demo.domain.member.MemberType;
import com.demo.domain.repository.MemberRepository;
import com.demo.domain.repository.support.QuerydslRepositorySupport;
import com.demo.web.dto.member.MemberUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.domain.member.QMember.member;

@Repository
public class JpaMemberRepository extends QuerydslRepositorySupport implements MemberRepository {

    public JpaMemberRepository() {
        super(Member.class);
    }

    public Optional<Member> findById(Long id) {
        return getJpaRepository().findById(id);
        /*return Optional.ofNullable(selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne());*/
        //fetchOne() 메소드는 쿼리 결과가 하나 이상일 때는 NonUniqueResultException 예외를 던지며, 결과가 없을 때는 null을 반환
    }

    public List<Member> findAll() {
        return getJpaRepository().findAll();
//        return selectFrom(member).fetch();
    }

    public Optional<Member> findByUsername(String loginId) {
        return Optional.ofNullable(
                selectFrom(member)
                        .where(member.username.eq(loginId))
                        .fetchOne()
        );
    }

    public Member save(Member member) {
        member.setMemberType(MemberType.NOMAL);
        getEntityManager().persist(member);
        return member;
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
        return selectFrom(member)
                .fetchCount();
    }

    public void delete(Member member) {
        getEntityManager().remove(member);
    }
}
