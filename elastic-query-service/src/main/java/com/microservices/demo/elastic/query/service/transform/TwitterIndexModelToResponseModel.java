package com.microservices.demo.elastic.query.service.transform;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponceModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TwitterIndexModelToResponseModel {

    public ElasticQueryServiceResponceModel getResponseModel(TwitterIndexModel doc){

        return ElasticQueryServiceResponceModel.builder()
                .id(doc.getId())
                .text(doc.getText())
                .userId(doc.getUserId())
                .createdAt(doc.getCreatedAt()).build();

    }

    public List<ElasticQueryServiceResponceModel> getResponseModels(List<TwitterIndexModel> docs){
        return docs.stream().map(this :: getResponseModel).collect(Collectors.toList());
    }



}
