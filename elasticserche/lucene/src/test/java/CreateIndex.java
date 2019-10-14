import com.itheima.dao.BookDao;
import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class CreateIndex {
    //创建BookDao实现类的实例，用户采集数据
    private BookDao bookDao = new BookDaoImpl();

    @Test
    public void testCreateIndex() throws Exception {
        //数据采集
        List<Book> books = bookDao.queryBookList();
        //创建一个集合容器存放documet对象
        List<Document> docs = new ArrayList<Document>();
        for (Book book : books) {
            //创建文档对象
            Document doc = new Document();
            doc.add(new TextField("id", book.getId() + "", Field.Store.YES));
            doc.add(new TextField("name", book.getName(), Field.Store.YES));
            doc.add(new TextField("price", book.getPic() + "", Field.Store.YES));
            doc.add(new TextField("pic", book.getPic(), Field.Store.YES));
            doc.add(new TextField("desc", book.getDesc(), Field.Store.YES));
            docs.add(doc);
        }
        //创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        //索引存储位置
        Directory directory = FSDirectory.open((new File("E:/index")).toPath());

        //IndexWriterConfig配置了IndexWriter对象的参数信息
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        //创建IndexWriter写入对象  Directory d, IndexWriterConfig conf
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

        //将文档写入到索引库
        indexWriter.addDocuments(docs);

        //提交操作
        indexWriter.commit();

        //回收资源
        indexWriter.close();

    }
}