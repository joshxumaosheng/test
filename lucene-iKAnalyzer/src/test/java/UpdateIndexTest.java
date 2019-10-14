import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;


public class UpdateIndexTest {
    /**
     * 索引修改实现
     */
    @Test
    public void testUpdateIndex() throws Exception {
        //分词器
        Analyzer analyzer=new IKAnalyzer();
        //往哪里写
        File file=new File("D:/index");
       Directory directory = FSDirectory.open(file.toPath());
       //配置往文件中写的 缓冲区
        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(analyzer);
        //创建操作Document操作对象IndexWriter
        IndexWriter indexWriter=new IndexWriter(directory,indexWriterConfig);
        //创建一个Document
        Document document=new Document();
        //将book转成document
        document.add(new TextField("id","10000", Field.Store .YES));
        document.add(new TextField("name","打电话", Field.Store .YES));
        //修改
        indexWriter.updateDocument(new Term("desc","全部"),document);
        //提交
        indexWriter.commit();
    }
}
