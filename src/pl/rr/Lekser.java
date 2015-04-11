package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Lekser {

    private int char_num = 0;
    private ArrayList<String> tagId;
    private StringBuilder strBld;
    private boolean insideTag = false;
    private String xml;
    private char currentCharacter;

    public Lekser(String xml) {
        tagId = new ArrayList<String>();
        strBld = new StringBuilder();
        this.xml = xml;
    }

    public int getChar_num() {
        return char_num;
    }

    public int lekser() {

        while (true) {
            if (isLastCharacter())
                return endOfXmlFileSymbol();

            getNextChar();
            char_num++;

            //if space or tab, skip
            if (isSpaceOrTabCharacter())
                ;

            //if < and ...
            else if (isLessThenCharacter()) {

                //... ?xml, return XML_ST
                if (isStartXMLDeclaration())
                    return startOfXmlDeclarationSymbol();

                //... the beginning of a comment, skip until find --> string
                else if (isStartOfComment())
                    skipComment();

                //... any string, return OTW_ID
                else if (isNextCharacterOfALetter())
                    return startOfTagWithTagNameSymbol();

                //... / and any string, return OTW_SL_ID
                else if (isNextCharacterOfASlashCharacter())
                    return startOfEndOfTagWithTagNameSymbol();

            //if ? and >, return XML_END
            } else if (isQuestionMark() && isNextCharacterOfAGreaterThanCharacter()) {
                return endOfXmlDeclarationSymbol();

            //if >, return ZAM
            } else if (isGreaterThanCharacter()) {
                return endOfTagSymbol();

            //if / return UKOS
            } else if (isSlashCharacter()) {
                return slashSymbol();

            //if =, return ROW
            } else if (isEqualsCharacter()) {
                return equalSymbol();

            //if " and any string and ", return WAR_ATR
            } else if (isDoubleQuotes()) {
                return valueOfAttrSymbol();

            //if I'm inside tag declaration and character is letter, return NAPIS_SPECJALNY (name of attribute)
            } else if (isLetter() && !insideTag) {
                return attrSymbol();

            //if I'm inside tag, return NAPIS_DOWOLNY
            } else if (!isLessThenCharacter() && insideTag) {
                return xmlElementSymbol();

            }
        }
    }

    private int xmlElementSymbol() {
        insideTag = false;
        while (!isLessThenCharacter() || isWhitespace()) {
            if (char_num < xml.length()) {
                getNextChar();
                char_num++;
            }
        }
        char_num--;
        return Const.NAPIS_DOWOLNY;
    }

    private int attrSymbol() {
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            if (char_num < xml.length()) {
                getNextChar();
                char_num++;
            }
        }
        char_num--;
        return Const.NAPIS_SPECJALNY;
    }

    private int valueOfAttrSymbol() {
        getNextChar();
        while (!isDoubleQuotes()) {
            if (isLessThenCharacter()) {
                return 1;
            }

            getNextCharIfPossible();
        }

        char_num++;
        return Const.WAR_ATR;
    }

    private int equalSymbol() {
        return Const.ROW;
    }

    private int slashSymbol() {
        if (isNextCharacterOfAGreaterThanCharacter()) {
            tagId.remove(tagId.size() - 1);
        }
        return Const.UKOS;
    }

    private int endOfTagSymbol() {
        if (char_num < xml.length()) {
            if (!isNextCharacterOfALessThanCharacter())
                insideTag = true;
        }
        return Const.ZAM;
    }

    private int endOfXmlDeclarationSymbol() {
        char_num = char_num + 1;
        return Const.XML_END;
    }

    private int startOfEndOfTagWithTagNameSymbol() {
        char_num++;
        getNextChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            getNextCharIfPossible();
        }

        if (isOpeningTagEqualsToClosingTag()) { //sprawdz tag otwierajacy z zamykajacym
            System.err.println("Tag zamykaj¹cy: <" + strBld.toString() + "> nie zgadza siê z otwieraj¹cym: <" + tagId.get(tagId.size() - 1) + ">");
            return 1;
        } else {
            tagId.remove(tagId.size() - 1);
        }

        return Const.OTW_SL_ID;
    }

    private int endOfXmlFileSymbol() {
        return Const.KONIEC;
    }

    private int startOfTagWithTagNameSymbol() {
        getNextChar();
        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            getNextCharIfPossible();
        }
        tagId.add(strBld.toString()); //dodaje ID tagu do listy

        return Const.OTW_ID;
    }

    private void skipComment() {
        char_num = char_num + 3;
        getNextChar();
        while (!isEndOfComment()) {
            getNextCharIfPossible();
        }
        char_num = char_num + 4;
    }

    private int startOfXmlDeclarationSymbol() {
        char_num = char_num + 4;
        return Const.XML_ST;
    }

    private boolean isWhitespace() {
        return Character.isWhitespace(currentCharacter);
    }

    private boolean isLetter() {
        return Character.isLetter(currentCharacter);
    }

    private boolean isDoubleQuotes() {
        return currentCharacter == '"';
    }

    private boolean isEqualsCharacter() {
        return currentCharacter == '=';
    }

    private boolean isSlashCharacter() {
        return currentCharacter == '/';
    }

    private boolean isNextCharacterOfALessThanCharacter() {
        return xml.charAt(char_num) == '<';
    }

    private boolean isGreaterThanCharacter() {
        return currentCharacter == '>';
    }

    private boolean isNextCharacterOfAGreaterThanCharacter() {
        return xml.charAt(char_num) == '>';
    }

    private boolean isQuestionMark() {
        return currentCharacter == '?';
    }

    private boolean isOpeningTagEqualsToClosingTag() {
        return tagId.get(tagId.size() - 1).compareTo(strBld.toString()) != 0;
    }

    private void getNextCharIfPossible() {
        if (char_num < xml.length()) {
            char_num++;
            getNextChar();

        }
    }

    private boolean isNextCharacterOfASlashCharacter() {
        return xml.charAt(char_num) == '/';
    }

    private boolean isHyphenCharacter() {
        return currentCharacter == '-';
    }

    private boolean isUnderscoreCharacter() {
        return currentCharacter == '_';
    }

    private boolean isDotCharacter() {
        return currentCharacter == '.';
    }

    private boolean isLetterOrDigit() {
        return Character.isLetterOrDigit(currentCharacter);
    }

    private boolean isNextCharacterOfALetter() {
        return Character.isLetter(xml.charAt(char_num));
    }

    private boolean isEndOfComment() {
        return sprawdzCiag(xml, char_num, "-->");
    }

    private boolean isStartOfComment() {
        return sprawdzCiag(xml, char_num, "!--");
    }

    private boolean isStartXMLDeclaration() {
        return sprawdzCiag(xml, char_num, "?xml");
    }

    private boolean isLessThenCharacter() {
        return currentCharacter == '<';
    }

    private boolean isSpaceOrTabCharacter() {
        return currentCharacter == ' ' || currentCharacter == '\t';
    }

    private void getNextChar() {
        currentCharacter = xml.charAt(char_num); //pobierz znak z xmla
    }

    private boolean isLastCharacter() {
        if (char_num == xml.length()) {
            return true;
        }
        return false;
    }


    private boolean sprawdzCiag(String xml, int cn, String tab) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length(); i++) {
            sb.append(xml.charAt(cn + i));
        }

        if (sb.toString().compareTo(tab) == 0) {
            return true;
        } else {
            return false;
        }
    }
}

