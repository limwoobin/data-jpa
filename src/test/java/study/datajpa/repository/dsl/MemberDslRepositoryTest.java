package study.datajpa.repository.dsl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberDslRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberDslRepository memberDslRepository;

    @Test
    public void basicTest() {
        Member member = new Member("member1" , 10);
        memberDslRepository.save(member);

        Member findMember = memberDslRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberDslRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberDslRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void searchTest() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1" , 10 , teamA);
        Member member2 = new Member("member2" , 20 , teamA);
        Member member3 = new Member("member3" , 30 , teamB);
        Member member4 = new Member("member4" , 40 , teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberDslRepository.search(condition);
        assertThat(result).extracting("username").containsExactly("member4");
    }

    @Test
    public void searchPageSimpleTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1" , 10 , teamA);
        Member member2 = new Member("member2" , 20 , teamA);
        Member member3 = new Member("member3" , 30 , teamB);
        Member member4 = new Member("member4" , 40 , teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        PageRequest pageRequest = PageRequest.of(0 , 3);

        Page<MemberTeamDto> result = memberDslRepository.searchPageSimple(condition , pageRequest);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result).extracting("username").containsExactly("member1" , "member2" , "member3");
    }

    @Test
    public void 쓰기_지연_테스트() {
        Team teamA = new Team("teamA");
        em.persist(teamA);
        System.out.println("teamA Persist ###");

        Team teamB = new Team("teamB");
        em.persist(teamB);
        System.out.println("teamA Persist ###");

        em.flush();
        em.clear();

        Team findTeamA = em.find(Team.class, teamA.getId());
        Team findTeamB = em.find(Team.class, teamB.getId());

        System.out.println("쓰기지연 여기부터 ###");

        findTeamA.setName("upt1");
        System.out.println("Update ### 1");

        findTeamB.setName("upt2");
        System.out.println("Update ### 2");

//        Team findTeamA_1 = em.find(Team.class, teamA.getId());
//        Team findTeamB_2 = em.find(Team.class, teamB.getId());
    }
}