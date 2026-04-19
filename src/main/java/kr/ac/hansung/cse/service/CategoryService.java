package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 카테고리 비즈니스 로직 처리 서비스
 * - 클래스 레벨 @Transactional(readOnly = true): 기본은 읽기 전용 트랜잭션
 * - 쓰기 메서드는 @Transactional로 오버라이드
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 생성자 주입 (Spring이 CategoryRepository 빈을 주입)
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리 조회
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 카테고리 등록
     * - 동일 이름이 이미 있으면 DuplicateCategoryException 발생
     */
    @Transactional
    public Category createCategory(String name) {
        // 중복 검사
        categoryRepository.findByName(name)
                .ifPresent(c -> {
                    throw new DuplicateCategoryException(name);
                });
        // 신규 카테고리 저장 (Category의 기본 생성자는 JPA 전용 protected → name 생성자 사용)
        return categoryRepository.save(new Category(name));
    }

    /**
     * 카테고리 삭제
     * - 연결된 상품이 있으면 IllegalStateException 발생
     */
    @Transactional
    public void deleteCategory(Long id) {
        long count = categoryRepository.countProductsByCategoryId(id);
        if (count > 0) {
            throw new IllegalStateException(
                    "상품 " + count + "개가 연결되어 있어 삭제할 수 없습니다.");
        }
        categoryRepository.delete(id);
    }
}