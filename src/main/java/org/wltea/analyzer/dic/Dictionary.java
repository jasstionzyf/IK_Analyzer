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
 */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.wltea.analyzer.core.CorpusType;

/**
 * 词典管理类,单子模式
 */
public class Dictionary {

    public static Properties dicProp = new Properties();

    static {
        try {
            //load a properties file from class path, inside static method
            dicProp.load(Dictionary.class.getClassLoader().getResourceAsStream("dic.properties"));

        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    /*
     * 词典单子实例
     */
    private static Dictionary singleton;

    /*
     * 主词典对象
     */
    private DictSegment _MainDict;

    /*
     * 停止词词典 
     */
    private DictSegment _StopWordDict;
    /*
     * 量词词典
     */
    private DictSegment _QuantifierDict;

    /**
     * 配置对象
     */
    private Dictionary() {
        this.loadMainDict();
        this.loadStopWordDict();
        this.loadQuantifierDict();
    }

    /**
     * 获取词典单子实例
     *
     * @return Dictionary 单例对象
     */
    public static Dictionary getSingleton() {
        if (singleton == null) {
            singleton = new Dictionary();
            //throw new IllegalStateException("词典尚未初始化，请先调用initial方法");
        }
        return singleton;
    }

    /**
     * 批量加载新词条
     *
     * @param words Collection<String>词条列表
     */
    public void addWords(Collection<String> words) {
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    //批量加载词条到主内存词典中
                    singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }
    }

    public void addWords(Collection<String> words, CorpusType corpusType) {
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    //批量加载词条到主内存词典中
                    singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray(), corpusType.getCorpusType());
                }
            }
        }
    }

    /**
     * 批量移除（屏蔽）词条
     *
     * @param words
     */
    public void disableWords(Collection<String> words) {
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    //批量屏蔽词条
                    singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }
    }

    /**
     * 检索匹配主词典
     *
     * @param charArray
     * @return Hit 匹配结果描述
     */
    public Hit matchInMainDict(char[] charArray) {
        return singleton._MainDict.match(charArray);
    }

    /**
     * 检索匹配主词典
     *
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return singleton._MainDict.match(charArray, begin, length);
    }

    /**
     * 检索匹配量词词典
     *
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
        return singleton._QuantifierDict.match(charArray, begin, length);
    }

    /**
     * 从已匹配的Hit中直接取出DictSegment，继续向下匹配
     *
     * @param charArray
     * @param currentIndex
     * @param matchedHit
     * @return Hit
     */
    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);
    }

    /**
     * 判断是否是停止词
     *
     * @param charArray
     * @param begin
     * @param length
     * @return boolean
     */
    public boolean isStopWord(char[] charArray, int begin, int length) {
        return singleton._StopWordDict.match(charArray, begin, length).isMatch();
    }

    private void loadDictFromFile(DictSegment _MainDict, String extDictName, int corpusType) {
        //读取扩展词典文件
        System.out.println("加载扩展词典：" + extDictName);
        InputStream is = null;
        try {

            is = new FileInputStream(extDictName);
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
                    //加载扩展词典数据到主内存词典中
                    //System.out.println(theWord);
                    _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray(), corpusType);
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
    }

    /**
     * 加载主词典及扩展词典
     */
    private void loadMainDict() {
        //建立一个主词典实例
        _MainDict = new DictSegment((char) 0);
        String message = dicProp.getProperty("dicPath") + "message.dic";
        String nickname = dicProp.getProperty("dicPath") + "nickname.dic";
        String selfintroduce = dicProp.getProperty("dicPath") + "selfintroduce.dic";
        loadDictFromFile(_MainDict, nickname, CorpusType.NICKNAME.getCorpusType());
        loadDictFromFile(_MainDict, message, CorpusType.MESSAGE.getCorpusType());
        loadDictFromFile(_MainDict, selfintroduce, CorpusType.SELFINTRODUCE.getCorpusType());
        //String message = dicProp.getProperty("dicPath") + "test.dic";
       // loadDictFromFile(_MainDict, message, CorpusType.NICKNAME.getCorpusType());

    }

    /**
     * 加载用户配置的扩展词典到主词库表 双核
     */
    private void loadExtDict() {
        //加载扩展词典配置
        loadNoteBookKeyWords();
        List<String> extDictFiles = new ArrayList<String>();
        extDictFiles.add("" + dicProp.getProperty("dicPath") + "ext.dic");
        if (extDictFiles != null) {
            InputStream is = null;
            for (String extDictName : extDictFiles) {
                //读取扩展词典文件
                System.out.println("加载扩展词典：" + extDictName);
                try {
                    is = new FileInputStream(extDictName);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //如果找不到扩展的字典，则忽略
                if (is == null) {
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord = null;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            //加载扩展词典数据到主内存词典中
                            //System.out.println(theWord);
                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
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
            }
        }

        //
    }

    /**
     * 加载用户扩展的停止词词典
     */
    private void loadStopWordDict() {
        //建立一个主词典实例
        _StopWordDict = new DictSegment((char) 0);
        //
        List<String> extDictFiles = new ArrayList<String>();
        extDictFiles.add("" + dicProp.getProperty("dicPath") + "stopword.dic");
        if (extDictFiles != null) {
            InputStream is = null;
            for (String extDictName : extDictFiles) {
                //读取扩展词典文件
                System.out.println("加载扩展词典：" + extDictName);
                try {
                    is = new FileInputStream(extDictName);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //如果找不到扩展的字典，则忽略
                if (is == null) {
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord = null;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            //加载扩展词典数据到主内存词典中
                            //System.out.println(theWord);
                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
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
            }
        }

    }

    /**
     * 加载量词词典
     */
    private void loadQuantifierDict() {
        //建立一个量词典实例
        _QuantifierDict = new DictSegment((char) 0);
        List<String> extDictFiles = new ArrayList<String>();
        extDictFiles.add("" + dicProp.getProperty("dicPath") + "quantifier.dic");
        if (extDictFiles != null) {
            InputStream is = null;
            for (String extDictName : extDictFiles) {
                //读取扩展词典文件
                System.out.println("加载扩展词典：" + extDictName);
                try {
                    is = new FileInputStream(extDictName);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //如果找不到扩展的字典，则忽略
                if (is == null) {
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord = null;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            //加载扩展词典数据到主内存词典中
                            //System.out.println(theWord);
                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
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
            }
        }

    }

    private void loadNoteBookKeyWords() {
        try {
            InputStream in = new FileInputStream("" + dicProp.getProperty("dicPath") + "notebook_prop_keyValues");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            do {
                line = br.readLine();
                if (line != null && !"".equals(line.trim())) {
                    //line.split(":")[0]
                    String keyWords = line.split(":")[1];
                    if (!keyWords.contains("-")) {
                        String[] theWords = keyWords.split(",");
                        for (String theWord : theWords) {
                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());

                        }
                    }
                    if (keyWords.contains("-")) {
                        String[] temps = keyWords.split(":");
                        String pace = temps[1];

                        //unit=temps[2];
                        String begain = temps[0].split("-")[0];
                        String end = temps[0].split("-")[1];
                        float value1 = Float.parseFloat(begain);
                        float value2 = Float.parseFloat(end);
                        while (value1 <= value2) {
                            String theWord = String.valueOf(value1);//+unit;

                            _MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                            value1 += Float.parseFloat(pace);

                        }
                    }
                }
            } while (line != null);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
