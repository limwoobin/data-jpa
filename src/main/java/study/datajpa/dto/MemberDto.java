package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;
}
