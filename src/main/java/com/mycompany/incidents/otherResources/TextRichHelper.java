package com.mycompany.incidents.otherResources;

import java.awt.Color;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextRichHelper {

	HashMap<String, SimpleAttributeSet> mapKeyWords = new HashMap<>();
	SimpleAttributeSet blackFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet redFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet redDarkFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet greenFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet magentaFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet blueFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet blueFontStyle1 = new SimpleAttributeSet();
	SimpleAttributeSet blueFontStyle2 = new SimpleAttributeSet();
	SimpleAttributeSet blueFontStyle3 = new SimpleAttributeSet();
	SimpleAttributeSet blueFontStyle4 = new SimpleAttributeSet();
	SimpleAttributeSet grayFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet blueLigthFontStyle = new SimpleAttributeSet();
	SimpleAttributeSet yellowHighlightedStyle = new SimpleAttributeSet();
	SimpleAttributeSet greenHighlightedStyle = new SimpleAttributeSet();
	JTextPane textPane = new JTextPane();
	AbstractDocument doc;
	DefaultStyledDocument dsd;
	ArrayList<SectionStyle> SectionsStyles = new ArrayList<>();
	char currentSpecialChar = 0;
	String currentWord = "";
	public String searchedText = "";
	String textOfDocument = "";

	public TextRichHelper(JTextPane aTextPane) {
		this.textPane = aTextPane;
		textPane.setMargin(new Insets(5, 5, 5, 5));
		configureStyles();
		configureKeyWords();
		doc = (AbstractDocument) textPane.getStyledDocument();
		//dsd = new DefaultStyledDocument(textPane.getStyledDocument().);
		configureStyles();
		textPane.setCaretPosition(0);
	}

	protected SimpleAttributeSet[] initAttributes(int length) {
		SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];
		attrs[0] = redDarkFontStyle;
		attrs[1] = redDarkFontStyle;
		attrs[2] = blueFontStyle1;
		attrs[3] = magentaFontStyle;
		attrs[4] = greenFontStyle;
		attrs[5] = yellowHighlightedStyle;
		return attrs;
	}

	private void configureKeyWords() {
		mapKeyWords.put("ERROR", blueFontStyle1);
		mapKeyWords.put("SOLUC", blueFontStyle2);
		mapKeyWords.put("CAUSA", blueFontStyle3);
		mapKeyWords.put("COLA_SALIDA", blueFontStyle4);
		mapKeyWords.put("COLA_ENTRADA", blueFontStyle4);
		mapKeyWords.put("DESCRIPCION", blueFontStyle4);
		mapKeyWords.put("SCRIPT", blueFontStyle4);
	}

	private void configureFontStyle(SimpleAttributeSet aStyle, Color aColor, boolean isBold, int fontSize) {
		StyleConstants.setForeground(aStyle, aColor);
		StyleConstants.setBold(aStyle, isBold);
	}

	private void configureStyles() {
		try {
			configureFontStyle(redFontStyle, Color.red, true, 20);
			configureFontStyle(blueFontStyle, Color.blue, true, 20);
			configureFontStyle(greenFontStyle, new Color(0, 150, 0), true, 20);
			configureFontStyle(magentaFontStyle, new Color(179, 0, 179), true, 20);
			configureFontStyle(redDarkFontStyle, new Color(201, 60, 32), true, 20);
			configureFontStyle(blueFontStyle1, new Color(31, 72, 186), true, 20);
			configureFontStyle(blueFontStyle2, new Color(74, 170, 192), true, 20);
			configureFontStyle(blueFontStyle3, new Color(1, 113, 205), true, 20);
			configureFontStyle(blueFontStyle4, new Color(86, 116, 255), true, 20);
			configureFontStyle(blueLigthFontStyle, Color.blue, true, 20);
			configureFontStyle(blackFontStyle, Color.black, false, 20);
			configureFontStyle(grayFontStyle, new Color(150, 150, 150), false, 20);
			StyleConstants.setBackground(yellowHighlightedStyle, new Color(255, 200, 0));
			StyleConstants.setBackground(greenHighlightedStyle, new Color(144, 226, 89));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String normalizeText(String text) {
		return text.toLowerCase().replaceAll("á", "a").replaceAll("é", "e")
						.replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "a");
	}

	private void setHighlightedStyle() throws BadLocationException {
		if (searchedText.length() > 1) {
			String normalizeText = normalizeText(textOfDocument);
			String normalizeSearch = normalizeText(searchedText);
			int position = normalizeText.indexOf(normalizeSearch, 0);
			while (position != -1) {
				String originalSearchedText = textOfDocument.substring(position, position + searchedText.length());				
				doc.replace(position, searchedText.length(), originalSearchedText, yellowHighlightedStyle);				
				position = normalizeText.indexOf(normalizeSearch, position + searchedText.length());
			}
		}
	}

	private void createSectionStyle(char evaluatedChar, SimpleAttributeSet anStyle) {
		if (currentSpecialChar == 0) {
			currentSpecialChar = evaluatedChar;
			return;
		}
		if (currentSpecialChar == evaluatedChar) {
			SectionsStyles.add(new SectionStyle(currentWord, anStyle));
			currentSpecialChar = 0;
			currentWord = "";
		}
	}
	
	public void applyStyles() throws BadLocationException {
		SectionsStyles = new ArrayList<>();
		textOfDocument = "";		
		textOfDocument = textPane.getStyledDocument().getText(0, textPane.getStyledDocument().getLength());		
		int aCaretPosition = textPane.getCaretPosition();
		textPane.setText("");
		int currentPosition = 0;
		currentWord = "";
		char[] textOfDocumentChars = textOfDocument.toCharArray();
		String aCharStr;

		currentSpecialChar = 0;
		for (char aChar : textOfDocumentChars) {
			aCharStr = Character.toString(aChar);
			currentWord = currentWord.concat(aCharStr);
			createSectionsStyles(aChar);
			currentPosition++;
			if (textOfDocument.length() == currentPosition) {//Quedo texto pendiente por analizar
				createLastSectionStyle();
			}
		}
		for (SectionStyle aLine : SectionsStyles) {//adicionar contenido al textPane			
			doc.insertString(doc.getLength(), aLine.getValue(), aLine.getStyle());			
		}
		setHighlightedStyle();                   //Resaltar en amarillo palabra buscada    		
		textPane.setCaretPosition(aCaretPosition);
	}
	
	public void createSectionsStyles(char aChar) {
		SimpleAttributeSet styleToAssign;
		switch (aChar) {
			case '~':
				createSectionStyle(aChar, greenFontStyle);
				break;
			case '¨':
				createSectionStyle(aChar, magentaFontStyle);
				break;
			case '`':
				createSectionStyle(aChar, blueFontStyle);
				break;
			case '\'':
				createSectionStyle(aChar, redDarkFontStyle);
				break;
			case '´':
				createSectionStyle(aChar, redFontStyle);
				break;
			case '°':
				createSectionStyle(aChar, grayFontStyle);
				break;
			case ' ':  //espacio
			case '\n': //enter
			case '\r': //retorno de carro
			case '\t': //tabulacion
				if (currentSpecialChar != 0) {
					break;
				}
				if (currentWord.length() != 0) {
					styleToAssign = mapKeyWords.get(currentWord.substring(0, currentWord.length() - 1));
					if (styleToAssign != null) {
						SectionsStyles.add(new SectionStyle(currentWord, styleToAssign));
					} else {
						SectionsStyles.add(new SectionStyle(currentWord, blackFontStyle));
					}
				}
				currentWord = "";
				break;
		}
	}

	public void createLastSectionStyle() {
		switch (currentSpecialChar) {
			case '~':
				SectionsStyles.add(new SectionStyle(currentWord, greenFontStyle));
				break;
			case '¨':
				SectionsStyles.add(new SectionStyle(currentWord, magentaFontStyle));
				break;
			case '`':
				SectionsStyles.add(new SectionStyle(currentWord, blueFontStyle));
				break;
			case '\'':
				SectionsStyles.add(new SectionStyle(currentWord, redDarkFontStyle));
				break;
			case '´':
				SectionsStyles.add(new SectionStyle(currentWord, redFontStyle));
				break;
			case '°':
				SectionsStyles.add(new SectionStyle(currentWord, grayFontStyle));
				break;
			default:
				SectionsStyles.add(new SectionStyle(currentWord, blackFontStyle));
				break;
		}
	}


}
