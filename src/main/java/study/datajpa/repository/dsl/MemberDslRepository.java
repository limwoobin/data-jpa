package study.datajpa.repository.dsl;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberDslRepository extends JpaRepository<Member, Long> , MemberDslRepositoryCustom{

    List<Member> findByUsername(String username);
}
