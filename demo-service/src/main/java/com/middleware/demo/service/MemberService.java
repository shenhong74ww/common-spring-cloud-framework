package com.middleware.demo.service;

import com.middleware.common.model.PageResult;
import com.middleware.common.model.TokenResult;
import com.middleware.demo.constant.enums.member.MemberType;
import com.middleware.demo.model.Member;
import com.middleware.demo.request.SignUpRequest;
import com.middleware.demo.vo.MemberVo;

import java.util.List;

public interface MemberService {
    TokenResult signUp(SignUpRequest signUpRequest);

    Member getMemberByPhoneAndRegionCode(String phone, Integer regionCode);

    PageResult<MemberVo> list(Integer pageIndex, Integer pageSize, String sortBy, Long companySid, List<MemberType> memberTypeList);
}
