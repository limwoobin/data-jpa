package study.datajpa.repository.dsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.datajpa.dto.MemberSearchCondition;
import study.datajpa.dto.MemberTeamDto;

import java.util.List;

public interface MemberDslRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
