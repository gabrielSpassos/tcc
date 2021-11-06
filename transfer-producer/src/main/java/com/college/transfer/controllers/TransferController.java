package com.college.transfer.controllers;

import com.college.transfer.controllers.dto.TransferDTO;
import com.college.transfer.model.TransferModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
public class TransferController extends BaseVersion {

    private ProducerTemplate producerTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public TransferController(ProducerTemplate producerTemplate, ObjectMapper objectMapper) {
        this.producerTemplate = producerTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/transfers")
    @ApiOperation(value = "This endpoint create's a transfer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transferDTO", value = "New transfer.",
                    dataType = "TransferDTO", paramType = "body"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = CREATED, message = CREATED_MESSAGE, response = TransferDTO.class),
    })
    public ResponseEntity<?> createEvent(@RequestBody @Valid TransferDTO transferDTO) {
        return Stream.of(transferDTO)
                .map(this::convertDTOtoModel)
                .peek(transferModel -> producerTemplate.sendBody("direct:createTransfer", transferModel))
                .map(this::convertModelToDTO)
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }

    private TransferModel convertDTOtoModel(TransferDTO transferDTO) {
        return objectMapper.convertValue(transferDTO, TransferModel.class);
    }

    private TransferDTO convertModelToDTO(TransferModel transferModel) {
        return objectMapper.convertValue(transferModel, TransferDTO.class);
    }
}
