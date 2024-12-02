package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.mapper.GetBookDtoMapper;
import com.productdock.application.port.in.GetBookQuery;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.productdock.data.provider.in.web.GetBookDtoMother.defaultGetBookDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetBookApiShould {

    @InjectMocks
    private GetBookApi getBookApi;

    @Mock
    private GetBookQuery getBookQuery;

    @Mock
    private GetBookDtoMapper getBookDtoMapper;

    public static final long DEFAULT_BOOK_ID = 1;

    @Test
    void createReviewForBook() {
        var book = mock(Book.class);
        var defaultBookDto = defaultGetBookDto();
        given(getBookQuery.getById(DEFAULT_BOOK_ID)).willReturn(book);
        given(getBookDtoMapper.toDto(book)).willReturn(defaultBookDto);

        var bookDto = getBookApi.getBook(DEFAULT_BOOK_ID);

        assertThat(bookDto.id).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(bookDto.title).isEqualTo(defaultBookDto.title);
        assertThat(bookDto.author).isEqualTo(defaultBookDto.author);
    }

}
