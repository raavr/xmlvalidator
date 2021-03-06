package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */

/* Gramma

    S ---> XML_START atrybuty XML_END tag_root END
    tag_root ---> LESSTHAN_ID atrybuty GREATERTHAN tag LESSTHAN_SLASH_ID GREATERTHAN
    tag ---> LESSTHAN_ID atrybuty tag_kont | ANY_STRING tag | ?
    tag_kont ---> SLASH GREATERTHAN tag
    tag_kont ---> GREATERTHAN tag LESSTHAN_SLASH_ID GREATERTHAN tag
    atrybuty ---> atrybut atrybuty | ?
    atrybut ---> ATTR_NAME EQ ATTR_VALUE | ?

 */
public class Parser {

    private ArrayList<Integer> newLinesIdxList;
    private Gramma currentLeks;
    private Lekser lekser;

    public Parser(String file, ArrayList list) {
        newLinesIdxList = new ArrayList<Integer>(list);
        lekser = new Lekser(file);
    }

    private int getLineNumber() {
        int lineNumber = 1;
        int indexOfError = lekser.getNextCharIndex();

        if(newLinesIdxList.size() <= 1)
            return lineNumber;

        for(int i = 1; i < newLinesIdxList.size(); i++)
            if(newLinesIdxList.get(i) < indexOfError)
                lineNumber++;
            else
               return lineNumber;


        return lineNumber;
    }

    private int getNumberAtLine() {
        int indexOfError = lekser.getNextCharIndex();

        if(newLinesIdxList.size() == 1)
            return (indexOfError - newLinesIdxList.get(0) + 1);

        for(int i = 1; i < newLinesIdxList.size(); i++)
            if(newLinesIdxList.get(i) > indexOfError)
                return indexOfError - newLinesIdxList.get(i - 1) + 1;

        return 0;
    }


    public void parser() {
        System.out.println("...Zaczynam parsować plik XML...");
        pobierzLeksem(currentLeks);

        //s -> XML_START atrybuty XML_END tag_root END
        pobierzLeksem(Gramma.XML_START);
        atrybuty();
        pobierzLeksem(Gramma.XML_END);
        tag_root();
        pobierzLeksem(Gramma.END);
        System.out.println("==================");
        System.out.println("XML jest poprawny.");
        System.out.println("==================");

    }

    private void pobierzLeksem(Gramma s) {
        if (currentLeks == s) {
            currentLeks = lekser.lekser();
        } else {
            if(currentLeks == Gramma.TAG_NOT_EQUALS)
                System.err.println("Tag zamykający: " + lekser.getClosingTag() + " nie zgadza się z otwierającym: " + lekser.getOpeningTag());
            System.err.println("Błąd składni " + ErrorMsgs.hm.get(s) + " linia: " + getLineNumber() + " numer " + getNumberAtLine());
            System.exit(0);

        }
    }

    //atrybuty ---> atrybut atrybuty | ?
    private void atrybuty() {
        switch (currentLeks) {
            case ATTR_NAME:
                atrybut();
                atrybuty();
                break;
            default:
                break;
        }
    }

    //atrybut ---> NAPIS_SECJALNY EQ ATTR_VALUE | ?
    private void atrybut() {
        switch (currentLeks) {
            case ATTR_NAME:
                pobierzLeksem(Gramma.ATTR_NAME);
                pobierzLeksem(Gramma.EQ);
                pobierzLeksem(Gramma.ATTR_VALUE);
                break;
            default:
                break;
        }
    }

    //tag_root ---> LESSTHAN_ID atrybuty GREATERTHAN tag LESSTHAN_SLASH_ID GREATERTHAN
    private void tag_root() {
        switch (currentLeks) {

            case LESSTHAN_ID:
                pobierzLeksem(Gramma.LESSTHAN_ID);
                atrybuty();
                pobierzLeksem(Gramma.GREATERTHAN);
                tag();
                pobierzLeksem(Gramma.LESSTHAN_SLASH_ID);
                pobierzLeksem(Gramma.GREATERTHAN);
                break;
            default:
                System.err.println("Blad skladni tag_root");
        }
    }
    //tag ---> LESSTHAN_ID atrybuty tag_kont | ANY_STRING tag | ?
    private void tag() {
        switch (currentLeks) {
            case LESSTHAN_ID:
                pobierzLeksem(Gramma.LESSTHAN_ID);
                atrybuty();
                tag_kont();
                break;
            case ANY_STRING:
                pobierzLeksem(Gramma.ANY_STRING);
                tag();
                break;
            default:
                break;
        }
    }

    //tag_kont ---> SLASH GREATERTHAN tag
    //tag_kont ---> GREATERTHAN tag LESSTHAN_SLASH_ID GREATERTHAN tag
    private void tag_kont() {
        switch (currentLeks) {
            case SLASH:
                pobierzLeksem(Gramma.SLASH);
                pobierzLeksem(Gramma.GREATERTHAN);
                tag();
                break;
            case GREATERTHAN:
                pobierzLeksem(Gramma.GREATERTHAN);
                tag();
                pobierzLeksem(Gramma.LESSTHAN_SLASH_ID);
                pobierzLeksem(Gramma.GREATERTHAN);
                tag();
                break;
            default:
                System.err.println("Blad skladni tag_kont");

        }
    }
}
