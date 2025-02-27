package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.VoucherRequestDto;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import com.ecommerce.book_store.service.abstraction.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/vouchers")
public class VoucherAdminController {
    private final VoucherService voucherService;
    private final CategoryService categoryService;

    public VoucherAdminController(VoucherService voucherService, CategoryService categoryService) {
        this.voucherService = voucherService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String index(Pageable pageable, Model model, RedirectAttributes redirectAttributes) {
        Page<Voucher> page = voucherService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("url", "/admin/vouchers");
        model.addAttribute("CONTENT_TITLE", "Quản lý voucher");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/vouchers/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("CONTENT_TITLE", "Thêm voucher");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        model.addAttribute("categories", categories);
        return "pages/admin/vouchers/create";
    }

    @PostMapping("/store")
    public String store(@ModelAttribute("voucherRequestDto") VoucherRequestDto voucherRequestDto, RedirectAttributes redirectAttributes) {
        try {
            this.voucherService.save(voucherRequestDto);
            redirectAttributes.addFlashAttribute("success", "Tạo voucher thành công");
            return "redirect:/admin/vouchers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vouchers/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Voucher voucher = voucherService.findById(id);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("voucher", voucher);
        model.addAttribute("categories", categories);
        model.addAttribute("CONTENT_TITLE", "Chỉnh sửa voucher");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/vouchers/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("voucherRequestDto") VoucherRequestDto voucherRequestDto, RedirectAttributes redirectAttributes) {
        try {
            this.voucherService.update(voucherRequestDto, id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật voucher thành công");
            return "redirect:/admin/vouchers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vouchers/edit/" + id;
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<Object, Object> delete(@PathVariable Long id) {
        try {
            voucherService.deleteById(id);
            return Map.of("message", "Xóa voucher thành công", "status", 200, "success", true);
        } catch (Exception e) {
            return Map.of("message", "Xóa voucher thất bại", "status", 500, "success", false);
        }
    }
}
