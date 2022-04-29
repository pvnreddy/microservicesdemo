package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.impl.TwitterElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final TwitterElasticQueryService twitterElasticQueryService;

    public ElasticDocumentController(TwitterElasticQueryService twitterElasticQueryService) {
        this.twitterElasticQueryService = twitterElasticQueryService;
    }

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponceModel>> getAllDocuments(){
        List<ElasticQueryServiceResponceModel> response = twitterElasticQueryService.getAllDocuments();
        LOG.info("Eastic search returned {} no of documents",response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceResponceModel> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponceModel response = twitterElasticQueryService.getDocumentById(id);
        LOG.info("Eastic search returned document with id",id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponceModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
        List<ElasticQueryServiceResponceModel> response = twitterElasticQueryService.getDocumentsByText(elasticQueryServiceRequestModel.getText());
        ElasticQueryServiceResponceModel elasticQueryServiceResponceModel= ElasticQueryServiceResponceModel.builder().text(elasticQueryServiceRequestModel.getText()).build();
        LOG.info("Eastic search returned {} no of documents with text {}",response.size(), elasticQueryServiceRequestModel.getText());
        response.add(elasticQueryServiceResponceModel);
        return ResponseEntity.ok(response);
    }
}
