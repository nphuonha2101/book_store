let mediaRecorder;
let audioChunks = [];

$(document).ready(function () {
    const startBtn = document.getElementById("startRecording");
    const stopBtn = document.getElementById("stopRecording");
    const voiceResult = document.getElementById("voiceResult");
    const audioPlayback = document.getElementById("audioPlayback");
    const searchBox = document.getElementById('headerSearchBox');

    header.showDefaultSearchPopup(searchBox);
    header.getSuggestBooksInSearchPopup();

    startBtn.addEventListener("click", function () {
        voiceResult.textContent = "Đang ghi âm...";
        startBtn.classList.add("d-none");
        stopBtn.classList.remove("d-none");
        audioPlayback.classList.add("d-none");

        header.startRecording();
    });

    stopBtn.addEventListener("click", function () {
        startBtn.classList.remove("d-none");
        stopBtn.classList.add("d-none");
        voiceResult.textContent = "Đã ghi âm xong!";

        header.stopRecording();
    });

    searchBox.addEventListener('input', function () {
        const term = searchBox.value;
        const dynamicSuggestions = $('#dynamicSuggestionContainer');
        const staticSuggestions = $('#staticSuggestionContainer');
        if (term.trim() === '') {
            dynamicSuggestions.empty();
            dynamicSuggestions.hide();
            header.refreshSearchHistory();
            staticSuggestions.show();
        } else {
            header.showSearchSuggestions(term);
        }
    });

    searchBox.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            const term = searchBox.value;
            if (term.trim() !== '') {
                header.handleStoreSearchKeyword(term);
                window.location.href = `/books/search?term=${term}`;
            }
        }
    });

    $(document).on('click', '.search-results', async function () {
        const title = $(this).find('h6').text();
        await header.handleStoreSearchKeyword(title);
        const bookId = $(this).data('id');
        window.location.href = `/books/${bookId}`;
    });

    $(document).on('click', '#searchSuggestionProducts .suggestion-product', function () {
        const bookId = $(this).data('id');
        window.location.href = `/books/${bookId}`;
    });
});

