package com.demo.web.member;

import com.demo.domain.member.Member;
import com.demo.domain.service.MemberService;
import com.demo.web.SessionConst;
import com.demo.web.member.form.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editForm(@ModelAttribute Member member, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return "redirect:/";
        }
        member.setId(loggedInMember.getId());
        member.setUsername(loggedInMember.getUsername());
        member.setName(loggedInMember.getName());
        member.setNickName(loggedInMember.getNickName());

        return "members/editMemberForm";
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("member") Member member, BindingResult bindingResult,
                         @RequestParam("confirmPassword") String confirmPassword, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "members/editMemberForm";
        }

        // 현재 로그인된 회원 정보 가져오기
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return "redirect:/";
        }

        // 비밀번호 확인
        if (!member.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "비밀번호가 일치하지 않습니다.");
            return "members/editMemberForm";
        }

        // 회원 정보 업데이트
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto(
                member.getPassword(),
                member.getName(),
                member.getNickName()
        );
        memberService.update(member.getId(), memberUpdateDto);

        // 현재 세션 업데이트
        loggedInMember.setName(member.getName());
        loggedInMember.setNickName(member.getNickName());
        loggedInMember.setPassword(member.getPassword());

        session.setAttribute(SessionConst.LOGIN_MEMBER, loggedInMember);

        return "loginHome";
    }
}
