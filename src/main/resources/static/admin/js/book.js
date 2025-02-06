$(document).ready(function() {
    book.validateForm();

});

(function(exports, global) {

    exports.validateForm = function() {
        const validator = $("#bookForm").validate({
            ignore: ':hidden:not(.validate)',
            rules: {
                title: {
                    required: true,
                    minlength: 3
                },
                authorName: {
                    required: true,
                    minlength: 3
                },
                description: {
                    required: true,
                    minlength: 10
                },
                isbn: {
                    required: true,
                    digits: true,
                    minlength: 10,
                    maxlength: 13
                },
                categoryId: {
                    required: true
                },
                price: {
                    required: true,
                    number: true,
                    min: 1000
                },
                quantity: {
                    required: true,
                    digits: true,
                    min: 1
                },
                publishedAt: {
                    required: false,
                    date: true
                },
                coverImage: {
                    required: true,
                    extension: "jpg|jpeg|png|gif"
                },
                "bookImages[]": {
                    required: false,
                    extension: "jpg|jpeg|png|gif"
                }
            },
            messages: {
                title: {
                    required: "Vui lòng nhập tiêu đề.",
                    minlength: "Tiêu đề ít nhất 3 ký tự."
                },
                authorName: {
                    required: "Vui lòng nhập tên tác giả.",
                    minlength: "Tên tác giả ít nhất 3 ký tự."
                },
                description: {
                    required: "Vui lòng nhập mô tả.",
                    minlength: "Mô tả ít nhất 10 ký tự."
                },
                isbn: {
                    required: "Vui lòng nhập ISBN.",
                    digits: "Chỉ nhập số.",
                    minlength: "ISBN phải có ít nhất 10 số.",
                    maxlength: "ISBN tối đa 13 số."
                },
                categoryId: {
                    required: "Vui lòng chọn thể loại."
                },
                price: {
                    required: "Vui lòng nhập giá.",
                    number: "Chỉ nhập số.",
                    min: "Giá phải lớn hơn 1000."
                },
                quantity: {
                    required: "Vui lòng nhập số lượng.",
                    digits: "Chỉ nhập số.",
                    min: "Số lượng phải lớn hơn 0."
                },
                coverImage: {
                    required: "Vui lòng tải lên ảnh bìa.",
                    extension: "Chỉ chấp nhận các định dạng jpg, jpeg, png, gif."
                },
                "bookImages[]": {
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
                window.swalConfirm("Bạn chắc chắn muốn lưu sách này?", "Có", "Không", function() {
                    form.submit();
                });
            }
        });

        const imageUpload = document.getElementById('imageUpload');
        const imagePreview = document.getElementById('imagePreview');

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
                };
                reader.readAsDataURL(file);
            });
            validator.element(input);
        }

        // Book images preview
        imageUpload.addEventListener('change', function() {
            handleImageUpload(this, imagePreview, true);
        });

        // Cover image preview
        const coverImage = document.getElementById('coverImage');
        const coverImagePreview = document.getElementById('coverImagePreview');
        coverImage.addEventListener('change', function() {
            handleImageUpload(this, coverImagePreview, false);
        });

    }

})((window.book = window.book || {}), window);

