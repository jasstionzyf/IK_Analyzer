/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wltea.analyzer.dic;

/**
 *
 * @author jasstion
 */
public interface MatchOperation {
    public Hit matchInMainDict(char[] charArray, int begin, int length);
    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit);
    public boolean isStopWord(char[] charArray, int begin, int length);
    public void addWord(Word word);
}
