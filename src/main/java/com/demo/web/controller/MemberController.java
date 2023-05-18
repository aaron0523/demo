package com.demo.web.controller;

import com.demo.domain.member.Member;
import com.demo.domain.service.member.MemberServiceImp;
import com.demo.web.SessionConst;
import com.demo.web.dto.member.MemberJoinDto;
import com.demo.web.dto.member.MemberUpdateDto;
import com.demo.web.validator.MemberJoinDtoValidator;
import com.demo.web.validator.MemberUpdateDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberServiceImp memberService;

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("memberJoinDto", new MemberJoinDto());
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute MemberJoinDto memberJoinDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 폼 입력값 검증
        new MemberJoinDtoValidator().validate(memberJoinDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        // 회원 정보 저장
        Member member = new Member();
        member.setUsername(memberJoinDto.getUsername());
        member.setPassword(memberJoinDto.getPassword());
        member.setName(memberJoinDto.getName());
        member.setNickName(memberJoinDto.getNickName());

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            // 예외 메시지를 "errorMessage"라는 이름으로 세션에 추가
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/members/add";
        }

        return "redirect:/";
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

        model.addAttribute("memberUpdateDto", memberUpdateDto);

        return "members/editMemberForm";
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult, HttpSession session) {
        // 폼 입력값 검증
        new MemberUpdateDtoValidator().validate(memberUpdateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "members/editMemberForm";
        }

        // 현재 로그인된 회원 정보 가져오기
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return "redirect:/";
        }

        // 회원 정보 업데이트
        memberUpdateDto.getUpdatedDate().now();
        memberService.update(loggedInMember.getId(), memberUpdateDto);

        // 현재 세션 업데이트
        loggedInMember.setName(memberUpdateDto.getName());
        loggedInMember.setNickName(memberUpdateDto.getNickName());
        loggedInMember.setPassword(memberUpdateDto.getPassword());

        session.setAttribute(SessionConst.LOGIN_MEMBER, loggedInMember);

        return "loginHome";
    }
}
