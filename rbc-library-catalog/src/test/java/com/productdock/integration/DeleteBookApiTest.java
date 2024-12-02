package com.productdock.integration;

import com.productdock.adapter.out.sql.BookRepository;
import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static com.productdock.data.provider.out.sql.BookEntityMother.defaultBookEntityBuilder;
import static com.productdock.kafka.KafkaFileUtil.getMessageFrom;
import static com.productdock.kafka.KafkaFileUtil.ifFileExists;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
public class DeleteBookApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testDeleteBook.txt";
    private static final String ROLE_ADMIN = "SCOPE_ROLE_ADMIN";
    private static final String ROLE_USER = "SCOPE_ROLE_USER";

    public static MockWebServer mockRentalBackEnd;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private RestRequestProducer requestProducer;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws IOException {
        mockRentalBackEnd = new MockWebServer();
        mockRentalBackEnd.start(8083);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockRentalBackEnd.shutdown();
    }

    @AfterEach
    final void before() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "book_topic", "book", "topic");
    }

    @AfterAll
    static void after() {
        new File(TEST_FILE).delete();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void deleteBook_whenIdDoesntExist() {
        requestProducer.makeDeleteBookRequest(1L, ROLE_ADMIN).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void returnForbidden_whenUserWithInsufficientRole() throws Exception {
        requestProducer.makeDeleteBookRequest(1L, ROLE_USER)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void deleteBook_whenBookIsValid() throws Exception {
        var bookId = givenAnyBook();
        mockRentalBackEnd.enqueue(new MockResponse().setBody("[]").addHeader("Content-Type", "application/json"));
        requestProducer.makeDeleteBookRequest(bookId, ROLE_ADMIN).andExpect(status().isOk());

        await()
                .atMost(Duration.ofSeconds(4))
                .until(ifFileExists(TEST_FILE));

        var deleteBookMessage = (Long) getMessageFrom(TEST_FILE);
        assertThat(deleteBookMessage).isEqualTo(bookId);
    }

    @Test
    @WithMockUser
    void deleteBook_whenBookIsTaken() throws Exception {
        var bookId = givenAnyBook();
        mockRentalBackEnd.enqueue(new MockResponse().setBody("[{\"user\":{\"fullName\":\"Test Test\"},\"status\":\"RENTED\"}]").addHeader("Content-Type", "application/json"));
        requestProducer.makeDeleteBookRequest(bookId, ROLE_ADMIN).andExpect(status().isBadRequest());
    }

    private Long givenAnyBook() {
        var marketingTopic = givenTopicWithName("MARKETING");
        var designTopic = givenTopicWithName("DESIGN");
        var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
        return bookRepository.save(book).getId();
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }

}
