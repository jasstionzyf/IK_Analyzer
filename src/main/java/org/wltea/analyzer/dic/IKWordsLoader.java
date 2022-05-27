/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wltea.analyzer.dic;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jasstion
 */
public class IKWordsLoader implements WordsLoader {

    private static Logger log = LoggerFactory.getLogger(IKWordsLoader.class);

    public static Properties dicProp = new Properties();

    static {
        try {
            //load a properties file from class path, inside static method
            dicProp.load(Dictionary.class.getClassLoader().getResourceAsStream("dic.properties"));

        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    private Collection<Word> loadFromOneFile(String filePath, int corpusType) {
        Collection<Word> words = Lists.newArrayList();
        System.out.println("加载扩展词典：" + filePath);
        InputStream is = null;
        try {

            is = new FileInputStream(filePath);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    words.add(new Word(theWord.trim(), corpusType));
                }
            } while (theWord != null);

        } catch (IOException ioe) {
            System.err.println("Extension Dictionary loading exception.");
            ioe.printStackTrace();

        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return words;

    }

    @Override
    public Collection<Word> load() {
        Collection<Word> words = Lists.newArrayList();
        //读取扩展词典文件
        String dicFile = dicProp.getProperty("dicPath");
//        String message = dicProp.getProperty("dicPath") + "message.dic";//1
//        String nickname = dicProp.getProperty("dicPath") + "nickname.dic";//2
//        String selfintroduce = dicProp.getProperty("dicPath") + "selfintroduce.dic";//3
//        String stopWords = dicProp.getProperty("dicPath") + "stopword.dic";//4

//        words.addAll(loadFromOneFile(nickname, 2));
//        words.addAll(loadFromOneFile(message, 1));
//        words.addAll(loadFromOneFile(selfintroduce, 3));
//        words.addAll(loadFromOneFile(stopWords, 4));
        words.addAll(loadFromOneFile(dicFile, 1));
        return words;

    }

}
