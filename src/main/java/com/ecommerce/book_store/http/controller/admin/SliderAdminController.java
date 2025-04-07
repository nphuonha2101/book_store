package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.SliderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.SliderResponseDto;
import com.ecommerce.book_store.service.abstraction.SliderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/sliders")
public class SliderAdminController {
    private final SliderService sliderService;

    public SliderAdminController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping
    public String listSlider(Model model, Pageable pageable) {
        try {
            Page<SliderResponseDto> page = sliderService.findAll(pageable);
            model.addAttribute("page", page);
            model.addAttribute("url", "/admin/sliders");
            model.addAttribute("CONTENT_TITLE", "Quản lý slider");
            model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
            return "pages/admin/sliders/index";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/admin/sliders";
        }
    }

    @GetMapping(value = {"/edit/{id}"})
    public String edit(@PathVariable Long id, Model model) {
        SliderResponseDto slider = sliderService.findById(id);
        model.addAttribute("slider", slider);
        model.addAttribute("CONTENT_TITLE", "Chỉnh sửa slider");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/sliders/edit";
    }

    @PostMapping(value = {"/{id}/update"})
    public String update(@PathVariable Long id, @Valid @ModelAttribute("slidersRequestDto") SliderRequestDto slidersRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/sliders/edit";
            }

            sliderService.update(slidersRequestDto, id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật slider thành công");
            return "redirect:/admin/sliders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật slider thất bại");
            return "redirect:/admin/sliders/edit/" + id;
        }
    }

    // create slider
    @GetMapping(value = {"/create"})
    public String create(Model model) {
        List<SliderResponseDto> sliders = sliderService.findAll();
        model.addAttribute("slider", sliders);
        model.addAttribute("CONTENT_TITLE", "Thêm slider");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/sliders/create";
    }

    @PostMapping(value = {"/store"})
    public String store(@Valid @ModelAttribute("sliderRequestDto") SliderRequestDto sliderRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/sliders/create";
            }

            sliderService.save(sliderRequestDto);
            redirectAttributes.addFlashAttribute("success", "Thêm slider thành công");
            return "redirect:/admin/sliders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm slider thất bại");
            return "redirect:/admin/sliders/create";
        }
    }

    @DeleteMapping(value = {"/delete/{id}"})
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long id) {
        try {
            sliderService.deleteById(id);
            return Map.of("message", "Xóa slider thành công", "status", 200, "success", true);
        } catch (Exception e) {
            return Map.of("message", "Xóa slider thất bại", "status", 500, "success", false);
        }
    }

}
