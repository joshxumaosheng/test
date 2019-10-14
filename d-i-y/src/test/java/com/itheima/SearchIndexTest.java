package com.itheima;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;


import java.io.File;
import java.io.IOException;

public class SearchIndexTest {

    public static void main(String[] args) throws Exception {

            // 1. 创建Query搜索对象
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser("desc", analyzer);
            Query query = queryParser.parse("desc:全");
            //2. 创建Directory流对象,声明索引库位置
            Directory directory= FSDirectory.open(new File("E:/index").toPath());
            //3. 创建索引读取对象IndexReader
            IndexReader indexReader = DirectoryReader.open(directory);
            //4. 创建索引搜索对象IndexSearcher
            IndexSearcher indexSearcher=new IndexSearcher(indexReader);
            //5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
            TopDocs docs = indexSearcher.search(query, 5);
            //5.1.拿着索引域中的结果去文档中找
            //6. 解析结果集
            ScoreDoc[] scoreDocs = docs.scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                int index = scoreDoc.doc;
                //根据下标获取数据
                Document doc = indexSearcher.doc(index);
                System.out.println(doc.get("id"));
                System.out.println(doc.get("name"));
                System.out.println(doc.get("price"));
                System.out.println(doc.get("pic"));
                System.out.println("------------------");
            }
            //7. 释放资源
            indexReader.close();

        }
    }



