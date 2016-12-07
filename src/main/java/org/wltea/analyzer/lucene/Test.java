package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.CorpusType;

import java.io.StringReader;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.print(CorpusType.MESSAGE.getCorpusType());
        //Lucene Document的域名
        String fieldName = "text";
        //检索内容
        String text = "蓝天白云沙滩海边Dr. Paul Gachet";
        //TokenizerFactory t=null;
        //实例化IKAnalyzer分词器
        IKTokenizer iKTokenizer = new IKTokenizer(new StringReader(text), true);
        while (iKTokenizer.incrementToken()) {
            CharTermAttribute charTermAttribute = iKTokenizer.getAttribute(CharTermAttribute.class);
            TypeAttribute typeAttribute = iKTokenizer.getAttribute(TypeAttribute.class);
            String term=charTermAttribute.toString();
            String additionInfo=typeAttribute.type();

            System.out.print(term + "\n");

            System.out.print(additionInfo + "\n");

        }

    }
}
