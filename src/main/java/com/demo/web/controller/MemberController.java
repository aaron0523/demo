package com.demo.web.controller;

import com.demo.domain.address.Address;
import com.demo.exception.DuplicateMemberException;
import com.demo.exception.MemberNotFoundException;
import com.demo.domain.member.Member;
import com.demo.service.member.MemberService;
import com.demo.web.SessionConst;
import com.demo.web.dto.member.MemberJoinDto;
import com.demo.web.dto.member.MemberUpdateDto;
import com.demo.web.validator.member.MemberJoinDtoValidator;
import com.demo.web.validator.member.MemberUpdateDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberJoinDtoValidator memberJoinDtoValidator;
    private final MemberUpdateDtoValidator memberUpdateDtoValidator;

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        MemberJoinDto memberJoinDto = (MemberJoinDto) session.getAttribute("memberJoinDto");
        if (memberJoinDto == null) {
            memberJoinDto = new MemberJoinDto();
        }

        model.addAttribute("memberJoinDto", memberJoinDto);
        return "member/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute MemberJoinDto memberJoinDto, BindingResult bindingResult,
                       Model model, HttpSession session) {
        // 폼 입력값 검증
        memberJoinDtoValidator.validate(memberJoinDto, bindingResult);
        if (bindingResult.hasErrors()) {
            // 입력값이 유효하지 않을 경우 세션에 값 저장
            session.setAttribute("memberJoinDto", memberJoinDto);
            // 검증 후 return 은 redirect 하면 안된다.
            return "member/addMemberForm";
        }

        // 회원 정보 저장
        Member member = new Member();
        member.setUsername(memberJoinDto.getUsername());
        member.setPassword(memberJoinDto.getPassword());
        member.setName(memberJoinDto.getName());
        member.setNickName(memberJoinDto.getNickName());

        Address address = new Address(memberJoinDto.getCity(), memberJoinDto.getStreet(), memberJoinDto.getZipcode());
        member.setAddress(address);

        member.setCreatedDate(LocalDateTime.now());

        try {
            memberService.save(member);
        } catch (DuplicateMemberException e) {
            // 입력값이 유효하지 않을 경우 세션에 값 저장
            session.setAttribute("memberJoinDto", memberJoinDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMemberForm";
        }
        log.info("로그인 ID =========> {}", Optional.ofNullable(memberJoinDto).map(MemberJoinDto::getUsername).orElse(""));
//        log.info("로그인 ID =========> {}", memberJoinDto != null ? (memberJoinDto.getUsername() != null ? memberJoinDto.getUsername() : "") : "");

        return "home";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return "redirect:/";
        }

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        memberUpdateDto.setId(loggedInMember.getId());
        memberUpdateDto.setUsername(loggedInMember.getUsername());
        memberUpdateDto.setName(loggedInMember.getName());
        memberUpdateDto.setNickName(loggedInMember.getNickName());

        memberUpdateDto.setCity(loggedInMember.getAddress().getCity());
        memberUpdateDto.setStreet(loggedInMember.getAddress().getStreet());
        memberUpdateDto.setZipcode(loggedInMember.getAddress().getZipcode());

        model.addAttribute("memberUpdateDto", memberUpdateDto);

        return "member/editMemberForm";
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult,
                         HttpSession session, Model model) {
        // 폼 입력값 검증
        memberUpdateDtoValidator.validate(memberUpdateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "member/editMemberForm";
        }

        // 현재 로그인된 회원 정보 가져오기
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return "redirect:/";
        }

        // 회원 정보 업데이트
        memberUpdateDto.setUpdatedDate(LocalDateTime.now());
        // 주소 정보 업데이트
        Address address = new Address(memberUpdateDto.getCity(), memberUpdateDto.getStreet(), memberUpdateDto.getZipcode());
        loggedInMember.setAddress(address);

        try {
            memberService.update(loggedInMember.getId(), memberUpdateDto);
        } catch (MemberNotFoundException e) {
            session.setAttribute("memberUpdateDto", memberUpdateDto);
            model.addAttribute("errorMessage", e.getMessage());
        }
        // 현재 세션 업데이트
        loggedInMember.setName(memberUpdateDto.getName());
        loggedInMember.setNickName(memberUpdateDto.getNickName());
        loggedInMember.setPassword(memberUpdateDto.getPassword());

        session.setAttribute(SessionConst.LOGIN_MEMBER, loggedInMember);

        return "loginHome";
    }
}
