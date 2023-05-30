package com.demo.domain.member;

import com.demo.domain.address.Address;
import com.demo.domain.board.Board;
import com.demo.domain.order.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Column(nullable = false, length = 30, unique = true)
    private String username;//아이디
    @Column(nullable = false)
    private String password;//비밀번호

    @Column(nullable = false, length = 30)
    private String name;//실명
    @Column(nullable = false, length = 30)
    private String nickName;//별명

    //CascadeType.ALL : Member 엔티티를 저장할 때 연관된 모든 Board 엔티티도 함께 저장되도록 설정
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @Embedded   // Embedded, 또는 Embeddable(클래스에) 둘 중 하나의 어노테이션만 있어도 되긴 한다.
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}