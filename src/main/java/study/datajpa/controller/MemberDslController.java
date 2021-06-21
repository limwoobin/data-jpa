package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;
import study.datajpa.repository.dsl.MemberJpaRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberDslController {
    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
