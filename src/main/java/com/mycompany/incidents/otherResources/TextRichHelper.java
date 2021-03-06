package com.mycompany.incidents.otherResources;

import java.awt.Color;
import java.util.HashMap;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TextRichHelper {

  public StyleContext sc = new StyleContext();
  public DefaultStyledDocument doc = new DefaultStyledDocument(sc);
  public String[][] matrizTokenizada=null;  
  
  Style blackFontStyle         = sc.addStyle("ConstantWidth", null);
  Style redFontStyle           = sc.addStyle("ConstantWidth", null);
  Style redDarkFontStyle       = sc.addStyle("ConstantWidth", null);
  Style greenFontStyle         = sc.addStyle("ConstantWidth", null);
  Style magentaFontStyle       = sc.addStyle("ConstantWidth", null);  
  
  Style blueFontStyle          = sc.addStyle("ConstantWidth", null);      
  Style blueFontStyle1         = sc.addStyle("ConstantWidth", null);    
  Style blueFontStyle2         = sc.addStyle("ConstantWidth", null);    
  Style blueFontStyle3         = sc.addStyle("ConstantWidth", null);    
  Style blueFontStyle4         = sc.addStyle("ConstantWidth", null);      
  Style grayFontStyle          = sc.addStyle("ConstantWidth", null);  
  Style blueLigthFontStyle     = sc.addStyle("ConstantWidth", null);  
  Style yellowHighlightedStyle = sc.addStyle("ConstantWidth", null);
  Style greenHighlightedStyle  = sc.addStyle("ConstantWidth", null);
  
  HashMap<String,Style> mapKeyWords = new HashMap<String,Style>();
  JTextPane aTextPane = new JTextPane();
  
  public String searchedText = "";
  
  public TextRichHelper(JTextPane aTextPane){    
    this.aTextPane = aTextPane;
    configureStyles();
    configureKeyWords();
  } 
  
  private void configureKeyWords(){    
    mapKeyWords.put("ERROR",blueFontStyle1);
    mapKeyWords.put("SOLUC",blueFontStyle2);
    mapKeyWords.put("CAUSA",blueFontStyle3);     
    mapKeyWords.put("COLA_SALIDA",blueFontStyle4); 
    mapKeyWords.put("COLA_ENTRADA",blueFontStyle4); 
    mapKeyWords.put("DESCRIPCION",blueFontStyle4); 
    mapKeyWords.put("SCRIPT",blueFontStyle4);    
  }
  
  private void configureFontStyle(Style aStyle, Color aColor, boolean isBold, int fontSize){
    StyleConstants.setForeground(aStyle,aColor);
    StyleConstants.setBold(aStyle,isBold);    
  }
  
  private void configureStyles(){    
    aTextPane.setStyledDocument(doc);
    try {
      doc.insertString(0, "", null);//sirve para aplicar style desde primer letra
      configureFontStyle(redFontStyle,       Color.red,  true, 20);
      configureFontStyle(blueFontStyle,      Color.blue,            true, 20);      
      configureFontStyle(greenFontStyle,     new Color(0,150,0),    true, 20);
      configureFontStyle(magentaFontStyle,   new Color(179,0,179),  true, 20);
      
      configureFontStyle(redDarkFontStyle,   new Color(201,60,32),  true, 20);
      configureFontStyle(blueFontStyle1,     new Color(31,72,186),  true, 20);
      configureFontStyle(blueFontStyle2,     new Color(74,170,192), true, 20);
      configureFontStyle(blueFontStyle3,     new Color(1,113,205),  true, 20);
      configureFontStyle(blueFontStyle4,     new Color(86,116,255), true, 20);      
      
      configureFontStyle(blueLigthFontStyle, Color.blue,            true, 20);      
      
      configureFontStyle(blackFontStyle,     Color.black,           false,20);
      configureFontStyle(grayFontStyle,      new Color(150,150,150),false,20);      
      
      StyleConstants.setBackground(yellowHighlightedStyle,new Color(255,200,0) );
      StyleConstants.setBackground(greenHighlightedStyle, new Color(144,226,89));
            
      applyStyles();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }  
    
  private void setStyle(int start,int length, Style aStyle ){
    doc.setCharacterAttributes(start, length, aStyle, true);
  }
  
  public String normalizeText(String text){
    return text.toLowerCase().replaceAll("á", "a").replaceAll("é", "e")
            .replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "a");    
  }
    
  private void setHighlightedStyle() {    
    if(searchedText.length()>1){
      String styledText = normalizeText(getStyledText());
      int position = styledText.indexOf(searchedText, 0); 
      while(position != -1){         
        doc.setCharacterAttributes(position, searchedText.length(), yellowHighlightedStyle,false);
        position= styledText.indexOf(searchedText, position+searchedText.length());
      }
    }
  }
  
  private void setSectionStyle(String startText, String endText, Style anStyle) {    
    String styledText = normalizeText(getStyledText());      
    int positionStart = 0;
    int positionEnd = 0;
    int lenght = 0;
    boolean found = true;          
    while(found) {
      found=false;      
      positionStart = styledText.indexOf(startText, positionEnd); 
      if(positionStart != -1) {        
        positionEnd= styledText.indexOf(endText, positionStart+1);
        if(positionEnd != -1) {
          positionEnd = positionEnd + endText.length();          
          doc.setCharacterAttributes(positionStart, positionEnd-positionStart, anStyle,false);          
          found = true;
        }
      }      
    }          
  } 
  
  private String getStyledText(){
   String styledText = "";    
    try {
      styledText =  aTextPane.getStyledDocument().getText(0, aTextPane.getStyledDocument().getLength());
    } catch (BadLocationException ex) {
      System.out.println("ERROR: "+ex.getMessage()); 
    }  
    return styledText;
  } 
  
  public void applyStyles() {
    
    String styledText = getStyledText();
    int currentPosition = 0;
    String currentWord = "";
    char[] styledTextArray = styledText.toCharArray();
    String currentCharString = "";
    Style styleToAssign = mapKeyWords.get("");
    setStyle(0,  styledText.length(), blackFontStyle);
    for(char aChar : styledTextArray){      
      currentCharString = Character.toString(aChar);
      switch(aChar){
        case ' ':  //espacio
        case '\n': //enter
        case '\r': //retorno de carro
        case '\t': //tabulacion
          if(currentWord.length()!=0){
            styleToAssign = mapKeyWords.get(currentWord); 
            if(styleToAssign != null){
              setStyle( currentPosition-currentWord.length(),  currentPosition, styleToAssign);            
              setStyle(currentPosition+1,  currentPosition+1, blackFontStyle);
            }
          }
          currentWord = "";        
          break; 
        default:
          currentWord=currentWord.concat(currentCharString);
      }     
      currentPosition++;
    }          
    setSectionStyle("'","'",redDarkFontStyle);   //si aparece '' pintar rojo    
    setSectionStyle("¨","¨",magentaFontStyle);
    setSectionStyle("´","´",redFontStyle);
    setSectionStyle("`","`",blueFontStyle);
    setSectionStyle("~","~",greenFontStyle);    
    setSectionStyle("/*","*/",grayFontStyle);//si aparece -- pintar gris    
    setSectionStyle("--","\n",grayFontStyle);//si aparece -- pintar gris
    setHighlightedStyle();                   //Resaltar en amarillo palabra buscada    
    aTextPane.repaint();                     //repintar el cuadro de texto
  }
}
