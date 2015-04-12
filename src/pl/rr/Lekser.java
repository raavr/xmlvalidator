package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Lekser {

    private int nextCharIndex = 0;
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

    public int getNextCharIndex() {
        return nextCharIndex;
    }

    public GrammaEnum lekser() {

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
                        return GrammaEnum.ERROR;
                }

                //... any string, return LESSTHAN_ID
                else if (isNextCharacterOfALetter())
                    return startOfTagPlusTagNameSymbol();

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
            currentCharacter = xml.charAt(nextCharIndex);
            nextCharIndex++;
    }

    private boolean isCorrectIndex() {
        if (nextCharIndex < xml.length())
            return true;

        return false;

    }

    private GrammaEnum xmlElementSymbol() {
        insideTag = false;
        while (!isLessThanCharacter() || isWhitespace()) {
            if (!isCorrectIndex())
                return GrammaEnum.ERROR;

            setCurrentChar();
        }
        nextCharIndex--;
        return GrammaEnum.ANY_STRING;
    }

    private GrammaEnum attrSymbol() {
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            if (!isCorrectIndex())
                return GrammaEnum.ERROR;

            setCurrentChar();

        }
        nextCharIndex--;
        return GrammaEnum.ATTR_NAME;
    }

    private GrammaEnum valueOfAttrSymbol() {
        if (!isCorrectIndex())
            return GrammaEnum.ERROR;
        setCurrentChar();

        while (!isDoubleQuotes()) {
            if (isLessThanCharacter() || !isCorrectIndex()) {
                return GrammaEnum.ERROR;
            }
            setCurrentChar();
        }

        return GrammaEnum.ATTR_VALUE;
    }

    private GrammaEnum equalSymbol() {
        return GrammaEnum.EQ;
    }

    private GrammaEnum slashSymbol() {
        if (isNextCharacterOfAGreaterThanCharacter()) {
            if(tagId.size() > 0)
                tagId.remove(tagId.size() - 1);
        }
        return GrammaEnum.SLASH;
    }

    private GrammaEnum endOfTagSymbol() {
        if (isCorrectIndex()) {
            if (!isNextCharacterOfALessThanCharacter())
                insideTag = true;
        }
        return GrammaEnum.GREATERTHAN;
    }

    private GrammaEnum endOfXmlDeclarationSymbol() {
        nextCharIndex++;
        return GrammaEnum.XML_END;
    }

    private GrammaEnum startOfEndOfTagPlusTagNameSymbol() {
        nextCharIndex++;
        if (!isCorrectIndex())
            return GrammaEnum.ERROR;

        setCurrentChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            if (!isCorrectIndex())
                return GrammaEnum.ERROR;

            setCurrentChar();
        }

        if (!isOpeningTagEqualsToClosingTag()) {
            return GrammaEnum.TAG_NOT_EQUALS;
        } else {
            tagId.remove(tagId.size() - 1);
        }

        nextCharIndex--;
        return GrammaEnum.LESSTHAN_SLASH_ID;
    }


    private GrammaEnum endOfXmlFileSymbol() {
        return GrammaEnum.END;
    }


    private GrammaEnum startOfTagPlusTagNameSymbol() {
        if (!isCorrectIndex())
            return GrammaEnum.ERROR;

        setCurrentChar();

        strBld.setLength(0);
        while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
            strBld.append(currentCharacter);
            if (!isCorrectIndex())
                return GrammaEnum.ERROR;

            setCurrentChar();
        }

        tagId.add(strBld.toString());
        nextCharIndex--;

        return GrammaEnum.LESSTHAN_ID;
    }


    private int skipComment() {
        nextCharIndex = nextCharIndex + 3;
        if (!isCorrectIndex())
            return -1;

        setCurrentChar();

        while (!isEndOfComment()) {
            if (!isCorrectIndex())
                return -1;

            setCurrentChar();
        }
        nextCharIndex = nextCharIndex + 3;

        return 0;
    }

    private GrammaEnum startOfXmlDeclarationSymbol() {
        nextCharIndex = nextCharIndex + 4;
        return GrammaEnum.XML_START;
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
            return xml.charAt(nextCharIndex) == '<';

        return false;
    }

    private boolean isGreaterThanCharacter() {
        return currentCharacter == '>';
    }

    private boolean isNextCharacterOfAGreaterThanCharacter() {
        if(isCorrectIndex())
            return xml.charAt(nextCharIndex) == '>';

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
            return xml.charAt(nextCharIndex) == '/';

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
            return Character.isLetter(xml.charAt(nextCharIndex));

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
        if (nextCharIndex == xml.length()) {
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
        if(nextCharIndex + tab.length() > xml.length())
            return false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length(); i++) {
            sb.append(xml.charAt(nextCharIndex + i));
        }

        if (sb.toString().compareTo(tab) == 0)
            return true;

        return false;

    }

}

