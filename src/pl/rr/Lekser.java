package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Lekser {

    private int nextCharNumber = 0;
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

    public int getNextCharNumber() {
        return nextCharNumber;
    }

    public int lekser() {

        while (true) {
            if (isLastCharacter())
                return endOfXmlFileSymbol();

            getChar();

            //if space or tab, skip
            if (isSpaceOrTabCharacter())
                ;

            //if < and ...
            else if (isLessThanCharacter()) {

                //... ?xml, return XML_START
                if (isStartXMLDeclaration())
                    return startOfXmlDeclarationSymbol();

                //... the beginning of a comment, skip until find --> string
                else if (isStartOfComment())
                    skipComment();

                //... any string, return LESSTHAN_ID
                else if (isNextCharacterOfALetter())
                    return startOfTagWithTagNameSymbol();

                //... / and any string, return LESSTHAN_SLASH_ID
                else if (isNextCharacterOfASlashCharacter())
                    return startOfEndOfTagWithTagNameSymbol();

            //if ? and >, return XML_END
            } else if (isQuestionMark() && isNextCharacterOfAGreaterThanCharacter()) {
                return endOfXmlDeclarationSymbol();

            //if >, return GREATERTHAN
            } else if (isGreaterThanCharacter()) {
                return endOfTagSymbol();

            //if / return SLASH
            } else if (isSlashCharacter()) {
                return slashSymbol();

            //if =, return EQ
            } else if (isEqualsCharacter()) {
                return equalSymbol();

            //if " and any string and ", return ATTR_VALUE
            } else if (isDoubleQuotes()) {
                return valueOfAttrSymbol();

            //if I'm inside tag declaration and character is letter, return ATTR_NAME (name of attribute)
            } else if (isLetter() && !insideTag) {
                return attrSymbol();

            //if I'm inside tag, return ANY_STRING
            } else if (!isLessThanCharacter() && insideTag) {
                return xmlElementSymbol();

            }
        }
    }

    private void getChar() {
        currentCharacter = xml.charAt(nextCharNumber);
        nextCharNumber++;
    }

    private int xmlElementSymbol() {
        insideTag = false;
        while (!isLessThanCharacter() || isWhitespace()) {
            if (nextCharNumber < xml.length()) {
                getChar();
            }
        }
        nextCharNumber--;
        return Constant.ANY_STRING;
    }

    private int attrSymbol() {
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            if (nextCharNumber < xml.length()) {
                getChar();
            }
        }
        nextCharNumber--;
        return Constant.ATTR_NAME;
    }

    private int valueOfAttrSymbol() {
        getChar();
        while (!isDoubleQuotes()) {
            if (isLessThanCharacter()) {
                return -1;
            }

            getChar();
        }

        return Constant.ATTR_VALUE;
    }

    private int equalSymbol() {
        return Constant.EQ;
    }

    private int slashSymbol() {
        if (isNextCharacterOfAGreaterThanCharacter()) {
            tagId.remove(tagId.size() - 1);
        }
        return Constant.SLASH;
    }

    private int endOfTagSymbol() {
        if (nextCharNumber < xml.length()) {
            if (!isNextCharacterOfALessThanCharacter())
                insideTag = true;
        }
        return Constant.GREATERTHAN;
    }

    private int endOfXmlDeclarationSymbol() {
        nextCharNumber++;
        return Constant.XML_END;
    }

    private int startOfEndOfTagWithTagNameSymbol() {
        nextCharNumber++;
        getChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            getChar();
        }

        if (isOpeningTagEqualsToClosingTag()) {
            System.err.println("Tag zamykaj¹cy: <"
                    + strBld.toString() +
                    "> nie zgadza siê z otwieraj¹cym: <"
                    + tagId.get(tagId.size() - 1) + ">");
            return -1;
        } else {
            tagId.remove(tagId.size() - 1);
        }

        nextCharNumber--;
        return Constant.LESSTHAN_SLASH_ID;
    }


    private int endOfXmlFileSymbol() {
        return Constant.END;
    }


    private int startOfTagWithTagNameSymbol() {
        getChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            getChar();
        }

        tagId.add(strBld.toString());
        nextCharNumber--;

        return Constant.LESSTHAN_ID;
    }


    private void skipComment() {
        nextCharNumber = nextCharNumber + 3;
        getChar();
        while (!isEndOfComment()) {
            getChar();
        }
        nextCharNumber--;

        nextCharNumber = nextCharNumber + 4;
    }

    private int startOfXmlDeclarationSymbol() {
        nextCharNumber = nextCharNumber + 4;
        return Constant.XML_START;
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
        return xml.charAt(nextCharNumber) == '<';
    }

    private boolean isGreaterThanCharacter() {
        return currentCharacter == '>';
    }

    private boolean isNextCharacterOfAGreaterThanCharacter() {
        return xml.charAt(nextCharNumber) == '>';
    }

    private boolean isQuestionMark() {
        return currentCharacter == '?';
    }

    private boolean isOpeningTagEqualsToClosingTag() {
        return tagId.get(tagId.size() - 1).compareTo(strBld.toString()) != 0;
    }

     private boolean isNextCharacterOfASlashCharacter() {
        return xml.charAt(nextCharNumber) == '/';
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
        return Character.isLetter(xml.charAt(nextCharNumber));
    }

    private boolean isEndOfComment() {
        return isXmlStrEqualsSpecialStr("-->");
    }

    private boolean isStartOfComment() {
        return isXmlStrEqualsSpecialStr("!--");
    }

    private boolean isStartXMLDeclaration() {
        return isXmlStrEqualsSpecialStr("?xml");
    }

    private boolean isLessThanCharacter() {
        return currentCharacter == '<';
    }

    private boolean isSpaceOrTabCharacter() {
        return currentCharacter == ' ' || currentCharacter == '\t';
    }

    private boolean isLastCharacter() {
        if (nextCharNumber == xml.length()) {
            return true;
        }
        return false;
    }


    private boolean isXmlStrEqualsSpecialStr(String tab) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length(); i++) {
            sb.append(xml.charAt(nextCharNumber + i));
        }

        if (sb.toString().compareTo(tab) == 0)
            return true;

        return false;

    }
}

