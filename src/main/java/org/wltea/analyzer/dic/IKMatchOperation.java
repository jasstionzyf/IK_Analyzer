/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wltea.analyzer.dic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jasstion
 */
public class IKMatchOperation implements MatchOperation{
    private static Logger log = LoggerFactory.getLogger(IKMatchOperation.class);
     private DictSegment _MainDict=new DictSegment((char) 0);
    
    @Override
    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return _MainDict.match(charArray, begin, length);
    }

    @Override
    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);

    }

    @Override
    public boolean isStopWord(char[] charArray, int begin, int length) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addWord(Word word) {
        String text=word.getText();
        int type=word.getType();
        if(text==null||text.trim().length()==0){
           return;
        }
        _MainDict.fillSegment(text.trim().toLowerCase().toCharArray(), type);
    }
}
