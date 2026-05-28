package ra.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.advice.BookControllerAdvice;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookControllerAdvice.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void getBooks_returnOkAndJsonList() throws Exception {
        List<Book> books = List.of(
                Book.builder().id(1L).title("Book 1").author("Author 1").category("Cat 1").quantity(2).build(),
                Book.builder().id(2L).title("Book 2").author("Author 2").category("Cat 2").quantity(3).build()
        );
        when(bookService.getBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void getBookById_found_returnOkAndJsonObject() throws Exception {
        Book book = Book.builder().id(1L).title("Book 1").author("Author 1").category("Cat 1").quantity(2).build();
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Book 1"));
    }

    @Test
    void getBookById_notFound_returnNotFound() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFound("Không tồn tại sách có mã 99"));

        mockMvc.perform(get("/api/books/99").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
