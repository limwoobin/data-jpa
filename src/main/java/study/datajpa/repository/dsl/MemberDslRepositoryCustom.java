package study.datajpa.repository.dsl;

import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;

import java.util.List;

public interface MemberDslRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
