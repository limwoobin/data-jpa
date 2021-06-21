package study.datajpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectionMemberDto {
    private String username;
    private int age;

    @QueryProjection
    public ProjectionMemberDto(String username , int age) {
        this.username = username;
        this.age = age;
    }
}
