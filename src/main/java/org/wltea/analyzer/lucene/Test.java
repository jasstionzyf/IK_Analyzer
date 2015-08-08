package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class Test{
public static void main(String[] args) throws Exception{
	//Lucene Document的域名
	String fieldName = "text";
	 //检索内容
	String text=
"公路局正在治理解放大道路面积水问题";	String keyword = "积水";
	//TokenizerFactory t=null;
	//实例化IKAnalyzer分词器
	Analyzer analyzer = new IKAnalyzer(true);
	// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_41); 
 
	 File indexDir =null; 
    Directory directory =null;  
    IndexWriter iwriter = null;
	IndexReader ireader = null;
	IndexSearcher isearcher = null;
	try {
		//建立内存索引对象
		
		//配置IndexWriterConfig
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_41 , analyzer);
		iwConfig.setOpenMode(OpenMode.CREATE);
		indexDir=new File("/data/test/");
		directory= FSDirectory.open(indexDir);

		iwriter = new IndexWriter(directory , iwConfig);
	       //FSDirectory directory = FSDirectory.getDirectory(indexDir,false);   
		//写入索引
		Document doc = new Document();
		doc.add(new StringField("ID", "10000",  Field.Store.YES));
		FieldType fieldType=new FieldType();
		fieldType.setIndexed(true);
		fieldType.setStored(true);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(true);

		Field field =new Field(fieldName,text.trim(),fieldType);//(fieldName, text.trim(), Field.Store.YES, Field.Index.ANALYZED,TermVector.YES); //new Field(fieldName, text, Field.Store.YES, Field.Index.ANALYZED);
        doc.add(field);

		iwriter.addDocument(doc);
		iwriter.commit();
		iwriter.close();
		
		
		//搜索过程**********************************
	    //实例化搜索器   
		 ireader = DirectoryReader.open(FSDirectory.open(indexDir));  		
			isearcher = new IndexSearcher(ireader);			

		//使用QueryParser查询分析器构造Query对象
		QueryParser qp = new QueryParser(Version.LUCENE_41, fieldName, analyzer);
		qp.setDefaultOperator(QueryParser.AND_OPERATOR);
		Term term = new Term(fieldName,keyword.toLowerCase());   
	    TermQuery query = new TermQuery(term); 
	       
		//搜索相似度最高的5条记录
		TopDocs topDocs = isearcher.search(query , 5);
		System.out.println("命中：" + topDocs.totalHits);
		//输出结果
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (int i = 0; i < topDocs.totalHits; i++){
			Document targetDoc = isearcher.doc(scoreDocs[i].doc);
			Terms terms=ireader.getTermVector(scoreDocs[i].doc, fieldName);
			if(terms==null){
				continue;
			}
			TermsEnum te = terms.iterator(null);

			BytesRef t=null;
			while ((t = te.next()) != null) {
				System.out.print(t.utf8ToString()+"\n");

			}

			
		}
	} catch (CorruptIndexException e) {
		e.printStackTrace();
	} catch (LockObtainFailedException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally{
		if(ireader != null){
			try {
				ireader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(directory != null){
			try {
				directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
}
