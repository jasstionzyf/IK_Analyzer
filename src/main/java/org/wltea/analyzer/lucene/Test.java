package org.wltea.analyzer.lucene;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.CorpusType;

import com.google.common.collect.Lists;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.print(CorpusType.MESSAGE.getCorpusType());
        // Lucene Document的域名
        String fieldName = "text";
        // 检索内容
        // String text = "a boat docked at a dock on the water say he is a good and
        // beautiful girl 虽然我是一个中国人,骨子里热爱中国";
        String text = "gin-za  person walking on  knockko extend nd dock with an umbrella 中国虽然我是一个中国人,骨子里热爱中国";
        String cr = "0";
        String[] words = text.split(" ");
        List<String> words_new = Lists.newArrayList();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            word = cr + word;
            words_new.add(word);

        }
        String text_new = String.join(cr, words_new) + cr;
        text = text_new;

        System.out.println(text);
        // TokenizerFactory t=null;Str

        // 实例化IKAnalyzer分词器
        IKTokenizer iKTokenizer = new IKTokenizer(new StringReader(text), true);
        List<String> kws = new ArrayList<>(0);
        while (iKTokenizer.incrementToken()) {
            CharTermAttribute charTermAttribute = iKTokenizer.getAttribute(CharTermAttribute.class);
            TypeAttribute typeAttribute = iKTokenizer.getAttribute(TypeAttribute.class);
            String term = charTermAttribute.toString();
            String additionInfo = typeAttribute.type();

            System.out.print(term + "\n");

            System.out.print(additionInfo + "\n");
            if (additionInfo.equals("4_1")) {
                kws.add(term);
            }

        }
        System.out.println(kws);

    }
}
