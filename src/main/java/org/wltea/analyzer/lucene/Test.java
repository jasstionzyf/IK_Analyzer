package org.wltea.analyzer.lucene;

import java.io.StringReader;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.CorpusType;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.print(CorpusType.MESSAGE.getCorpusType());
        //Lucene Document的域名
        String fieldName = "text";
        //检索内容
        String text = "直升机精品电影在线看精品电影赵玉飞靠你妈妈淫穴\n" +
"淫妻\n" +
"乱交\n" +
"淫妹\n" +
"水野\n" +
"仲村\n" +
"饭岛\n" +
"嫩穴";
        //TokenizerFactory t=null;
        //实例化IKAnalyzer分词器
        IKTokenizer iKTokenizer = new IKTokenizer(new StringReader(text), true);
        while (iKTokenizer.incrementToken()) {
            CharTermAttribute charTermAttribute = iKTokenizer.getAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = iKTokenizer.getAttribute(OffsetAttribute.class);
            TypeAttribute typeAttribute = iKTokenizer.getAttribute(TypeAttribute.class);
            System.out.print(charTermAttribute.toString() + "\n");
            System.out.print(offsetAttribute.startOffset() + "-" + offsetAttribute.endOffset() + "\n");
            System.out.print(typeAttribute.type() + "\n");

        }

    }
}
