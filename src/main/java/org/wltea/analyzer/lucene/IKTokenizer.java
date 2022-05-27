/**
 * IK 中文分词 版本 5.0 IK Analyzer release 5.0
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供 版权声明 2012，乌龙茶工作室 provided by Linliangyi
 * and copyright 2012 by Oolong studio
 *
 *
 *
 */
package org.wltea.analyzer.lucene;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * IK分词器 Lucene Tokenizer适配器类 兼容Lucene 3.1以上版本
 */
public final class IKTokenizer extends Tokenizer {

    private static Logger log = LoggerFactory.getLogger(IKTokenizer.class);

    //IK分词器实现
    private IKSegmenter _IKImplement;
    //词元文本属性
    private CharTermAttribute termAtt;
    //词元位移属性
    private OffsetAttribute offsetAtt;
    //记录最后一个词元的结束位置
    private int finalOffset;
    private TypeAttribute typeAttribute;

    /**
     * Lucene 3.5 Tokenizer适配器类构造函数
     *
     * @param in
     * @param useSmart
     */
    public IKTokenizer(Reader in, boolean useSmart) {
        super();
        setReader(in);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);

        _IKImplement = new IKSegmenter(in, useSmart);
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        //清除所有的词元属性
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if (nextLexeme != null) {
            //将Lexeme转成Attributes
            //设置词元文本
            termAtt.append(nextLexeme.getLexemeText());
            //	System.out.print(nextLexeme.getLexemeText()+"\n");
            //设置词元长度
            termAtt.setLength(nextLexeme.getLength());
            //设置词元位移
            offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            //记录分词的最后位置
            finalOffset = nextLexeme.getEndPosition();
            List<Integer> types=nextLexeme.getCorpusTypes();
            List<String> types_str= Lists.newArrayList();

            for(Integer type:types){
                types_str.add(String.valueOf(type));

            }
            String corpusTypeStr= StringUtils.join(types_str,"_");


            typeAttribute.setType(String.valueOf(nextLexeme.getLexemeType() + "_" + corpusTypeStr));
            //返会true告知还有下个词元
            return true;
        }
        //返会false告知词元输出完毕
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
     */
    public void reset(Reader input) throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }

    @Override
    public final void end() {
        // set final offset 
        offsetAtt.setOffset(finalOffset, finalOffset);
    }
}
