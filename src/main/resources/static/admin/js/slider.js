$(document).ready(function() {
    slider.validateForm();

    $('.delete-slider-btn').off().on('click', function() {
        console.log('delete slider');
        const sliderId = $(this).data('slider-id');
        slider.deleteSlider(sliderId);
    });
});

(function(exports, global) {

    exports.validateForm = function() {
        const validator = $("#sliderForm").validate({
            ignore: ':hidden:not(.validate)',
            rules: {
                title: {
                    required: true,
                    minlength: 3
                },
                description: {
                    required: true,
                    minlength: 10
                },
                url: {
                    required: true,
                    url: true
                },
                image: {
                    required: function (element) {
                        return window.location.pathname === '/admin/sliders/create';
                    },
                    extension: "jpg|jpeg|png|gif"
                }
            },
            messages: {
                title: {
                    required: "Vui lòng nhập tiêu đề.",
                    minlength: "Tiêu đề ít nhất 3 ký tự."
                },
                description: {
                    required: "Vui lòng nhập mô tả.",
                    minlength: "Mô tả ít nhất 10 ký tự."
                },
                url: {
                    required: "Vui lòng nhập URL.",
                    url: "URL không hợp lệ."
                },
                image: {
                    required: "Vui lòng tải lên hình ảnh.",
                    extension: "Chỉ chấp nhận các định dạng jpg, jpeg, png, gif."
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
                window.swalConfirm("Bạn chắc chắn muốn lưu slider này?", "Có", "Không", function() {
                    form.submit();
                });
            }
        });

        function handleImageUpload(input, previewContainer, isMultiple = true) {
            const files = Array.from(input.files);
            previewContainer.innerHTML = '';

            files.forEach((file, index) => {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const previewWrapper = document.createElement('div');
                    previewWrapper.className = 'preview-item';

                    const img = document.createElement('img');
                    img.src = e.target.result;
                    img.className = 'preview-image';

                    const deleteBtn = document.createElement('button');
                    deleteBtn.innerHTML = '✖';
                    deleteBtn.className = 'delete-btn';
                    deleteBtn.onclick = function() {
                        previewWrapper.remove();
                        if (isMultiple) {
                            const dt = new DataTransfer();
                            const remainingFiles = Array.from(input.files)
                                .filter((_, i) => i !== index);
                            remainingFiles.forEach(file => dt.items.add(file));
                            input.files = dt.files;
                        } else {
                            input.value = '';
                        }
                        validator.element(input);
                    };

                    previewWrapper.appendChild(img);
                    previewWrapper.appendChild(deleteBtn);
                    previewContainer.appendChild(previewWrapper);

                    // Update the first image's src with the new image's src
                    if (index === 0) {
                        const firstImage = document.getElementById('coverImagePreview');
                        if (firstImage) {
                            firstImage.src = e.target.result;
                        }
                    }
                };
                reader.readAsDataURL(file);
            });
            validator.element(input);
        }

        // Cover image preview
        const coverImage = document.getElementById('coverImage');
        const coverImagePreview = document.getElementById('coverImagePreview');
        if (coverImage) {
            coverImage.addEventListener('change', function() {
                handleImageUpload(this, coverImagePreview, false);
            });
        }
    }

    exports.deleteSlider = function(sliderId) {
        window.swalConfirm("Bạn chắc chắn muốn xóa slider này?", "Có", "Không", function() {
            $.ajax({
                url: "/admin/sliders/delete/" + sliderId,
                type: "DELETE",
                success: function (res) {
                    if (res.status === 200) {
                        window.swalSuccess("Xóa slider thành công!").then(function() {
                            window.location.reload();

                        });
                    } else {
                        window.swalError("Xóa slider thất bại!");
                    }
                },
                error: function (err) {
                    window.swalError("Lỗi hệ thống!");
                }
            });
        });
    }

})((window.slider = window.slider || {}), window);