/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wltea.analyzer.dic;

import java.io.Serializable;

/**
 *
 * @author jasstion
 */
public class Word implements Serializable{

    private int type =-1;
    private String text = null;

   public  Word(String trim, int corpusType) {
       this.text=trim;
       this.type=corpusType;
    }

    @Override
    public String toString() {
        return "Word{" + "type=" + type + ", text=" + text + '}';
    }

    public Word() {
        super();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

   
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