(function (exports, global) {
    exports.startRecording = function () {
        navigator.mediaDevices.getUserMedia({
            audio: {
                channelCount: 1,
                sampleRate: 16000,
                echoCancellation: true,
                noiseSuppression: true
            }
        })
            .then(stream => {
                audioStream = stream;
                const options = {
                    mimeType: 'audio/webm;codecs=opus',
                    audioBitsPerSecond: 128000
                };

                try {
                    mediaRecorder = new MediaRecorder(stream, options);
                } catch (e) {
                    console.warn("Codec opus không được hỗ trợ, sử dụng codec mặc định");
                    mediaRecorder = new MediaRecorder(stream);
                }

                audioChunks = []; // Reset dữ liệu cũ

                mediaRecorder.ondataavailable = event => {
                    console.log("Dữ liệu chunk nhận được:", event.data.size);
                    if (event.data.size > 0) {
                        audioChunks.push(event.data);
                    }
                };

                mediaRecorder.onstart = () => {
                    console.log("Ghi âm bắt đầu...");
                };

                mediaRecorder.onerror = err => {
                    console.error("Lỗi MediaRecorder:", err);
                };

                // Lấy dữ liệu mỗi 1 giây
                mediaRecorder.start(500);
            })
            .catch(err => {
                console.error("Không thể truy cập microphone:", err);
                alert("Vui lòng cấp quyền sử dụng micro!");
            });
    };

    exports.stopRecording = function () {
        if (mediaRecorder && mediaRecorder.state !== "inactive") {
            console.log("Dừng ghi âm...");
            mediaRecorder.stop();

            mediaRecorder.onstop = async () => {
                console.log("Số lượng chunks thu được:", audioChunks.length);

                // Dừng luồng âm thanh
                if (audioStream) {
                    audioStream.getTracks().forEach(track => track.stop());
                }

                if (audioChunks.length === 0) {
                    console.warn("Không có dữ liệu âm thanh hợp lệ!");
                    $('#voiceResult').text("Không nhận diện được giọng nói.");
                    return;
                }

                // Sử dụng định dạng thực tế của dữ liệu
                const mimeType = mediaRecorder.mimeType || 'audio/webm';
                const audioBlob = new Blob(audioChunks, {type: mimeType});
                console.log("Kích thước file âm thanh:", audioBlob.size, "Định dạng:", mimeType);

                if (audioBlob.size < 1000) {
                    console.warn("File âm thanh quá nhỏ, có thể không hợp lệ.");
                    $('#voiceResult').text("Không nhận diện được giọng nói.");
                    return;
                }

                const formData = new FormData();
                formData.append('audio', audioBlob, 'recording.webm');

                // Hiển thị âm thanh vừa ghi
                const audioPlayback = document.getElementById('audioPlayback');
                audioPlayback.src = URL.createObjectURL(audioBlob);
                audioPlayback.controls = true;
                audioPlayback.classList.remove('d-none');

                // Thêm thông tin về định dạng và thời gian ghi âm
                console.log("Gửi audio định dạng:", mimeType);

                $('#voiceResult').text("Đang xử lý...");

                // Gửi lên API Vosk
                $.ajax({
                    url: '/api/v1/vosk/transcribe',
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    timeout: 30000, // 30 giây timeout
                    success: function (res) {
                        console.log("Kết quả nhận diện:", res);
                        if (res.text && res.text.trim() !== "") {
                            $('#headerSearchBox').val(res.text);
                            $('#voiceResult').text(res.text);
                        } else {
                            $('#voiceResult').text("Không nhận diện được giọng nói.");
                        }
                    },
                    error: function (err) {
                        console.error("Lỗi API:", err);
                        $('#voiceResult').text("Lỗi xử lý âm thanh");
                        alert('Có lỗi xảy ra. Vui lòng thử lại sau!');
                    }
                });
            };
        } else {
            console.warn("MediaRecorder chưa được khởi tạo hoặc đã dừng trước đó.");
        }
    };

    exports.showDefaultSearchPopup = function (searchBox) {
        const suggestionsBox = document.getElementById('searchSuggestions');
        const closeButton = document.querySelector('.close-suggestions');
        const suggestionHeaderTitle = $('#suggestionHeaderTitle');
        suggestionHeaderTitle.text('Tìm kiếm gần đây');

        header.refreshSearchHistory();

        // Hiển thị popup khi nhấp vào ô tìm kiếm
        searchBox.addEventListener('click', function () {
            suggestionsBox.style.display = 'block';
        });

        // Đóng popup khi nhấp vào nút đóng
        closeButton.addEventListener('click', function () {
            suggestionsBox.style.display = 'none';
        });

        // Đóng popup khi nhấp ra ngoài
        document.addEventListener('click', function (event) {
            if (!searchBox.contains(event.target) &&
                !suggestionsBox.contains(event.target)) {
                suggestionsBox.style.display = 'none';
            }
        });

        // Thêm sự kiện cho các tag gợi ý
        const suggestionTags = document.querySelectorAll('.suggestion-tag');
        suggestionTags.forEach(tag => {
            tag.addEventListener('click', function () {
                searchBox.value = this.textContent;
                suggestionsBox.style.display = 'none';
            });
        });

        // Thêm sự kiện cho các mục tìm kiếm gần đây
        const suggestionItems = document.querySelectorAll('.suggestion-list li');
        suggestionItems.forEach(item => {
            item.addEventListener('click', function () {
                searchBox.value = this.textContent.replace(/^\s*/, '');
                suggestionsBox.style.display = 'none';
                searchBox.focus();
            });
        });

        // Thêm sự kiện cho các sản phẩm đề xuất
        const suggestionProducts = document.querySelectorAll('.suggestion-product');
        suggestionProducts.forEach(product => {
            product.addEventListener('click', function () {
                searchBox.value = this.querySelector('h6').textContent;
                suggestionsBox.style.display = 'none';
                searchBox.focus();
            });
        });
    }

    exports.refreshSearchHistory = function () {
        const keywordHistory = localStorage.getItem('searchKeywords');
        console.log('Từ khóa tìm kiếm gần đây:', keywordHistory);
        if (keywordHistory) {
            const keywords = JSON.parse(keywordHistory);
            const suggestionKeywordHistory = $('#searchHistoryList');
            suggestionKeywordHistory.empty();
            keywords.forEach(keyword => {
                suggestionKeywordHistory.append(`<li><i class="bi bi-clock-history"></i>${keyword}</li>`);
            });
        } else {
            $('#searchHistoryList').append('<p class="text-center">Không có từ khóa tìm kiếm gần đây</p>');
        }
    }

    exports.showSearchSuggestions = function (term) {
        const dynamicSuggestions = $('#dynamicSuggestionContainer');
        const staticSuggestions = $('#staticSuggestionContainer');
        const suggestionHeaderTitle =$('#suggestionHeaderTitle');
        staticSuggestions.hide();
        $.ajax({
                url: '/api/v1/books/search',
                type: 'GET',
                data: {
                    term: term
                },
            success: function (res) {
                dynamicSuggestions.empty();
                if (res.content.length > 0) {
                    suggestionHeaderTitle.text(`Kết quả tìm kiếm cho "${term}"`);
                    const maxResults = 5;
                    const resultsToShow = res.content.slice(0, maxResults);

                    resultsToShow.forEach(book => {
                        console.log(book.title);

                        const coverImage = book.coverImage || 'https://img.freepik.com/free-vector/hand-drawn-flat-design-stack-books-illustration_23-2149341898.jpg';
                        dynamicSuggestions.append(
                            `<div class="suggestion-product search-results mt-1" data-id="${book.id}">
                                <div class="product-img">
                                    <img src="${coverImage}" alt="Product" onerror="this.onerror=null;this.src='/path/to/default/image.jpg';">
                                </div>
                                <div class="product-info">
                                    <h6>${book.title}</h6>
                                    <span class="price">${book.price} VND</span>
                                </div>
                            </div>`
                        );
                    });

                    if (res.content.length > maxResults) {
                        dynamicSuggestions.append(
                            `<div class="text-center text-sm mt-2">
                    <button class="btn btn-primary" id="viewMoreBtn">Xem thêm</button>
                </div>`
                        );

                        $('#viewMoreBtn').on('click', function () {
                            window.location.href = `/books/search?term=${term}`;
                        });
                    }

                    dynamicSuggestions.show();
                } else {
                    suggestionHeaderTitle.text(`Không tìm thấy sản phẩm nào cho "${term}"`);
                    dynamicSuggestions.append('<p class="text-center">Không tìm thấy sản phẩm nào</p>');
                    dynamicSuggestions.show();
                }
            }
            }
        )
    }


    exports.handleStoreSearchKeyword = async function (keyword) {

        const keywords = localStorage.getItem('searchKeywords');
        if (keywords) {
            const keywordArr = JSON.parse(keywords);
            if (!keywordArr.includes(keyword)) {
                keywordArr.unshift(keyword);
                localStorage.setItem('searchKeywords', JSON.stringify(keywordArr));
            }
        } else {
            localStorage.setItem('searchKeywords', JSON.stringify([keyword]));
        }
    };

    exports.getSuggestBooksInSearchPopup = function () {
        const keywordHistory = localStorage.getItem('searchKeywords');
        const searchSuggestionProducts = $('#searchSuggestionProducts');
        let keywords;
        if (keywordHistory) {
            keywords = JSON.parse(keywordHistory);
            $.ajax({
                url: '/api/v1/books/suggest?page=0&size=4',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(keywords),
                success: function (res) {
                    searchSuggestionProducts.empty();
                    res.content.forEach(book => {
                        searchSuggestionProducts.append(
                            `<div class="suggestion-product" data-id="${book.id}">
                                <div class="product-img">
                                    <img src="${book.coverImage}" alt="Product">
                                </div>
                                <div class="product-info">
                                    <h6>${book.title}</h6>
                                    <span class="price">${book.price} VND</span>
                                </div>
                            </div>`
                        );
                    });
                }
            })
        }
    }
})((window.header = window.header || {}), window);