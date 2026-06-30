import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.transactionservice.controller.TransactionController;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testSubmitTransaction_Success() throws Exception {
        TransactionRequest request = new TransactionRequest(123L, 100.0);
        TransactionResponse response = new TransactionResponse(123L, "COMPLETED");

        when(transactionService.submitTransaction(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transactionId\":\"txn123\",\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("txn123"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void testGetTransactionStatus_Success() throws Exception {
        TransactionResponse response = new TransactionResponse("txn123", "COMPLETED");

        when(transactionService.getTransactionStatus("txn123")).thenReturn(response);

        mockMvc.perform(get("/transactions/txn123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("txn123"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void testGetTransactionStatus_NotFound() throws Exception {
        when(transactionService.getTransactionStatus("txn999")).thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(get("/transactions/txn999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSubmitTransaction_Duplicate() throws Exception {
        TransactionRequest request = new TransactionRequest("txn123", 100.0);
        when(transactionService.submitTransaction(any(TransactionRequest.class)))
                .thenThrow(new DuplicateTransactionException("Duplicate transaction"));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transactionId\":\"txn123\",\"amount\":100.0}"))
                .andExpect(status().isConflict());
    }
}