package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Lekser {

    private int char_num = 0;
    private int nrwiersza = 1;
    private ArrayList<String> tagId;
    private StringBuilder strBld;
    private boolean inTag = false;
    private String xml;
    private char t;


    public Lekser(String xml) {
        tagId = new ArrayList<String>();
        strBld = new StringBuilder();
        this.xml = xml;
    }

    public int getChar_num() {
        return char_num;
    }

    public int getNrwiersza() {
        return nrwiersza;
    }

    public int lekser() {


        while (true) {

            if (isLastCharacter()) return Const.KONIEC;
            getNextChar();
            char_num++;

            if (isSpaceOrTabCharacter())
                ;
            else if (isNewLineCharacter()) {
                nrwiersza++;
            } else if (isLessThenCharacter()) {

                if (isStartXMLDeclaration()) { //jesli < i ?xml, zwracam XML_ST
                    char_num = char_num + 4;
                    return Const.XML_ST;

                } else if (isStartOfComment()) { //jesli komentarz - pomijam, dopoki nie znajde -->
                    char_num = char_num + 3;
                    getNextChar();
                    while (!isEndOfComment()) {
                        getNextCharIfPossible();
                    }
                    char_num = char_num + 4;


                } else if (isLetter()) { //jesli < i dowolny ciag znakow, zwracam OTW_ID
                    getNextChar();
                    strBld.setLength(0);
                    while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
                        strBld.append(t);
                        getNextCharIfPossible();
                    }
                    tagId.add(strBld.toString()); //dodaje ID tagu do listy

                    return Const.OTW_ID;

                } else if (isSlashCharacter()) { //jesli < / i dowolny ciag znakow, zwracm OTW_SL_ID
                    char_num++;
                    getNextChar();

                    strBld.setLength(0);
                    while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
                        strBld.append(t);
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

            } else if (isQuestionMark() && isGreaterThanCharacter()) { //jesli ? > zwracam XML_END
                char_num = char_num + 1;
                return Const.XML_END;

            } else if (t == '>') { //jesli > zwrcam ZAM
                if (char_num < xml.length()) {
                    if (!(xml.charAt(char_num) == '<')) {
                        inTag = true;
                        return Const.ZAM;
                    } else {
                        return Const.ZAM;
                    }
                }
                return Const.ZAM;

            } else if (t == '/') { //jesli / zwracam UKOS
                if (isGreaterThanCharacter()) {
                    tagId.remove(tagId.size() - 1);
                }
                return Const.UKOS;

            } else if (t == '=') { //jesli = zwracam ROW
                return Const.ROW;

            } else if (t == '"') { //jesli " pobieram wszystko do " i zwracam WAR_ATR
                getNextChar();
                while (t != '"') {
                    if (isLessThenCharacter()) {
                        return 1;
                    }

                    getNextCharIfPossible();
                }

                char_num++;
                return Const.WAR_ATR;

            } else if (Character.isLetter(t) && inTag == false) { //jesli inTag == false, jestem w nazwie atrybutu, zwracam NAPIS_SPECJALNY

                while (isLetterOrDigit() || isDotCharacter() || isUnderscoreCharacter() || isHyphenCharacter()) {
                    if (char_num < xml.length()) {
                        getNextChar();
                        char_num++;
                    }
                }
                char_num--;
                return Const.NAPIS_SPECJALNY;

            } else if (t != '<' && inTag == true) { //w przeciwnym wypadku zwracam NAPIS_DOWOLNY, ktory dopuszcza biale znaki
                inTag = false;
                while (t != '<' || Character.isWhitespace(t)) {
                    if (char_num < xml.length()) {
                        getNextChar();
                        char_num++;
                    }
                }
                char_num--;
                return Const.NAPIS_DOWOLNY;

            }
        }
    }

    private boolean isGreaterThanCharacter() {
        return xml.charAt(char_num) == '>';
    }

    private boolean isQuestionMark() {
        return t == '?';
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

    private boolean isSlashCharacter() {
        return xml.charAt(char_num) == '/';
    }

    private boolean isHyphenCharacter() {
        return t == '-';
    }

    private boolean isUnderscoreCharacter() {
        return t == '_';
    }

    private boolean isDotCharacter() {
        return t == '.';
    }

    private boolean isLetterOrDigit() {
        return Character.isLetterOrDigit(t);
    }

    private boolean isLetter() {
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
        return t == '<';
    }

    private boolean isNewLineCharacter() {
        return t == '\n';
    }

    private boolean isSpaceOrTabCharacter() {
        return t == ' ' || t == '\t';
    }

    private void getNextChar() {
        t = xml.charAt(char_num); //pobierz znak z xmla
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

