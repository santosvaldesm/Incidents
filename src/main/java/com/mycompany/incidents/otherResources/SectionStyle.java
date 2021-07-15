/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.otherResources;

import javax.swing.text.SimpleAttributeSet;

/**
 *
 * @author santvamu
 */


public class SectionStyle {
	String value= "";
	SimpleAttributeSet style=null;

	public SectionStyle(String value,SimpleAttributeSet style) {
		this.value = value;
		this.style = style;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SimpleAttributeSet getStyle() {
		return style;
	}

	public void setStyle(SimpleAttributeSet style) {
		this.style = style;
	}
	
}
