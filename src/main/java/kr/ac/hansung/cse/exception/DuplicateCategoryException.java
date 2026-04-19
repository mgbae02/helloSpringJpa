package kr.ac.hansung.cse.exception;

/**
 * 카테고리 이름이 중복될 때 발생하는 커스텀 예외
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("이미 존재하는 카테고리입니다: " + name);
    }
}