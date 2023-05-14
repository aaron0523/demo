package com.demo.domain.repository;

import com.demo.domain.member.Member;
import com.demo.domain.member.MemberType;
import com.demo.domain.support.Querydsl4RepositorySupport;
import com.demo.web.member.form.MemberUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.domain.member.QMember.member;

@Repository
public class JpaMemberRepository extends Querydsl4RepositorySupport {

    public JpaMemberRepository() {
        super(Member.class);
    }

    public Member findById(Long id) {
        return selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
        //fetchOne() 메소드는 쿼리 결과가 하나 이상일 때는 NonUniqueResultException 예외를 던지며, 결과가 없을 때는 null을 반환
    }

    public List<Member> findAll() {
        return selectFrom(member).fetch();
    }

    public Optional<Member> findByUsername(String loginId) {
        return Optional.ofNullable(
                selectFrom(member)
                        .where(member.username.eq(loginId))
                        .fetchOne()
        );
    }

    public void save(Member newMember) {
//        getQueryFactory().insert(member)
//                .set(member.memberType, MemberType.NOMAL)
//                .set(member.username, newMember.getUsername())
//                .set(member.password, newMember.getPassword())
//                .set(member.name, newMember.getName())
//                .set(member.nickName, newMember.getNickName())
//                .execute();
        newMember.setMemberType(MemberType.NOMAL);
        getEntityManager().persist(newMember);
    }

    public void update(Long memberId, MemberUpdateDto updateParam) {
        getQueryFactory().update(member)
                .set(member.name, updateParam.getName())
                .set(member.nickName, updateParam.getNickName())
                .where(member.id.eq(memberId))
                .execute();
    }

    public long count() {
        return selectFrom(member)
                .fetchCount();
    }


}
