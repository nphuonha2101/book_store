package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.RibbonRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.Ribbon;
import com.ecommerce.book_store.service.abstraction.RibbonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/admin/ribbons")
public class RibbonAdminController {
    private final RibbonService ribbonService;

    public RibbonAdminController(RibbonService ribbonService) {
        this.ribbonService = ribbonService;
    }

    @GetMapping(value = {"", "/index", "/"})
    public String index(Model model, Pageable pageable) {
        Page<RibbonResponseDto> page = ribbonService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("url", "/admin/ribbons");
        model.addAttribute("CONTENT_TITLE", "Quản lý Ribbon sách");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/ribbon/index";
    }

    @GetMapping("/create")
    public String createRibbon(Model model) {
        model.addAttribute("CONTENT_TITLE", "Tạo Ribbon mới");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/ribbon/create";
    }

    @GetMapping("/edit/{id}")
    public String editRibbon(@PathVariable Long id, Model model) {
        RibbonResponseDto ribbon = ribbonService.findById(id);
        model.addAttribute("ribbon", ribbon);
        model.addAttribute("CONTENT_TITLE", "Chỉnh sửa Ribbon");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/ribbon/edit";
    }

    @PostMapping("/store")
    public String storeRibbon(
            @ModelAttribute RibbonRequestDto ribbonRequestDto,
            RedirectAttributes redirectAttributes) {

        try {
            // Save ribbon and ribbon items in a transaction
            ribbonService.saveRibbonWithRibbonItems(ribbonRequestDto);

            redirectAttributes.addFlashAttribute("success", "Tạo Ribbon mới thành công!");
            return "redirect:/admin/ribbons/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo Ribbon: " + e.getMessage());
            return "redirect:/admin/ribbons/create";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRibbon(
            @PathVariable Long id,
            @ModelAttribute RibbonRequestDto ribbonRequestDto,
            RedirectAttributes redirectAttributes) {

        try {
            // Update ribbon and ribbon items in a transaction
            ribbonService.updateRibbonWithRibbonItems(ribbonRequestDto, id);

            redirectAttributes.addFlashAttribute("success", "Cập nhật Ribbon thành công!");
            return "redirect:/admin/ribbons/";
        } catch (Exception e) {
            log.error("Error updating ribbon: ", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật Ribbon: " + e.getMessage());
            return "redirect:/admin/ribbons/edit/" + id;
        }
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Map<Object, Object> deleteRibbon(@PathVariable Long id) {
        try {
            ribbonService.deleteById(id);
            return Map.of("status", 200, "message", "Xóa Ribbon thành công!", "success", true);
        } catch (Exception e) {
            log.error("Error deleting ribbon: ", e);
            return Map.of("status", 500, "message", "Lỗi khi xóa Ribbon: " + e.getMessage(), "success", false);
        }
    }

}
