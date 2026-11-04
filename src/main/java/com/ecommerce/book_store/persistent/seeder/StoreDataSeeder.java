package com.ecommerce.book_store.persistent.seeder;

import com.ecommerce.book_store.persistent.entity.Address;
import com.ecommerce.book_store.persistent.entity.Admin;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import com.ecommerce.book_store.persistent.entity.CartItem;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.entity.Review;
import com.ecommerce.book_store.persistent.entity.Ribbon;
import com.ecommerce.book_store.persistent.entity.RibbonItem;
import com.ecommerce.book_store.persistent.entity.Role;
import com.ecommerce.book_store.persistent.entity.Slider;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.persistent.entity.WishList;
import com.ecommerce.book_store.persistent.repository.abstraction.AddressRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.AdminRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.BookImageRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.BookRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.CartItemRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.CategoryRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.ReviewRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.RibbonItemRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.RibbonRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.RoleRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.SliderRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.VoucherRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Idempotent demo data loader. Enabled with APP_SEED_ENABLED=true and intended
 * for local/demo environments only.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class StoreDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final SliderRepository sliderRepository;
    private final RibbonRepository ribbonRepository;
    private final RibbonItemRepository ribbonItemRepository;
    private final VoucherRepository voucherRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.email}")
    private String adminEmail;

    @Value("${app.seed.admin.password}")
    private String adminPassword;

    @Value("${app.seed.user.email}")
    private String demoUserEmail;

    @Value("${app.seed.user.password}")
    private String demoUserPassword;

    private record BookSeed(
            String title,
            String author,
            String isbn,
            int price,
            int quantity,
            String category,
            String description
    ) {
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = ensureRole("ADMIN", "Quản trị viên");
        Role userRole = ensureRole("USER", "Khách hàng");

        ensureAdmin(adminRole);
        User demoUser = ensureUser("Nguyễn Văn Demo", demoUserEmail, demoUserPassword,
                "0901234567", "12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh", userRole);
        User reviewer = ensureUser("Trần Thị Review", "reviewer@npbookstore.local", "Review@123456",
                "0912345678", "45 Lê Lợi, Quận 3, TP. Hồ Chí Minh", userRole);

        if (bookRepository.count() > 0) {
            log.info("Book store catalogue already contains data; demo seed skipped.");
            return;
        }

        Map<String, Category> categories = seedCategories();
        List<Book> books = seedBooks(categories);
        seedSliders();
        seedRibbons(books);
        seedVouchers(categories);
        seedDemoAccountData(demoUser, reviewer, books);

        log.info("Demo seed completed: {} categories, {} books, {} vouchers.",
                categories.size(), books.size(), voucherRepository.count());
        log.info("Demo customer: {} / {} | Admin: {} / {}",
                demoUserEmail, demoUserPassword, adminEmail, adminPassword);
    }

    private Role ensureRole(String name, String description) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role(name, description);
            return roleRepository.save(role);
        });
    }

    private void ensureAdmin(Role adminRole) {
        if (adminRepository.findByEmail(adminEmail).isPresent()) return;

        Admin admin = new Admin("Store Admin", adminEmail,
                passwordEncoder.encode(adminPassword), adminRole);
        adminRepository.save(admin);
        log.info("Seeded admin account {}", adminEmail);
    }

    private User ensureUser(String name, String email, String password, String phone,
                            String address, Role role) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User(name, email, passwordEncoder.encode(password), role,
                    "https://picsum.photos/seed/npbookstore-avatar/200/200", phone, address);
            User saved = userRepository.save(user);
            log.info("Seeded demo customer {}", email);
            return saved;
        });
    }

    private Map<String, Category> seedCategories() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("Văn học", "Tiểu thuyết, truyện ngắn, thơ và tác phẩm kinh điển.");
        data.put("Kinh tế - Kinh doanh", "Đầu tư, khởi nghiệp, quản trị và tài chính cá nhân.");
        data.put("Kỹ năng sống", "Phát triển bản thân, thói quen và giao tiếp.");
        data.put("Thiếu nhi", "Sách tranh, truyện thiếu nhi và văn học tuổi thơ.");
        data.put("Công nghệ", "Lập trình, khoa học máy tính và công nghệ hiện đại.");
        data.put("Lịch sử - Khoa học", "Lịch sử nhân loại và khám phá khoa học.");

        Map<String, Category> result = new LinkedHashMap<>();
        data.forEach((name, description) -> {
            Category category = categoryRepository.findAll().stream()
                    .filter(item -> name.equals(item.getName()))
                    .findFirst()
                    .orElseGet(() -> categoryRepository.save(new Category(name, description)));
            result.put(name, category);
        });
        return result;
    }

    private List<Book> seedBooks(Map<String, Category> categories) {
        List<BookSeed> seeds = List.of(
                new BookSeed("Nhà giả kim", "Paulo Coelho", "9786041175794", 79_000, 50, "Văn học",
                        "Hành trình đi tìm kho báu và ý nghĩa cuộc sống của chàng chăn cừu Santiago."),
                new BookSeed("Sapiens: Lược sử loài người", "Yuval Noah Harari", "9786045852486", 169_000, 30,
                        "Lịch sử - Khoa học", "Khám phá cách loài người trở thành chủ nhân của hành tinh."),
                new BookSeed("Đắc nhân tâm", "Dale Carnegie", "9786041181160", 86_000, 80, "Kỹ năng sống",
                        "Những nguyên tắc giao tiếp và xây dựng quan hệ bền vững."),
                new BookSeed("Tư duy nhanh và chậm", "Daniel Kahneman", "9786045853711", 149_000, 25,
                        "Kinh tế - Kinh doanh", "Hiểu về hai hệ thống tư duy chi phối quyết định của con người."),
                new BookSeed("Tuổi trẻ đáng giá bao nhiêu", "Rosie Nguyễn", "9786041189869", 75_000, 60,
                        "Kỹ năng sống", "Những chia sẻ truyền cảm hứng dành cho người trẻ."),
                new BookSeed("Dế Mèn phiêu lưu ký", "Tô Hoài", "9786042091234", 55_000, 100, "Thiếu nhi",
                        "Chuyến phiêu lưu đầy thú vị của chú Dế Mèn."),
                new BookSeed("Clean Code", "Robert C. Martin", "9780132350884", 299_000, 20, "Công nghệ",
                        "Nguyên tắc viết mã nguồn sạch, dễ đọc và dễ bảo trì."),
                new BookSeed("Cha giàu cha nghèo", "Robert Kiyosaki", "9781612680194", 98_000, 45,
                        "Kinh tế - Kinh doanh", "Bài học tư duy tài chính từ hai người cha."),
                new BookSeed("Hoàng tử bé", "Antoine de Saint-Exupéry", "9786042095678", 65_000, 90, "Thiếu nhi",
                        "Câu chuyện cổ tích dành cho người lớn về tình yêu và trách nhiệm."),
                new BookSeed("Mắt biếc", "Nguyễn Nhật Ánh", "9786041185678", 90_000, 70, "Văn học",
                        "Mối tình đơn phương trong trẻo của Ngạn dành cho Hà Lan."),
                new BookSeed("Lược sử thời gian", "Stephen Hawking", "9780553380163", 135_000, 35,
                        "Lịch sử - Khoa học", "Từ vụ nổ lớn đến lỗ đen qua lời kể của Stephen Hawking."),
                new BookSeed("Atomic Habits", "James Clear", "9780735211292", 129_000, 55, "Kỹ năng sống",
                        "Xây dựng thói quen nhỏ để tạo thay đổi lớn."),
                new BookSeed("Tôi thấy hoa vàng trên cỏ xanh", "Nguyễn Nhật Ánh", "9786041185679", 95_000, 65,
                        "Văn học", "Thiên truyện về tuổi thơ nơi làng quê Việt Nam."),
                new BookSeed("Sherlock Holmes toàn tập", "Arthur Conan Doyle", "9786041185680", 110_000, 40,
                        "Văn học", "Những vụ án kinh điển của thám tử lừng danh Sherlock Holmes."),
                new BookSeed("Nghĩ giàu và làm giàu", "Napoleon Hill", "9786041185681", 105_000, 50,
                        "Kinh tế - Kinh doanh", "13 nguyên tắc làm giàu được đúc kết từ những người thành công.")
        );

        List<Book> result = new ArrayList<>();
        for (int index = 0; index < seeds.size(); index++) {
            BookSeed seed = seeds.get(index);
            int number = index + 1;

            Book book = new Book(
                    seed.title(),
                    seed.author(),
                    seed.description(),
                    seed.isbn(),
                    "https://picsum.photos/seed/npbookstore-cover-" + number + "/400/600",
                    seed.price(),
                    seed.quantity(),
                    true,
                    LocalDateTime.now().minusDays(20L + number * 9L),
                    categories.get(seed.category())
            );
            Book saved = bookRepository.save(book);

            saveBookImage(saved, "https://picsum.photos/seed/npbookstore-book-" + number + "-1/800/1200");
            saveBookImage(saved, "https://picsum.photos/seed/npbookstore-book-" + number + "-2/800/1200");
            result.add(saved);
        }
        return result;
    }

    private void saveBookImage(Book book, String url) {
        BookImage image = new BookImage(url, book);
        image.setBookId(book.getId());
        bookImageRepository.save(image);
    }

    private void seedSliders() {
        if (sliderRepository.count() > 0) return;

        sliderRepository.saveAll(List.of(
                new Slider("Khám phá thế giới sách",
                        "Hàng nghìn đầu sách hay đang chờ bạn khám phá.",
                        "https://picsum.photos/seed/npbookstore-slider-1/1600/600",
                        "/products"),
                new Slider("Sách mới mỗi tuần",
                        "Cập nhật những tựa sách mới nhất và ưu đãi hấp dẫn.",
                        "https://picsum.photos/seed/npbookstore-slider-2/1600/600",
                        "/products"),
                new Slider("Ưu đãi dành cho bạn",
                        "Giảm giá đến 20% cho nhiều đầu sách được yêu thích.",
                        "https://picsum.photos/seed/npbookstore-slider-3/1600/600",
                        "/search?title=sách")
        ));
    }

    private void seedRibbons(List<Book> books) {
        if (ribbonRepository.count() > 0) return;

        List<Ribbon> ribbons = List.of(
                newRibbon("Sách mới", "Những đầu sách vừa cập bến nhà sách."),
                newRibbon("Bán chạy", "Những tựa sách được độc giả yêu thích nhất."),
                newRibbon("Được yêu thích", "Gợi ý dành cho bạn từ NP Book Store.")
        );
        ribbonRepository.saveAll(ribbons);

        for (int ribbonIndex = 0; ribbonIndex < ribbons.size(); ribbonIndex++) {
            Ribbon ribbon = ribbons.get(ribbonIndex);
            for (int offset = 0; offset < 6; offset++) {
                Book book = books.get((ribbonIndex * 3 + offset) % books.size());
                if (ribbonItemRepository.existsByRibbonIdAndBookId(ribbon.getId(), book.getId())) continue;

                RibbonItem item = new RibbonItem();
                item.setRibbon(ribbon);
                item.setRibbonId(ribbon.getId());
                item.setBook(book);
                item.setBookId(book.getId());
                ribbonItemRepository.save(item);
            }
        }
    }

    private Ribbon newRibbon(String name, String description) {
        Ribbon ribbon = new Ribbon();
        ribbon.setName(name);
        ribbon.setDescription(description);
        ribbon.setStatus(true);
        return ribbon;
    }

    private void seedVouchers(Map<String, Category> categories) {
        if (voucherRepository.count() > 0) return;

        Voucher welcome = new Voucher(
                "https://picsum.photos/seed/npbookstore-voucher-1/600/300",
                "WELCOME10", 10, 100_000, LocalDateTime.now().plusMonths(6));
        welcome.setCategories(List.of(categories.get("Văn học"), categories.get("Kỹ năng sống")));

        Voucher books = new Voucher(
                "https://picsum.photos/seed/npbookstore-voucher-2/600/300",
                "BOOK15", 15, 200_000, LocalDateTime.now().plusMonths(3));
        books.setCategories(List.of(categories.get("Kinh tế - Kinh doanh"), categories.get("Công nghệ")));

        Voucher tech = new Voucher(
                "https://picsum.photos/seed/npbookstore-voucher-3/600/300",
                "TECH20", 20, 300_000, LocalDateTime.now().plusMonths(2));
        tech.setCategories(List.of(categories.get("Công nghệ"), categories.get("Lịch sử - Khoa học")));

        voucherRepository.saveAll(List.of(welcome, books, tech));
    }

    private void seedDemoAccountData(User demoUser, User reviewer, List<Book> books) {
        if (addressRepository.findByUserId(demoUser.getId()).isEmpty()) {
            addressRepository.saveAll(List.of(
                    new Address(demoUser.getId(), demoUser.getName(), "0901234567",
                            "TP. Hồ Chí Minh", "Quận 1", "Phường Bến Nghé",
                            "12 Nguyễn Huệ", true),
                    new Address(demoUser.getId(), demoUser.getName(), "0901234567",
                            "TP. Hồ Chí Minh", "Quận 3", "Phường Võ Thị Sáu",
                            "45 Lê Lợi", false)
            ));
        }

        if (cartItemRepository.findByUserId(demoUser.getId()).isEmpty()) {
            cartItemRepository.saveAll(List.of(
                    new CartItem(demoUser.getId(), books.get(0), 1, books.get(0).getPrice()),
                    new CartItem(demoUser.getId(), books.get(3), 2, books.get(3).getPrice())
            ));
        }

        if (wishListRepository.findByUserId(demoUser.getId()).isEmpty()) {
            wishListRepository.saveAll(List.of(
                    new WishList(demoUser.getId(), books.get(1)),
                    new WishList(demoUser.getId(), books.get(6)),
                    new WishList(demoUser.getId(), books.get(11))
            ));
        }

        for (int index = 0; index < 6; index++) {
            Book book = books.get(index);
            addReview(demoUser, book, 5, "Sách rất hay, đóng gói cẩn thận và giao hàng nhanh.");
            addReview(reviewer, book, 4, "Nội dung hấp dẫn, giấy đẹp, đáng để mua.");
        }
    }

    private void addReview(User user, Book book, int rating, String comment) {
        if (reviewRepository.isUserReviewedBook(user.getId(), book.getId())) return;
        reviewRepository.save(new Review(user, book, rating, comment));
    }
}
