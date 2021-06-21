package study.datajpa.repository.dsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;
import study.datajpa.dto.QMemberTeamDto;
import study.datajpa.entity.Member;

import static org.springframework.util.StringUtils.hasText;
import static study.datajpa.entity.QMember.member;
import static study.datajpa.entity.QTeam.team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class , id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m" , Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class , id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m" , Long.class)
                .getSingleResult();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findByPage(int age , int offset , int limit) {
        return em.createQuery("select m from Member m where age = :age order by m.username desc")
                .setParameter("age" , age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age" , Long.class)
                .setParameter("age" , age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 " +
                "where m.age >= :age")
                .setParameter("age" , age)
                .executeUpdate();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }

        if (hasText(condition.getUsername())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .where(builder)
                .leftJoin(member.team , team)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team , team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}
