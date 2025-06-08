package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.CategoryRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model, Pageable pageable) {
        try {
            Page<CategoryResponseDto> page = categoryService.findAll(pageable);
            model.addAttribute("page", page);
            model.addAttribute("url", "/admin/categories");
            model.addAttribute("CONTENT_TITLE", "Quản lý thế loại sách");
            model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
            return "pages/admin/categories/index";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/admin/categories";
        }
    }

    @GetMapping(value = {"/edit/{id}"})
    public String edit(@PathVariable Long id, Model model) {
        CategoryResponseDto category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("CONTENT_TITLE", "Chỉnh sửa thể loại");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/categories/edit";
    }

    @GetMapping(value = {"/create"})
    public String create(Model model) {
        model.addAttribute("CONTENT_TITLE", "Thêm thể loại");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/categories/create";
    }

    @PostMapping(value = {"/store"})
    public String store(@Valid @ModelAttribute("categoriesRequestDto") CategoryRequestDto categoriesRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/categories/create";
            }

            categoryService.save(categoriesRequestDto);
            redirectAttributes.addFlashAttribute("success", "Thêm thể loại sách thành công");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm thể loại sách thất bại");
            return "redirect:/admin/categories/create";
        }
    }


    @PostMapping(value = {"/update/{id}"})
    public String update(@PathVariable Long id, @Valid @ModelAttribute("categoriesRequestDto") CategoryRequestDto categoriesRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/categories/edit";
            }

            categoryService.update(categoriesRequestDto, id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thể loại sách thành công");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thể loại sách thất bại");
            return "redirect:/admin/categories/edit/" + id;
        }
    }

    @DeleteMapping(value = {"/delete/{id}"})
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return Map.of("message", "Xóa thể loại sách thành công", "status", 200, "success", true);
        } catch (Exception e) {
            return Map.of("message", "Xóa thể loại sách thất bại", "status", 500, "success", false);
        }
    }

}