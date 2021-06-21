package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;
import study.datajpa.repository.dsl.MemberDslRepository;
import study.datajpa.repository.dsl.MemberJpaRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberDslController {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberDslRepository memberDslRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }

    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition condition , Pageable pageable) {
        return memberDslRepository.searchPageSimple(condition , pageable);
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchCondition condition , Pageable pageable) {
        return memberDslRepository.searchPageComplex(condition , pageable);
    }
}
