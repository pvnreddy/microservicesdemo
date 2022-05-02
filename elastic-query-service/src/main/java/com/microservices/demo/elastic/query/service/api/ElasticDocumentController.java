package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.impl.TwitterElasticQueryService;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponceModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponceModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final TwitterElasticQueryService twitterElasticQueryService;

    @Value("${server.port}")
    private String port;
    public ElasticDocumentController(TwitterElasticQueryService twitterElasticQueryService) {
        this.twitterElasticQueryService = twitterElasticQueryService;
    }


    @Operation(summary = "Get All Elastic Documents!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful Response", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                    schema = @Schema(implementation = ElasticQueryServiceResponceModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponceModel>> getAllDocuments(){
        List<ElasticQueryServiceResponceModel> response = twitterElasticQueryService.getAllDocuments();
        LOG.info("Eastic search returned {} no of documents",response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Document by id!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful Response", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponceModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceResponceModel> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponceModel response = twitterElasticQueryService.getDocumentById(id);
        LOG.info("Eastic search returned document with id "+id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Document by id!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful Response", content = {
                    @Content(mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponceModelV2.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceResponceModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponceModel response = twitterElasticQueryService.getDocumentById(id);
        LOG.info("Eastic search returned document with id "+id);
        return ResponseEntity.ok(getmodelV2(response));
    }

    @Operation(summary = "Get Document by text!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful Response", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponceModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponceModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
        List<ElasticQueryServiceResponceModel> response = twitterElasticQueryService.getDocumentsByText(elasticQueryServiceRequestModel.getText());
        ElasticQueryServiceResponceModel elasticQueryServiceResponceModel= ElasticQueryServiceResponceModel.builder().text(elasticQueryServiceRequestModel.getText()).build();
        LOG.info("Eastic search returned {} no of documents with text {} with port {}",response.size(), elasticQueryServiceRequestModel.getText(), port);
        response.add(elasticQueryServiceResponceModel);
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponceModelV2 getmodelV2(ElasticQueryServiceResponceModel model1){
        return ElasticQueryServiceResponceModelV2.builder()
                .id(Long.getLong(model1.getId()))
                .userId(model1.getUserId()).text(model1.getText())
                .text2("Version V2 text").build()
                .add(model1.getLinks());
    }

}
