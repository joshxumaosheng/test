package com.itheima;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Article;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElasticSearchTest {
    /**
     * 创建索引
     */
    @Test
    public void testCreateIndexDemo1() throws Exception {
        //创建TransportClient,并设置不使用集群[]
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //设置IP和端口
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建指定索引
        client.admin().indices().prepareCreate("blog1").get();
        //关闭资源
        client.close();
    }

    /**
     * 删除索引
     *
     * @throws Exception
     */

    @Test
    public void testDeleteIndexDemo2() throws Exception {
        //创建TransportClient,并设置不使用集群[]
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //设置IP和端口
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建指定索引
        client.admin().indices().prepareDelete("blog").get();
        //关闭资源
        client.close();
    }

    /**
     * 创建映射
     */
    @Test
    public void testCreateMappingDemo3() throws Exception {
        //创建TransportClient,并设置不适用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //设置IP和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建指定索引
        client.admin().indices().prepareCreate("blog2").get();
        //通过XcontentBuilder构建映射关系
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id")
                .field("type", "long").endObject()

                .startObject("title")
                .field("type", "string")
                .field("store", "false")
                .field("analyzer", "ik_smart").endObject()

                .startObject("content")
                .field("type", "string")
                .field("store", "false")
                .field("analyzer", "ik_smart").endObject()

                .endObject()
                .endObject()
                .endObject();
        //创建映射
        PutMappingRequest mapping = Requests.putMappingRequest("blog2").type("article").source(builder);
        System.out.println(mapping);
        client.admin().indices().putMapping(mapping);
        //关闭资源
        client.close();

    }

    /**
     * 创建索引数据，通过XcontentBuilder对象创建
     */
    @Test
    public void testPutIndexDataDemo4() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //通过XcontentBuilder构建数据
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("id", "2");
        builder.field("title", "增加一条ElasticSearch是一个基于Lucene的搜索服务器,深圳黑马训练营！");
        builder.field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
        builder.endObject();
        //使用TransportClient对象增加数据
        client.prepareIndex("blog1", "article", "2").setSource(builder).get();
        //关闭资源
        client.close();
    }

    @Test
    public void testPutBeanIndexDataDemo5() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建Article
        Article article = new Article();
        article.setId(2);
        article.setTitle("做全文搜素引擎工资属于科研工作，Elasticsearch是基于lucene封装而来的");
        article.setContent("我们希望我们的搜素解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式");
        //json转换对象
        ObjectMapper objectMapper = new ObjectMapper();
        //创建索引数据
        client.prepareIndex("blog2", "article", article.getId().toString())
                .setSource(objectMapper.writeValueAsString(article).getBytes(), XContentType.JSON).get();
        //关闭资源
        client.close();
    }

    /**
     * 使用prepareUpdate修改
     */
    @Test
    public void testUpdateIndexDataDemo5() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建Article
        Article article = new Article();
        article.setId(2);
        article.setTitle("做全文搜素引擎工资属于科研工作，Elasticsearch是基于lucene封装而来的");
        article.setContent("我们希望我们的搜素解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式");
        //json转换对象
        ObjectMapper objectMapper = new ObjectMapper();
        //更新索引数据
        client.prepareUpdate("blog2", "article", article.getId().toString())
                .setDoc(objectMapper.writeValueAsString(article).getBytes(), XContentType.JSON).get();
        //关闭资源
        client.close();
    }

    /**
     * 直接用Update修改
     */
    @Test
    public void testUpdateIndexDataDemo6() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建Article
        Article article = new Article();
        article.setId(2);
        article.setTitle("*****做全文搜素引擎工资属于科研工作，Elasticsearch是基于lucene封装而来的");
        article.setContent("****我们希望我们的搜素解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式");
        //json转换对象
        ObjectMapper objectMapper = new ObjectMapper();
        //更新索引数据
        client.update(new UpdateRequest("blog2", "article", article.getId().toString()).doc(objectMapper.writeValueAsString(article), XContentType.JSON)).get();
        //关闭资源
        client.close();
    }

    /**
     * 通过prepareDelete删除索引数据
     */
    @Test
    public void testDeleteIndexDemo7() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //删除
        client.prepareDelete("blog2", "article", "2").get();
        client.close();
    }

    /**
     * 直接delete删除
     *
     * @throws Exception
     */
    @Test
    public void testDeleteIndexDemo8() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //删除
        client.delete(new DeleteRequest("blog2", "article2", "2")).get();
        client.close();
    }

    /**
     * 批量增加
     */
    @Test
    public void testPutIndexDataBatch() throws Exception {
        //创建TransportClient,并设置不使用集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //创建ip和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //Json转换对象
        ObjectMapper objectMapper = new ObjectMapper();
        //批量增加数据构建对象
        BulkRequestBuilder builder = client.prepareBulk();
        for (int i = 0; i < 50; i++) {
            //创建Article对象
            Article article = new Article();
            article.setId(i);
            article.setTitle("为中华民族之崛起而读书-周恩来");
            article.setContent("每个优秀的人，都有一段沉默的时光。那段时光，是付出了很多努力，却得不到结果的日子，我们把它叫做扎根。");
            builder.add(client.prepareIndex("blog2", "article", article.getId().toString()).setSource(objectMapper.writeValueAsString(article), XContentType.JSON));
            //执行操作
            builder.execute().actionGet();

        }
        //关闭资源
        client.close();
    }

    /**
     * 字符串查询
     */
    @Test
    public void testQueryIndexByString() throws Exception {
        //创建客户端对象，设定没有集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        //通过字符串查询
        SearchResponse response = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.queryStringQuery("读书").field("title")).get();
        //获取命中数据
        SearchHits hits = response.getHits();
        //获取集合数据
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            //获取当前数据
            SearchHit hit = iterator.next();
            //获取数据结果
            String source = hit.getSourceAsString();
            System.out.println(source);
        }

        client.close();
    }

    /**
     * 通过词条查询
     *
     * @throws Exception
     */
    @Test
    public void testQueryIndexByTerm() throws Exception {
        //创建客户端对象，设定没有集群
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        //设置IP和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //通过词条查询
        SearchResponse response = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.termQuery("title", "读书")).get();

        //获取命中数据
        SearchHits hits = response.getHits();
        System.out.println(hits);

        //获取集合数据
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            //获取当前数据
            SearchHit hit = iterator.next();
            //获取数据结果
            String source = hit.getSourceAsString();
            System.out.println(source);
        }

        client.close();
    }
    /**
     * 字符串查询，使用jackson将数据转成javabean，
     */
    @Test
    public void testQueryJavaBeanQueryDemo() throws Exception{
        //创建TransportClient,并设置不用集群
        TransportClient client=new PreBuiltTransportClient(Settings.EMPTY);
                //设置ip和端口
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
        //设置搜索
        SearchResponse response = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.queryStringQuery("读书").field("title")).get();
        //json转换对象
        ObjectMapper objectMapper=new ObjectMapper();
        //获取命中数据
        SearchHits hits = response.getHits();
        //获取集合数据
        Iterator<SearchHit> iterator = hits.iterator();
        //定义一个集合
        List<Article> articles=new ArrayList<Article>();
        while (iterator.hasNext()){
            //获取当前数据
            SearchHit hit = iterator.next();
            //获取数据结果
            String result = hit.getSourceAsString();
            //将json数据转成Javabean
            Article article = objectMapper.readValue(result, Article.class);
            articles.add(article);
        }
        //循环输出
        for (Article article : articles) {
            System.out.println(article);
        }
        //关闭资源
        client.close();
    }
    /**
     * 组合查询
     */
    @Test
    public void testBoolQuery()throws Exception{
        //创建TransportClient,并设置无集群
        TransportClient client=new PreBuiltTransportClient(Settings.EMPTY);
        //设置IP和端口号
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
        //执行搜索
        SearchResponse response = client.prepareSearch("blog2")
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.queryStringQuery("读书")
                                .field("title"))
                        .must(QueryBuilders.rangeQuery("id").from(2).to(10))).get();
        //jason转换成对象
        ObjectMapper objectMapper=new ObjectMapper();
        //获取命中数
        SearchHits hits = response.getHits();
        //获取集合数据
        Iterator<SearchHit> iterator = hits.iterator();
        //定义一个集合
        List<Article> articles=new ArrayList<Article>();
        while (iterator.hasNext()){
            //获取当前数据
            SearchHit hit = iterator.next();
            //获取数据结果
            String result = hit.getSourceAsString();
            //将jason数据转换成JavaBean
            Article article = objectMapper.readValue(result, Article.class);
            articles.add(article);
        }
        //循环输出
        for (Article article : articles) {
            System.out.println(article);
        }
        //关闭资源
        client.close();
    }
    /**
     * 分页排序
     */
    @Test
    public void testPageAndSort() throws Exception{
        //创建TransportClient
        TransportClient client=new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
        //执行搜索
        SearchResponse response = client.prepareSearch("blog2")
                .setTypes("article")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFrom(0)
                .setSize(3)
                .addSort("id", SortOrder.ASC).get();
        //jason转换对象
        ObjectMapper objectMapper=new ObjectMapper();
        //获取命中数
        SearchHits hits = response.getHits();
        //获取集合数据
        Iterator<SearchHit> iterator = hits.iterator();
        //定义一个集合
        List<Article> articles=new ArrayList<Article>();
        while (iterator.hasNext()){
            //获取当前数据
            SearchHit hit = iterator.next();
            //获取当前数据结果
            String result = hit.getSourceAsString();
            //将json字符串转对象
            Article article = objectMapper.readValue(result, Article.class);
            articles.add(article);
        }
        //循环输出
        for (Article article : articles) {
            System.out.println(article);
        }
        //关闭资源
        client.close();
    }
}