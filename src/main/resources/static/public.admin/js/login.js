
$(document).ready(function() {
   login.validateForm();
});

(function (exports, global) {
    exports.validateForm = function() {
        $('#loginForm').validate({
            ignore: ':hidden:not(.validate)',
            rules: {
                username: {
                    required: true,
                    minlength: 3,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 6
                }
            },
            messages: {
                username: {
                    required: "Vui lòng nhập tên đăng nhập.",
                    minlength: "Tên đăng nhập ít nhất 3 ký tự.",
                    email: "Vui lòng nhập địa chỉ email hợp lệ."
                },
                password: {
                    required: "Vui lòng nhập mật khẩu.",
                    minlength: "Mật khẩu ít nhất 6 ký tự."
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
                form.submit();
            }
        })
    }
})(window.login = window.login || {}, window);