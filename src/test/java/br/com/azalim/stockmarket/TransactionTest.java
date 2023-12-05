package br.com.azalim.stockmarket;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionTest {

    @Test
    public void testConstructor() {

        assertThrows(IllegalArgumentException.class, () -> new Transaction(0, 1), "The quantity must be different than zero");
        assertThrows(IllegalArgumentException.class, () -> new Transaction(1, -1), "The price must be greater than or equal to zero");

    }

    @Test
    public void testCompareTo() {

        Transaction transaction = mock(Transaction.class);
        Transaction otherTransaction = mock(Transaction.class);

        when(transaction.compareTo(otherTransaction)).thenCallRealMethod();

        when(transaction.getInstant()).thenReturn(Instant.ofEpochMilli(2));
        when(otherTransaction.getInstant()).thenReturn(Instant.ofEpochMilli(3));

        assertTrue(transaction.compareTo(otherTransaction) < 0, "The transaction should be before the other transaction");

        when(otherTransaction.getInstant()).thenReturn(Instant.ofEpochMilli(1));

        assertTrue(transaction.compareTo(otherTransaction) > 0, "The transaction should be after the other transaction");

    }

}
