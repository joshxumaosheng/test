package com.itheima;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElasticSearchTest {
    /**
     * 完成文档对象（数据）增加
     */
    @Test
    public void testCreateIndexJsonDemo1() throws Exception {
        /**
         * 连接客户端对象
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        /**
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 准备增加的数据
         */
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        builder.field("id", 1);
        builder.field("title", "ElasticSearch是一个基于Lucene的搜索服务器。");
        builder.field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
        builder.endObject();
        transportClient.prepareIndex("blog", "article", "1").setSource(builder).get();
        //关闭资源
        transportClient.close();
    }

    /**
     * 完成文档对象（数据）增加
     */
    @Test
    public void testCreateIndexMapDemo2() throws Exception {
        /**
         * 连接客户端对象
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        /**
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 准备增加的数据
         */
       /* XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        builder.field("id", 1);
        builder.field("title", "ElasticSearch是一个基于Lucene的搜索服务器。");
        builder.field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
        builder.endObject();*/
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "2");
        map.put("title", "ElasticSearch是一个基于Lucene的搜索服务器22222。");
        map.put("content", "我是map它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");

        transportClient.prepareIndex("blog2", "article2", "2").setSource(map).get();
        //关闭资源
        transportClient.close();
    }

    @Test
    public void testCreateIndexJsonSearchDemo3() throws Exception {
        /**
         * 连接客户端对象
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        /**
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 根据下标找数据
         * 1:指定索引库 index
         * 2:指定类型 type
         * 3:指定要找的数据的下标 id
         * 4；get()：执行操作
         */
        GetResponse response = transportClient.prepareGet("blog", "article", "1").get();
        //获取数据
        String json = response.getSourceAsString();
        System.out.println(json);
        /**
         * 关闭资源
         */
        transportClient.close();
    }

    @Test
    public void testCreateIndexJsonSearchDemo4() throws Exception {
        /**
         * 连接客户端对象
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        /**
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 查询所有数据
         * QueryBuilder:搜素条件构建对象
         */
        SearchResponse response = transportClient.prepareSearch("blog").setQuery(QueryBuilders.matchAllQuery()).get();
        /**
         * 获取数据结果信息
         */
        SearchHits hits = response.getHits();//搜素到的数据
        long total = hits.getTotalHits();//总记录数
        //获取结果集迭代对象
        Iterator<SearchHit> hitIterator = hits.iterator();
        while (hitIterator.hasNext()) {
            //获取当前数据
            SearchHit hit = hitIterator.next();
            //指定域的数据
            String title = hit.getSource().get("title").toString();
            System.out.println(title);
        }
        /**
         * 关闭资源
         */
        transportClient.close();
    }

    @Test
    public void testCreateIndexJsonSearchDemo5() throws Exception {
        /**
         * 连接客户端对象
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        /**
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 查询所有数据
         */
        SearchResponse response = transportClient.prepareSearch("blog").setTypes("article").setQuery(QueryBuilders.termQuery("title", "搜素")).get();
        /**
         * 获取数据结果信息
         */
        SearchHits hits = response.getHits();//命中数据：搜素道德数据
        long totalHits = hits.getTotalHits();//总记录数
        //获取结果集迭代对象
        Iterator<SearchHit> hitIterator = hits.iterator();
        while (hitIterator.hasNext()) {
            //获取当前数据
            SearchHit hit = hitIterator.next();
            //指定域的数据
            String title = hit.getSource().get("title").toString();
            System.out.println(title);
        }
        /**
         * 关闭资源
         */
        transportClient.close();
    }

    /*****
     * 通配符模糊搜索
     */
    @Test
    public void testCreateIndexJsonSearchDemo7() throws Exception {
        /***
         * [链接客户端对象]->没有集群
         * Settings.EMPTY:无集群
         */
        TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);

        /***
         * 指定ES的IP和端口号
         */
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        /**
         * 查询所有数据
         */
        SearchResponse response = transportClient.prepareSearch("blog").setTypes("article").setQuery(QueryBuilders.wildcardQuery("title", "*搜*")).get();
        /**
         * 获取数据结果信息
         */
        SearchHits hits = response.getHits();//搜素到的数据
        long totalHits = hits.getTotalHits();//总记录数
        //获取结果集迭代对象
        Iterator<SearchHit> hitIterator = hits.iterator();
        while (hitIterator.hasNext()){
            //获取当前数据
            SearchHit hit = hitIterator.next();
            //指定域的数据
            String title = hit.getSource().get("title").toString();
            System.out.println(title);
        }
        /**
         * 关闭资源
         */
        transportClient.close();

    }
}