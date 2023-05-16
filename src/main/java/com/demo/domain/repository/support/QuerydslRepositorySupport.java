package com.demo.domain.repository.support;

import com.demo.domain.repository.jpa.SpringDataJpaMemberRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

@Repository
public abstract class QuerydslRepositorySupport {

    private final Class domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager; // 엔티티 객체의 생명주기를 관리하고, 영속성 컨텍스트와 데이터베이스 간의 상호작용을 담당.
    private JPAQueryFactory queryFactory; // Spring Framework 에서는 EntityManager 를 EntityManagerFactory 를 통해 얻는다.
    private SpringDataJpaMemberRepository springDataJpaMemberRepository; // JpaRepository

    public QuerydslRepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager, SpringDataJpaMemberRepository springDataJpaMemberRepository) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(springDataJpaMemberRepository, "springDataJpaMemberRepository must not be null!");

        JpaEntityInformation entityInformation =
                JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath path = resolver.createPath(entityInformation.getJavaType());
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.springDataJpaMemberRepository = springDataJpaMemberRepository;
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
        Assert.notNull(springDataJpaMemberRepository, "springDataJpaMemberRepository must not be null!");
    }

    protected SpringDataJpaMemberRepository getJpaRepository() {
        return springDataJpaMemberRepository;
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery) {
        JPAQuery jpaQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable,
                jpaQuery).fetch();
        return PageableExecutionUtils.getPage(content, pageable,
                jpaQuery::fetchCount);
    }

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory, JPAQuery> countQuery) {
        JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable,
                jpaContentQuery).fetch();
        JPAQuery countResult = countQuery.apply(getQueryFactory());
        return PageableExecutionUtils.getPage(content, pageable,
                countResult::fetchCount);
    }
}