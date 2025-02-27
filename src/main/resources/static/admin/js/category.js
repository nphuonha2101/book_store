$(document).ready(function() {
    category.validateForm();

    $('.delete-category-btn').off().on('click', function() {
        console.log('delete category');
        const categoryId = $(this).data('category-id');
        category.deleteCategory(categoryId);
    });
});

(function(exports, global) {

    exports.validateForm = function() {
        const validator = $("#categoryForm").validate({
            ignore: ':hidden:not(.validate)',
            rules: {
                name: {
                    required: true,
                    minlength: 3
                },
                description: {
                    required: true,
                    minlength: 10
                }
            },
            messages: {
                name: {
                    required: "Vui lòng nhập tên thể loại.",
                    minlength: "Tên thể loại ít nhất 3 ký tự."
                },
                description: {
                    required: "Vui lòng nhập mô tả.",
                    minlength: "Mô tả ít nhất 10 ký tự."
                }
            },
            errorPlacement: function (error, element) {
                error.addClass("text-danger");

                if (element.closest('.form-group').length) {
                    error.appendTo(element.closest('.form-group'));
                } else {
                    error.insertAfter(element);
                }
            },
            submitHandler: function (form) {
                window.swalConfirm("Bạn chắc chắn muốn lưu/cập nhật thể loại này?", "Có", "Không", function() {
                    form.submit();
                });
            }
        });
    }

    exports.deleteCategory = function(categoryId) {
        window.swalConfirm("Bạn chắc chắn muốn xóa thể loại này?", "Có", "Không", function() {
            $.ajax({
                url: "/admin/categories/delete/" + categoryId,
                type: "DELETE",
                success: function (res) {
                    if (res.status === 200) {
                        window.swalSuccess("Xóa thể loại thành công!").then(function() {
                            window.location.reload();
                        });
                    } else {
                        window.swalError("Xóa thể loại thất bại!");
                    }
                },
                error: function (err) {
                    window.swalError("Lỗi hệ thống!");
                }
            });
            console.log('delete category' + err);
        });
    }

})((window.category = window.category || {}), window);