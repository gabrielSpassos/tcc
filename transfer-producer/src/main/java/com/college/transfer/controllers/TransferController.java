package com.college.transfer.controllers;

import com.college.transfer.controllers.dto.TransferDTO;
import com.college.transfer.routes.TransferRoute;
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

    @Autowired
    public TransferController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
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
                .peek(transfer -> producerTemplate.sendBody("direct:createTransfer", transfer))
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }
}
