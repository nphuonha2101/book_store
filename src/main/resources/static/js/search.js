$(function () {
    search.getCategories();

    $('#filterForm').on('submit', function (event) {
        event.preventDefault();
        const pageSize = $('#pageSize').val();
        search.filter(0, pageSize);
    });

    $('#pageSize').on('change', function () {
        const pageSize = $(this).val();
        search.filter(0, pageSize);
    });
});

(function(exports, global) {
    exports.filter = function(page = 0, size = 10) {
        console.log('page:', page);
        console.log('size:', size);
        // Lấy giá trị filter
        const authorName = document.getElementById('author').value;
        const categoryCheckboxes = document.querySelectorAll('input[name="categoryIds"]:checked');
        const categoryIds = Array.from(categoryCheckboxes).map(cb => cb.value);
        const minPrice = document.getElementById('minPrice').value;
        const maxPrice = document.getElementById('maxPrice').value;

        // Xây dựng các tham số truy vấn
        let params = new URLSearchParams();
        if (authorName) params.append('authorName', authorName);
        categoryIds.forEach(id => params.append('categoryIds', id));
        if (minPrice) params.append('minPrice', minPrice);
        if (maxPrice) params.append('maxPrice', maxPrice);
        params.append('page', `${page}`);
        params.append('size', `${size}`);

        // Gọi API
        $.ajax({
            url: `/api/v1/books/filter?${params.toString()}`,
            type: 'GET',
            success: function(data) {
                // Cập nhật container sách với kết quả
                search.updatePagination(data);
                search.renderBooks(data.content);
                // Cập nhật phân trang
            },
            error: function(error) {
                console.error('Error fetching books:', error);
            }
        });
    }

    exports.renderBooks = function(books) {
        const container = document.getElementById('booksContainer');
        container.innerHTML = '';
        let html = '';

        if (books.length === 0) {
            html = '<h3>Không tìm thấy sách phù hợp</h3>';
            container.innerHTML = html;
            return;
        }

        // Mẫu hiển thị theo định dạng yêu cầu
        books.forEach(book => {
             html += `
                    <div class="col-lg-3 col-md-6 col-12">
                        <div class="single-product">
                            <div class="product-image">
                                <img src="${book.coverImage || '/api/placeholder/300/400'}" alt="${book.title}">
                                <div class="button">
                                    <a href="#" class="btn"><i class="lni lni-cart"></i> Thêm vào giỏ hàng</a>
                                </div>
                            </div>
                            <div class="product-info">
                                <span class="category">${book.authorName || 'Tác giả'}</span>
                                <h4 class="title">
                                    <a href="/books/${book.id}">${book.title}</a>
                                </h4>
                                <div class="price">
                                    <span>${search.formatPrice(book.price)} VND</span>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
        });
        container.innerHTML = html;
    }

    exports.updatePagination = function(pageData) {
        document.getElementById('currentPage').textContent = pageData.number + 1;
        document.getElementById('totalPages').textContent = pageData.totalPages;

        // Cập nhật liên kết phân trang ở đây

        const pagination = document.getElementById('paginationList');
        if (!pagination) return;
        pagination.innerHTML = '';

        if (pageData.totalPages <= 1) return;

        let html = '';

        // Nút Previous
        html += `
            <li class="page-item ${pageData.number === 0 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="${pageData.number - 1}" href="#">«</a>
            </li>
        `;

        for (let i = 0; i < pageData.totalPages; i++) {
            if (pageData.totalPages <= 6 || i < 4 || (i >= pageData.number - 2 && i <= pageData.number + 2) || i >= pageData.totalPages - 4) {
                html += `
                    <li class="page-item ${pageData.number === i ? 'active' : ''}">
                        <a class="page-link pagination-link" data-page="${i}" href="#">${i + 1}</a>
                    </li>
                `;
            }

            if (pageData.totalPages > 6 && i === 4 && pageData.number > 5) {
                html += `<li class="page-item"><span class="page-link">...</span></li>`;
            }

            if (pageData.totalPages > 6 && i === pageData.totalPages - 5 && pageData.number < pageData.totalPages - 5) {
                html += `<li class="page-item"><span class="page-link">...</span></li>`;
            }
        }

        // Nút Next
        html += `
            <li class="page-item ${pageData.number === pageData.totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="${pageData.number + 1}" href="#">»</a>
            </li>
        `;

        pagination.innerHTML = html;
        search.attachPaginationEvents();
    }

    exports.attachPaginationEvents = function () {
        const pageSize = $('#pageSize').val();
        const term = $('#filterForm #title').val();
        document.querySelectorAll(".pagination-link").forEach(link => {
            link.addEventListener("click", function (event) {
                event.preventDefault();
                let newPage = parseInt(this.getAttribute("data-page"));
                if (!isNaN(newPage)) {
                    search.filter(newPage, pageSize);
                    window.history.pushState({}, "", `?term=${term}&page=${newPage}`);
                }
            });
        });
    }

    exports.formatPrice = function(price) {
        return new Intl.NumberFormat('vi-VN').format(price);
    }

    exports.getCategories = function() {
        const categoriesSelect = $('select#category');
        $.ajax({
            url: '/api/v1/categories/all',
            type: 'GET',
            success: function(categories) {
                categoriesSelect.empty();
                categoriesSelect.append('<option value="">Tất cả</option>');
                categories.forEach(category => {
                    categoriesSelect.append(`<option value="${category.id}">${category.name}</option>`);
                });
            },
            error: function(error) {
                categoriesSelect.append('<option value="">Không thể tải danh mục</option>');
                console.error('Error fetching categories:', error);
            }
        })

    }
})((window.search = window.search || {}), window);