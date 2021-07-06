package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PutMapping("/member/insert/{name}")
    public String save(@PathVariable("name") String name) {
        Member member = new Member(name);
        memberRepository.save(member);
        return "OK";
    }

    @PostMapping("/member/update/{name}")
    public String update(@PathVariable("name") String name) {
        Member member = new Member(name);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(1L).get();
        findMember.setUsername("zzzzz");
        memberRepository.save(findMember);

        return "OK";
    }

    @DeleteMapping("/member/delete/{name}")
    public String delete(@PathVariable("name") String name) {
        Member member = new Member(name);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(1L).get();
        memberRepository.delete(findMember);

        return "OK";
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(member -> new MemberDto(member.getId(), member.getUsername() , member.getTeam().getName()));
    }
}
