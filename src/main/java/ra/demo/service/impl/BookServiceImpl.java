package ra.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ra.demo.exception.BookNotFound;
import ra.demo.model.dto.request.BookDTO;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookResporitory bookResporitory;

    @Override
    public List<Book> getBooks() {
        log.debug("getBooks called");
        List<Book> books = bookResporitory.findAll();
        log.info("getBooks success - found {} books", books.size());
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        log.debug("getBookById called with id={}", id);
        Book book = bookResporitory.findById(id).orElseThrow(() -> {
            log.error("getBookById failed - book not found with id={}", id);
            return new BookNotFound("Không tồn tại sách có mã " + id);
        });
        log.info("getBookById success - id={}", id);
        return book;
    }

    @Override
    public Book insertBook(BookDTO bookDTO) {
        log.debug("insertBook called with payload={}", bookDTO);
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();
        Book savedBook = bookResporitory.save(book);
        log.info("insertBook success - id={}", savedBook.getId());
        return savedBook;
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) {
        bookResporitory.findById(id).orElseThrow(()-> new BookNotFound("Không tồn tại sách có mã "+id));
        Book book = Book.builder()
                .id(id)
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();
        return bookResporitory.save(book);
    }

    @Override
    public boolean deleteBook(Long id) {
        bookResporitory.findById(id).orElseThrow(()-> new BookNotFound("Không tồn tại sách có mã "+id));
        bookResporitory.deleteById(id);
        return true;
    }

    @Override
    public Book updatePartialBook(Long id, BookDTO bookDTO) {
        Book book = bookResporitory.findById(id).orElseThrow(() -> new BookNotFound("Không tồn tại sách có mã " + id));
        if(!bookDTO.getTitle().isBlank()){
            book.setTitle(bookDTO.getTitle());
        }
        if(!bookDTO.getAuthor().isBlank()){
            book.setAuthor(book.getAuthor());
        }
        if(!bookDTO.getCategory().isBlank()){
            book.setCategory(bookDTO.getCategory());
        }
        if(bookDTO.getQuantity()!=null && bookDTO.getQuantity()>=0){
            book.setQuantity(bookDTO.getQuantity());
        }
        return bookResporitory.save(book);
    }
}
