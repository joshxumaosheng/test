import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.Dictionary;

public class SearchIndexTest {
    @Test
    public void testSearchIndex() throws Exception {
        //创建Query 搜索对象
        Analyzer analyzer=new IKAnalyzer();
        QueryParser queryParser=new QueryParser("desc",analyzer);
        Query query=queryParser.parse("desc:全部");
        //创建Directory流对象，声明索引库位置
        FSDirectory dictionary = FSDirectory.open(new File("E:/index").toPath());
        //创建索引搜索对象IndexSearcher，从读出的索引库数据中查找【索引域】
        IndexReader indexReader= DirectoryReader.open(dictionary);
        //创建索引搜索对象IndexSearcher,从读出的索引库数据中查找对应的数据
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        /**
         * 使用索引搜索对象，执行搜索，返回结果集TopDocs,索引域中的下标信息
         */
        TopDocs topDocs = indexSearcher.search(query, 6);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int index = scoreDoc.doc;
            //根据下表获取数据
            Document doc = indexSearcher.doc(index);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("name"));
            System.out.println(doc.get("price"));
            System.out.println(doc.get("pic"));
            System.out.println("======================");
        }
        //释放资源
        indexReader.close();
    }
}
