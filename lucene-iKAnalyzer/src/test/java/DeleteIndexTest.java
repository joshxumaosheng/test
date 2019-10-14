import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class DeleteIndexTest {
    /**
     * 删除实现=IndexWriter操作Document
     */
    @Test
    public void testDeleteByTerms() throws Exception {
        //分词器
        Analyzer analyzer=new IKAnalyzer();
        //往哪里xie
        File file=new File("E:/index");
        Directory directory= FSDirectory.open(file.toPath());
        //配置缓冲区
        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(analyzer);
        //创建操作Document操作对象IndexWriter
        IndexWriter indexWriter=new IndexWriter(directory,indexWriterConfig);
        //将指定域作为条件
        //indexWriter.deleteDocuments(new Term("desc","哈哈"));
        // 删除所有
        indexWriter.deleteAll();
        //提交
        indexWriter.commit();
        //关闭资源
        indexWriter.close();
        directory.close();
    }
}
