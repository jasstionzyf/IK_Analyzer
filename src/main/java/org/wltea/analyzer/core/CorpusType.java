/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wltea.analyzer.core;

/**
 *
 * @author jasstion
 */
public enum CorpusType {
        MESSAGE(1),NICKNAME(2),SELFINTRODUCE(3),STOP(4);
	private int corpusType=-1;
	private CorpusType(int value) {
		this.corpusType = value;
	}
        public int getCorpusType(){
            return corpusType;
        }

    @Override
    public String toString() {
        return String.valueOf(corpusType);
    }
        
	
}
