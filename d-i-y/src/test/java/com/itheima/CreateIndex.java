package com.itheima;

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
        import org.apache.lucene.store.FilterDirectory;
        import org.junit.Test;

        import java.io.File;

public class CreateIndex {
    //引入BookDao做成员变量
    private BookDao bookDao = new BookDaoImpl();
    //写个方法来创建索引库
    @Test
    public void testCreateIndex() throws Exception{
        //第一步获取数据库数据
        Book book=new Book(1,"西游记",100F,"http://www.itcast.1.jpg","背景很重要");
        //将对象转换成Lucene所需要的Document类型的对象
            Document doc=new Document();

        doc.add(new TextField("id",book.getId().toString(), Field.Store.YES));
        doc.add(new TextField("name",book.getName(), Field.Store.YES));
        doc.add(new TextField("pic",book.getPic().toString(), Field.Store.YES));
        doc.add(new TextField("price",book.getPrice().toString(), Field.Store.YES));
        doc.add(new TextField("desc",book.getPic().toString(), Field.Store.YES));
        //将这些Document的对象存放到硬盘的具体位置
        File file=new File("E:/index");
        Directory directory=FSDirectory.open(file.toPath());
        //创建分词
        Analyzer analyzer=new StandardAnalyzer();
        //缓冲区
        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(analyzer);
        //创建Document操作对象IndexWriter(Dao)
        IndexWriter indexWriter=new IndexWriter(directory,indexWriterConfig);
        //将doc对象传入索引库中
        indexWriter.addDocument(doc);
        indexWriter.close();
        directory.close();
    }
}
