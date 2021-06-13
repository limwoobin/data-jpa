package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("AAA" , 10);
        Member member2 = new Member("AAA" , 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA" , 15);
        Member findMember = result.get(0);
        
        assertThat(findMember.getUsername()).isEqualTo("AAA");
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("AAA" , 10);
        Member member2 = new Member("BBB" , 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

        assertThat(usernameList.get(0)).isEqualTo("AAA");
        assertThat(usernameList.get(1)).isEqualTo("BBB");
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA" , 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
            assertThat(dto.getTeamName()).isEqualTo("teamA");
            assertThat(dto.getUsername()).isEqualTo("AAA");
            assertThat(dto.getId()).isEqualTo(1);
        }
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("AAA" , 10);
        Member member2 = new Member("BBB" , 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(List.of("AAA" , "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("AAA" , 10);
        Member member2 = new Member("BBB" , 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findListByUsername("AAA");
        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> memberOptional = memberRepository.findOptionalByUsername("AAA");
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1" , 10));
        memberRepository.save(new Member("member2" , 10));
        memberRepository.save(new Member("member3" , 10));
        memberRepository.save(new Member("member4" , 10));
        memberRepository.save(new Member("member5" , 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0 , 3 , Sort.by(Sort.Direction.DESC , "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age , pageRequest);
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId() , member.getUsername() , null));

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);
        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void slice() {
        // given
        memberRepository.save(new Member("member1" , 10));
        memberRepository.save(new Member("member2" , 10));
        memberRepository.save(new Member("member3" , 10));
        memberRepository.save(new Member("member4" , 10));
        memberRepository.save(new Member("member5" , 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0 , 3 , Sort.by(Sort.Direction.DESC , "username"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age , pageRequest);

        // then
        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
}