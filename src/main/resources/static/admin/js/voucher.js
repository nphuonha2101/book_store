$(function() {
    voucher.validateForm();
    $('.delete-voucher-btn').on('click', function() {
        const voucherId = $(this).data('id');
        voucher.deleteVoucher(voucherId);
    });
});

(function(exports, global) {
    exports.validateForm = function() {
        $.validator.addMethod("greaterThan", function(value, element, params) {
            return new Date(value) > new Date($(params).val());
        });
        $('#voucherForm').validate({
            rules: {
                code: {
                    required: true,
                },
                discount: {
                    required: true,
                    min: 1,
                    max: 100
                },
                minSpend: {
                    required: true,
                    min: 1000,
                },
                startDate: {
                    required: true,
                },
                endDate: {
                    required: true,
                    greaterThan: "#startDate",
                },
                thumbnail: {
                    required: function (element) {
                        return $("#thumbnailPreview").attr("src") == "" || window.location.pathname.includes("create");
                    },
                },
                categoryIds: {
                    required: true,
                }
            },
            messages: {
                code: {
                    required: "Vui lòng nhập mã voucher",
                },
                discount: {
                    required: "Vui lòng nhập giảm giá",
                    min: "Giảm giá phải lớn hơn 0",
                    max: "Giảm giá phải nhỏ hơn hoặc bằng 100"
                },
                minSpend: {
                    required: "Vui lòng nhập mức tiêu chuẩn",
                    min: "Mức tiêu chuẩn phải lớn hơn 1000",
                },
                startDate: {
                    required: "Vui lòng chọn ngày bắt đầu",
                },
                endDate: {
                    required: "Vui lòng chọn ngày kết thúc",
                    greaterThan: "Ngày kết thúc phải lớn hơn ngày bắt đầu"
                },
                thumbnail: {
                    required: "Vui lòng chọn ảnh",
                },
                categoryIds: {
                    required: "Vui lòng chọn danh mục",
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
                window.swalConfirm("Bạn chắc chắn muốn lưu/cập nhật voucher này?", "Có", "Không", function() {
                    form.submit();
                });
            }
        })
    }

    exports.deleteVoucher = function(voucherId) {
        window.swalConfirm("Bạn chắc chắn muốn xóa voucher này?", "Có", "Không", function() {
            $.ajax({
                url: "/admin/vouchers/delete/" + voucherId,
                type: "DELETE",
                success: function (res) {
                    if (res.status === 200) {
                        window.swalSuccess("Xóa voucher thành công!").then(function() {
                            window.location.reload();
                        });
                    } else {
                        window.swalError("Xóa voucher thất bại!");
                    }
                },
                error: function (err) {
                    window.swalError("Lỗi hệ thống!");
                }

            });
        });
    }

})((window.voucher = window.voucher || {}), window);