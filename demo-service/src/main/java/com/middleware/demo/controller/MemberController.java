package com.middleware.demo.controller;

import com.middleware.common.model.PageResult;
import com.middleware.common.model.ResponseEntity;
import com.middleware.common.model.TokenResult;
import com.middleware.demo.constant.enums.member.MemberType;
import com.middleware.demo.model.Member;
import com.middleware.demo.request.SignUpRequest;
import com.middleware.demo.service.MemberService;
import com.middleware.demo.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResult> signUp(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(memberService.signUp(signUpRequest));
    }

    @GetMapping("/")
    public ResponseEntity<Member> getMember(@RequestParam(value = "phone") String phone, @RequestParam(value = "regionCode", required = false) Integer regionCode) {
        Member member = null;
        member = memberService.getMemberByPhoneAndRegionCode(phone, regionCode);

        return new ResponseEntity<>(member);
    }

    @GetMapping("/list")
    public ResponseEntity<PageResult<MemberVo>> list(
            @RequestParam(value = "page_index", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "sort", required = false, defaultValue = "modifiedDate desc") String sortBy,
            @RequestParam(value = "company_sid", required = false) Long companySid,
            @RequestParam(value = "member_type", required = false) List<MemberType> memberTypeList
            ) {
        return new ResponseEntity<>(memberService.list(pageIndex, pageSize, sortBy, companySid, memberTypeList));
    }
}
