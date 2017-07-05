package com.xzf.solr.client;

import org.apache.http.protocol.HTTP;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {

	public static final String SOLR_URL = "http://localhost:8080/solr";
	public static final String CORE_NAME = "bbs";
	public static String[] docs = {"Solr是一个独立的企业级搜索应用服务器",
			"它对外提供类似于Web-service的API接口",
			"用户可以通过http请求",
			"向搜索引擎服务器提交一定格式的XML文件生成索引",
			"也可以通过Http Get操作提出查找请求",
			"并得到XML格式的返回结果"};

	public static final SolrClient solrClient;

	static {
		solrClient = new HttpSolrClient.Builder().withBaseSolrUrl(SOLR_URL+"/"+CORE_NAME).build();
	}

	@Test
	public void createIndex() {
		List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
		SolrInputDocument solrDoc = null;
		int i=1;
		for(String doc : docs){
			solrDoc = new SolrInputDocument();
			solrDoc.addField("id",i++);
			solrDoc.addField("content_test",doc);
			docList.add(solrDoc);
		}
		try {
			solrClient.add(docList);
			solrClient.commit();
		}catch (Exception e){

		}
	}

	@Test
	public void search() throws Exception{
		SolrQuery query = new SolrQuery();
		query.setQuery("id:1");
		QueryResponse response = solrClient.query(query);
		System.out.println(response.toString());
		SolrDocumentList resultList = response.getResults();
		System.out.println("找到的文档个数："+resultList.getNumFound());
		for(SolrDocument doc : resultList){
			System.out.println("id:"+doc.getFieldValue("id"));
			System.out.println("content:"+doc.getFieldValue("content_test"));
			System.out.println("-------------------");
		}
	}



}
