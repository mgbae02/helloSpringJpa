package kr.ac.hansung.cse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 등록 폼 DTO
 * - @NotBlank: 빈 값 또는 공백만 입력 시 검증 실패
 * - @Size: 최대 50자 제한
 */
@Getter
@Setter
public class CategoryForm {

    @NotBlank(message = "카테고리 이름을 입력하세요")
    @Size(max = 50, message = "50자 이내로 입력하세요")
    private String name;
}