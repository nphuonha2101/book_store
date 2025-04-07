$(document).ready(function () {
    ribbon.validateForm();

    ribbon.initAutocomplete();

    ribbon.initExistingBooks();

    ribbon.initAddBookButton();

    ribbon.initRemoveBookHandler();

    ribbon.initFormSubmit();

    $('.delete-ribbon-btn').off().on('click', function () {
        const ribbonId = $(this).data('ribbon-id');
        ribbon.deleteRibbon(ribbonId);
    });
});

(function (exports, global) {
    const selectedBooks = new Set();

    exports.validateForm = function () {
        $("#ribbonForm").validate({
            ignore: ':hidden:not(.validate)',
            rules: {
                ribbonName: {
                    required: true,
                    minlength: 3
                },
                bookIds: {
                    required: true
                }
            },
            messages: {
                ribbonName: {
                    required: "Vui lòng nhập tên Ribbon",
                    minlength: "Tên Ribbon phải có ít nhất 3 ký tự"
                },
                bookIds: {
                    required: "Vui lòng chọn ít nhất một sách cho Ribbon"
                }
            },
            submitHandler: function (form) {
                if (selectedBooks.size === 0) {
                    window.swalError("Vui lòng chọn ít nhất một sách cho Ribbon");
                } else {
                    window.swalConfirm("Bạn chắc chắn muốn lưu sách này?", "Có", "Không", function () {
                        form.submit();
                    });
                }
            },
            errorPlacement: function (error, element) {
                error.addClass("text-danger");

                if (element.closest('.form-group').length) {
                    error.appendTo(element.closest('.form-group'));
                } else {
                    error.insertAfter(element);
                }
            }
        });
    };

    exports.initAutocomplete = function () {
        const bookSearchInput = $("#bookSearch");
        if (!bookSearchInput.length) return;

        bookSearchInput.autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: "/admin/books/api/search",
                    dataType: "json",
                    data: { term: request.term },
                    success: function (data) {
                        const filteredData = data.books.filter(book => !selectedBooks.has(book.id.toString()));
                        response($.map(filteredData, function (book) {
                            return {
                                label: book.title + (book.authorName ? " - " + book.authorName : ""),
                                value: book.title,
                                id: book.id
                            };
                        }));
                    }
                });
            },
            minLength: 2,
            select: function (event, ui) {
                $("#selectedBookId").val(ui.item.id);
                return true;
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            return $("<li>")
                .append("<div><strong>" + item.value + "</strong>" +
                    (item.label.includes(" - ") ? "<br><small>" + item.label.split(" - ")[1] + "</small>" : "") +
                    "</div>")
                .appendTo(ul);
        };
    };

    exports.initExistingBooks = function () {
        if (typeof existingBooks !== "undefined" && Array.isArray(existingBooks)) {
            existingBooks.forEach(item => {
                exports.addBookToSelection(item.book.id, item.book.title + (item.book.authorName ? " - " + item.book.authorName : ""));
            });
        }
    };

    exports.initAddBookButton = function () {
        $("#addBookBtn").on("click", function () {
            const bookId = $("#selectedBookId").val();
            const bookTitle = $("#bookSearch").val();

            if (bookId && bookTitle && !selectedBooks.has(bookId)) {
                exports.addBookToSelection(bookId, bookTitle);
                $("#bookSearch").val("");
                $("#selectedBookId").val("");
            } else if (!bookId && bookTitle) {
                window.swalError("Vui lòng chọn sách từ danh sách gợi ý");
            }
        });
    };

    exports.addBookToSelection = function (bookId, bookTitle) {
        selectedBooks.add(bookId.toString());

        const listItem = $("<li>").addClass("list-group-item d-flex justify-content-between align-items-center")
            .attr("data-book-id", bookId)
            .html(`
                ${bookTitle}
                <button type="button" class="btn btn-sm btn-danger remove-book" data-book-id="${bookId}">
                    <i class="fas fa-times"></i> Xóa
                </button>
            `);
        $("#selectedBooksList").append(listItem);

        const hiddenInput = $("<input>")
            .attr("type", "hidden")
            .attr("name", "bookIds")
            .attr("value", bookId)
            .attr("id", `book-input-${bookId}`);
        $("#selectedBooksContainer").append(hiddenInput);

        $("#noSelectedBooks").hide();
        $("#selectedBooksList").show();
    };

    exports.initRemoveBookHandler = function () {
        $("#selectedBooksList").on("click", ".remove-book", function () {
            const bookId = $(this).data("book-id");
            $(this).closest("li").remove();
            selectedBooks.delete(bookId.toString());
            $(`#book-input-${bookId}`).remove();

            if (selectedBooks.size === 0) {
                $("#noSelectedBooks").show();
                $("#selectedBooksList").hide();
            }
        });
    };

    exports.initFormSubmit = function () {
        $("#ribbonForm").submit(function (e) {
            if (selectedBooks.size === 0) {
                e.preventDefault();
                window.swalError("Vui lòng chọn ít nhất một sách cho Ribbon");
            }
        });
    };

    exports.deleteRibbon = function (ribbonId) {
        window.swalConfirm("Bạn chắc chắn muốn xóa ribbon này?", "Có", "Không", function () {
            $.ajax({
                url: "/admin/ribbons/delete/" + ribbonId,
                type: "DELETE",
                success: function (res) {
                    if (res.status === 200) {
                        window.swalSuccess("Xóa ribbon thành công!").then(function () {
                            window.location.reload();
                        });
                    } else {
                        window.swalError("Xóa ribbon thất bại!");
                    }
                },
                error: function () {
                    window.swalError("Lỗi hệ thống!");
                }
            });
        });
    };

})((window.ribbon = window.ribbon || {}), window);
