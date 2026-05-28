package ra.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookResporitory bookResporitory;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_returnList() {
        List<Book> books = List.of(
                Book.builder().id(1L).title("Book 1").author("Author 1").category("Cat 1").quantity(2).build(),
                Book.builder().id(2L).title("Book 2").author("Author 2").category("Cat 2").quantity(3).build()
        );
        when(bookResporitory.findAll()).thenReturn(books);

        List<Book> result = bookService.getBooks();

        assertEquals(2, result.size());
    }

    @Test
    void getBookById_found() {
        Book book = Book.builder().id(1L).title("Book 1").author("Author 1").category("Cat 1").quantity(2).build();
        when(bookResporitory.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(book, result);
    }

    @Test
    void getBookById_notFound() {
        when(bookResporitory.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, () -> bookService.getBookById(99L));
    }
}
