package com.thd.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class HelloLucene {

    /**
     * 建立索引
     */
    public void index() {
        IndexWriter writer = null;
        try {
            //1、创建Directory
            //Directory directory = new RAMDirectory();//创建在内存中
            Directory dir = FSDirectory.open(Paths.get("E:/lucene/02/index"));
            //2、创建IndexWriter
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(dir, iwc);
            //3、创建Document对象
            Document doc = new Document();
            //4、为Document对象添加TextField
            File file = new File("E:/lucene/02/content");
            for (File f : file.listFiles()) {
                doc = new Document();
                doc.add(new TextField("content", new FileReader(f)));

                doc.add(new TextField("filename", f.getName(), Field.Store.YES));
                doc.add(new TextField("path", f.getAbsolutePath(), Field.Store.YES));
            }

            //5、通过IndexWriter添加文档到索引中
            writer.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索
     */
    public void search() throws IOException, ParseException {
        String field = "content";
        String queryString = "test3";
        int hitsPerPage = 10;

        //1、创建Directory
        Directory dir = FSDirectory.open(Paths.get("E:/lucene/02/index"));
        //2、创建IndexReader
        IndexReader reader = DirectoryReader.open(dir);
        //3、根据IndexReader创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        //4、创建搜索的Query
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(queryString);
        System.out.println("Searching for: " + query.toString(field));
        //5、根据searcher搜索并且返回TopDocs
        TopDocs topDocs = searcher.search(query, 100);
        //6、根据TopDocs获取ScoreDoc
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //7、根据searcher和ScoreDoc对象获取具体的Document对象
            Document document = searcher.doc(scoreDoc.doc);
            //8、根据Document对象获取具体的值
            System.out.println(document.get("filename") + "[" + document.get("path") + "]");
        }

        //9、关闭reader
        reader.close();
    }
}
