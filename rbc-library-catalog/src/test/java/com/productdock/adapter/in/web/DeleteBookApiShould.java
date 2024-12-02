package com.productdock.adapter.in.web;

import com.productdock.application.port.in.DeleteBookUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteBookApiShould {

    public static final long BOOK_ID = 1;
    @InjectMocks
    private DeleteBookApi deleteBookApi;
    @Mock
    private DeleteBookUseCase deleteBookUseCase;

    @Test
    void deleteBook() {

        deleteBookApi.deleteBook(BOOK_ID);

        verify(deleteBookUseCase).deleteBook(BOOK_ID);
    }
}
