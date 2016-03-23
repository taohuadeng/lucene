package com.thd.lucene;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import java.io.IOException;

public class HelloLuceneTest {
    @Test
    public void testIndex() {
        HelloLucene lucene = new HelloLucene();
        lucene.index();
    }

    @Test
    public void testSearch() throws IOException, ParseException {
        HelloLucene lucene = new HelloLucene();
        lucene.search();
    }
}
