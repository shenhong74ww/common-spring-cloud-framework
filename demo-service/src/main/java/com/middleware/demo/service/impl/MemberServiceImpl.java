package com.middleware.demo.service.impl;

import com.google.common.collect.Lists;
import com.middleware.common.config.DynamicQueryFilterLogicType;
import com.middleware.common.config.DynamicQueryOperators;
import com.middleware.common.event.MemberPermissionChangedEvent;
import com.middleware.common.exception.InvalidClientIdException;
import com.middleware.common.model.PageResult;
import com.middleware.common.model.TokenResult;
import com.middleware.common.service.impl.BaseServiceImpl;
import com.middleware.common.specification.Filter;
import com.middleware.common.specification.SpecificationBuilder;
import com.middleware.common.util.StringUtil;
import com.middleware.demo.constant.enums.member.MemberType;
import com.middleware.demo.exception.company.CompanyNotExistsException;
import com.middleware.demo.exception.member.PasswordInvalidExcaption;
import com.middleware.demo.exception.member.PhoneInvalidExcaption;
import com.middleware.demo.exception.member.UserExistsException;
import com.middleware.demo.feign.AuthenticationTokenClient;
import com.middleware.demo.model.Company;
import com.middleware.demo.model.Member;
import com.middleware.demo.repository.CompanyRepository;
import com.middleware.demo.repository.MemberRepository;
import com.middleware.demo.request.SignUpRequest;
import com.middleware.demo.service.MemberService;
import com.middleware.demo.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.middleware.common.constant.Constants.AUTH_TYPE_MEMBER;

@Service
public class MemberServiceImpl extends BaseServiceImpl implements MemberService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationTokenClient authenticationTokenClient;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public TokenResult signUp(SignUpRequest signUpRequest) {
        if (StringUtils.isEmpty(signUpRequest.getPhone())) {
            throw new PhoneInvalidExcaption("phone value is invalid.");
        }
        if (StringUtils.isEmpty(signUpRequest.getPassword())) {
            throw new PasswordInvalidExcaption("password value is invalid.");
        }
        if (StringUtils.isEmpty(signUpRequest.getClientId())) {
            throw new InvalidClientIdException("clientId value is invalid.");
        }

        Member member = getMemberByPhoneAndRegionCode(signUpRequest.getPhone(), signUpRequest.getRegionCode());

        if (member == null) {
            member = new Member();
            member.setCompanySid(signUpRequest.getCompanySid());
            member.setType(signUpRequest.getMemberType());
            member.setPhone(signUpRequest.getPhone());
            member.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            member.setRegionCode(signUpRequest.getRegionCode());

            if (signUpRequest.getBirthday() != null) {
                member.setBirthday(signUpRequest.getBirthday());
            }

            if (!StringUtil.isEmpty(signUpRequest.getEmail())) {
                member.setEmail(signUpRequest.getEmail());
            }

            if (!StringUtil.isEmpty(signUpRequest.getUsername())) {
                member.setUsername(signUpRequest.getUsername());
            }

            if (signUpRequest.getRegionCode() != null) {
                member.setRegionCode(signUpRequest.getRegionCode());
            } else {
                member.setRegionCode(86);
            }

        } else {
            throw new UserExistsException("Member already exists.");
        }

        memberRepository.save(member);

        return authenticationTokenClient.getToken(AUTH_TYPE_MEMBER, signUpRequest.getClientId(), signUpRequest.getRegionCode(),
                signUpRequest.getPhone(), signUpRequest.getPassword()).getResult();
    }

    @Override
    public Member getMemberByPhoneAndRegionCode(String phone, Integer regionCode) {
        return memberRepository.findByPhoneAndRegionCode(phone, regionCode);
    }

    @Override
    public PageResult<MemberVo> list(Integer pageIndex, Integer pageSize, String sortBy, Long companySid, List<MemberType> memberTypeList) {
        Company company = companySid == null ? null : companyRepository.findById(companySid).orElse(null);
        if (companySid != null && company == null) {
            throw new CompanyNotExistsException("Company not exists");
        }

        Sort sort = super.multipleConditionsSort(sortBy, Member.class);

        Pageable page = null;
        if (sort == null) {
            page = new PageRequest(pageIndex, pageSize, null);
        } else {
            page = new PageRequest(pageIndex, pageSize, sort);
        }

        Filter filter = new Filter();
        filter.setLogic(DynamicQueryFilterLogicType.AND);
        List<Filter> filters = Lists.newArrayList();
        filter.setFilters(filters);

        if (companySid != null) {
            Filter companySidFilter = new Filter("companySid", DynamicQueryOperators.EQUAL, companySid);
            filters.add(companySidFilter);
        }

        if (memberTypeList != null && memberTypeList.size() > 0) {
            Filter memberTypeListFilter = new Filter("type", DynamicQueryOperators.IN, memberTypeList);
            filters.add(memberTypeListFilter);
        }

        Page<Member> members;

        if (filter.getFilters().size() > 0) {
            members = SpecificationBuilder.selectFrom(memberRepository).where(filter).findPage(page);
        } else {
            members = SpecificationBuilder.selectFrom(memberRepository).findPage(page);
        }

        PageResult<MemberVo> pageResult = new PageResult<>();
        pageResult.setPageIndex(members.getNumber());
        pageResult.setPageSize(members.getSize());
        pageResult.setTotalCount(members.getTotalElements());
        pageResult.setTotalPages(members.getTotalPages());

        pageResult.setData(members.getContent()
                .stream()
                .map(this::member2memberVo)
                .collect(Collectors.toList()));

        applicationEventPublisher.publishEvent(new MemberPermissionChangedEvent(new Member(), "member", "authentication", new Long(1), MemberType.FREE.name(), MemberType.PAID.name()));

        return pageResult;
    }

    private MemberVo member2memberVo(Member member) {
        MemberVo memberVo = new MemberVo();
        memberVo.setBirthday(member.getBirthday());
        memberVo.setCompany(member.getCompany());
        memberVo.setCompanySid(member.getCompanySid());
        memberVo.setEmail(member.getEmail());
        memberVo.setMemberType(member.getType());
        memberVo.setPhone(member.getPhone());
        memberVo.setRegionCode(member.getRegionCode());
        memberVo.setUsername(member.getUsername());

        return memberVo;
    }
}
