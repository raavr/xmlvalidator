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

            setCurrentChar();

            //if space or tab, skip
            if (isSpaceOrTabCharacter())
                ;

            //if < and ...
            else if (isLessThanCharacter()) {

                //... ?xml, return XML_START
                if (isStartXMLDeclaration())
                    return startOfXmlDeclarationSymbol();

                //... the beginning of a comment, skip until find "-->"
                else if (isStartOfComment()) {
                    if(skipComment() == -1)
                        return Constant.ERROR;
                }

                //... any string, return LESSTHAN_ID
                else if (isNextCharacterOfALetter())
                    return startOfTagWithTagNameSymbol();

                //... / and any string, return LESSTHAN_SLASH_ID
                else if (isNextCharacterOfASlashCharacter())
                    return startOfEndOfTagPlusTagNameSymbol();

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

    private void setCurrentChar() {
            currentCharacter = xml.charAt(nextCharNumber);
            nextCharNumber++;
    }

    private boolean isCorrectIndex() {
        if (nextCharNumber < xml.length())
            return true;

        return false;

    }

    private int xmlElementSymbol() {
        insideTag = false;
        while (!isLessThanCharacter() || isWhitespace()) {
            if (!isCorrectIndex())
                return Constant.ERROR;

            setCurrentChar();
        }
        nextCharNumber--;
        return Constant.ANY_STRING;
    }

    private int attrSymbol() {
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            if (!isCorrectIndex())
                return Constant.ERROR;

            setCurrentChar();

        }
        nextCharNumber--;
        return Constant.ATTR_NAME;
    }

    private int valueOfAttrSymbol() {
        if (!isCorrectIndex())
            return Constant.ERROR;
        setCurrentChar();

        while (!isDoubleQuotes()) {
            if (isLessThanCharacter() || !isCorrectIndex()) {
                return Constant.ERROR;
            }
            setCurrentChar();
        }

        return Constant.ATTR_VALUE;
    }

    private int equalSymbol() {
        return Constant.EQ;
    }

    private int slashSymbol() {
        if (isNextCharacterOfAGreaterThanCharacter()) {
            if(tagId.size() > 0)
                tagId.remove(tagId.size() - 1);
        }
        return Constant.SLASH;
    }

    private int endOfTagSymbol() {
        if (isCorrectIndex()) {
            if (!isNextCharacterOfALessThanCharacter())
                insideTag = true;
        }
        return Constant.GREATERTHAN;
    }

    private int endOfXmlDeclarationSymbol() {
        nextCharNumber++;
        return Constant.XML_END;
    }

    private int startOfEndOfTagPlusTagNameSymbol() {
        nextCharNumber++;
        if (!isCorrectIndex())
            return Constant.ERROR;

        setCurrentChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            if (!isCorrectIndex())
                return Constant.ERROR;

            setCurrentChar();
        }

        if (!isOpeningTagEqualsToClosingTag()) {
            return Constant.TAG_NOT_EQUALS;
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
        if (!isCorrectIndex())
            return Constant.ERROR;

        setCurrentChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            if (!isCorrectIndex())
                return Constant.ERROR;

            setCurrentChar();
        }

        tagId.add(strBld.toString());
        nextCharNumber--;

        return Constant.LESSTHAN_ID;
    }


    private int skipComment() {
        nextCharNumber = nextCharNumber + 3;
        if (!isCorrectIndex())
            return Constant.ERROR;

        setCurrentChar();

        while (!isEndOfComment()) {
            if (!isCorrectIndex())
                return Constant.ERROR;

            setCurrentChar();
        }
        nextCharNumber = nextCharNumber + 3;

        return 0;
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
        if(isCorrectIndex())
            return xml.charAt(nextCharNumber) == '<';

        return false;
    }

    private boolean isGreaterThanCharacter() {
        return currentCharacter == '>';
    }

    private boolean isNextCharacterOfAGreaterThanCharacter() {
        if(isCorrectIndex())
            return xml.charAt(nextCharNumber) == '>';

        return false;
    }

    private boolean isQuestionMark() {
        return currentCharacter == '?';
    }

    private boolean isOpeningTagEqualsToClosingTag() {
        if(tagId.size() > 0)
            return tagId.get(tagId.size() - 1).compareTo(strBld.toString()) == 0;

        return false;
    }

     private boolean isNextCharacterOfASlashCharacter() {
         if(isCorrectIndex())
            return xml.charAt(nextCharNumber) == '/';

         return false;
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
        if(isCorrectIndex())
            return Character.isLetter(xml.charAt(nextCharNumber));

        return false;
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

    public String getOpeningTag() {
        if(tagId.size() > 1)
            return tagId.get(tagId.size()-1);

        return "";
    }

    public String getClosingTag() {
        return strBld.toString();
    }

    private boolean isXmlStrEqualsSpecialStr(String tab) {
        if(nextCharNumber + tab.length() > xml.length())
            return false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length(); i++) {
            sb.append(xml.charAt(nextCharNumber + i));
        }

        if (sb.toString().compareTo(tab) == 0)
            return true;

        return false;

    }

}

