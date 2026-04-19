package kr.ac.hansung.cse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.CategoryForm;
import kr.ac.hansung.cse.service.CategoryService;

/**
 * 카테고리 웹 요청 처리 컨트롤러
 * - GET  /categories          : 카테고리 목록
 * - GET  /categories/create   : 등록 폼
 * - POST /categories/create   : 등록 처리
 * - POST /categories/{id}/delete : 삭제 처리
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 목록 화면
     */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categoryList";
    }

    /**
     * 등록 폼 표시
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categoryForm";
    }

    /**
     * 등록 처리
     * - @Valid + BindingResult로 입력 검증
     * - 중복 시 BindingResult에 오류 등록 후 폼 재표시
     */
    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute CategoryForm categoryForm,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        // 검증 실패 (빈 값, 길이 초과 등)
        if (bindingResult.hasErrors()) {
            return "categoryForm";
        }
        try {
            categoryService.createCategory(categoryForm.getName());
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 등록되었습니다.");
        } catch (DuplicateCategoryException e) {
            // 중복 예외 → 폼 필드 오류로 변환
            bindingResult.rejectValue("name", "duplicate", e.getMessage());
            return "categoryForm";
        }
        return "redirect:/categories";
    }

    /**
     * 삭제 처리
     * - 연결 상품 있으면 Flash로 오류 메시지 전달
     */
    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/categories";
    }
}