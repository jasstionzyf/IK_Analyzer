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
public class Dictionary {

    private static Logger log = LoggerFactory.getLogger(Dictionary.class);

    static Dictionary dictionary = null;

    public static Dictionary getSingleton() {
        WordsLoader wordsLoader = new IKWordsLoader();
        MatchOperation matchOperation = new IKMatchOperation();
        if (dictionary == null) {
            dictionary = new Dictionary(wordsLoader, matchOperation);
        }

        return dictionary;

    }

    public static synchronized void setDictionary(Dictionary dictionary) {
        Dictionary.dictionary = dictionary;

    }
    private WordsLoader wordsLoader = null;
    private MatchOperation matchOperation = null;

    public Dictionary(WordsLoader wordsLoader, MatchOperation matchOperation) {
        this.wordsLoader = wordsLoader;
        this.matchOperation = matchOperation;
        init();
    }

    private void init() {
        Iterable<Word> words = wordsLoader.load();
        for (Word word : words) {
            this.addWord(word);
        }
    }

    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return matchOperation.matchInMainDict(charArray, begin, length);
    }

    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        return matchOperation.matchWithHit(charArray, currentIndex, matchedHit);

    }

    public boolean isStopWord(char[] charArray, int begin, int length) {
        return matchOperation.isStopWord(charArray, begin, length);
    }

    public void addWord(Word word) {
        matchOperation.addWord(word);
    }

}
