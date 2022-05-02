package com.microservices.demo.elastic.query.service.business;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponceModel;

import java.util.List;

public interface ElasticQueryService {
    ElasticQueryServiceResponceModel getDocumentById(String id);
    List<ElasticQueryServiceResponceModel> getDocumentsByText(String text);
    List<ElasticQueryServiceResponceModel> getAllDocuments();
}
