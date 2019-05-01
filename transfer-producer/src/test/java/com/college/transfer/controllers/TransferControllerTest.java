package com.college.transfer.controllers;

import com.college.transfer.TestConfig;
import com.college.transfer.model.TransferModel;
import com.college.transfer.rabbit.EventProducer;
import com.college.transfer.stub.TransferDTOStub;
import com.college.transfer.stub.TransferModelStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransferController.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProducerTemplate producerTemplate;
    @MockBean
    private EventProducer eventProducer;

    private TransferDTOStub transferDTOStub;
    private TransferModelStub transferModelStub;

    @Before
    public void setup() {
        this.transferDTOStub = new TransferDTOStub();
        this.transferModelStub = new TransferModelStub();
    }

    @Test
    public void shouldProduceTransferEvent() throws Exception {
        given(producerTemplate.requestBody(eq("direct:createTransfer"), any(TransferModel.class)))
                .willReturn(transferModelStub.buildTransferModel("10", "001", "002"));

        mockMvc.perform(post("/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(new ModelMapper().map(transferDTOStub.buildTransferDTO(BigDecimal.TEN, "001", "002"), String.class))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value("10"))
                .andExpect(jsonPath("originAccountNumber").value("001"))
                .andExpect(jsonPath("destinyAccountNumber").value("002"));
    }

    @Test
    public void shouldReturnMissingAmount() throws Exception {
        mockMvc.perform(post("/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(new ModelMapper().map(transferDTOStub.buildTransferDTO(null, "001", "002"), String.class))
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("The amount to transfer must be informed"));
    }
}
