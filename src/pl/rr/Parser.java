package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */

/* GRAMMA

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
    private int currentLeks;
    private Lekser lekser;


    public Parser(String file, ArrayList list) {
        newLinesIdxList = new ArrayList<Integer>(list);
        lekser = new Lekser(file);
    }

    private int getLineNumber() {
        int lineNumber = 1;
        int indexOfError = lekser.getNextCharNumber();

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
        int indexOfError = lekser.getNextCharNumber();

        if(newLinesIdxList.size() == 1)
            return (indexOfError - newLinesIdxList.get(0) + 1);

        for(int i = 1; i < newLinesIdxList.size(); i++)
            if(newLinesIdxList.get(i) > indexOfError)
                return indexOfError - newLinesIdxList.get(i - 1) + 1;

        return 0;
    }


    public void parser() {
        System.out.println("...Zaczynam parsować plik XML...");
        currentLeks = 0;
        pobierzLeksem(0);

        //s -> XML_START atrybuty XML_END tag_root END
        pobierzLeksem(Constant.XML_START);
        atrybuty();
        pobierzLeksem(Constant.XML_END);
        tag_root();
        pobierzLeksem(Constant.END);
        System.out.println("==================");
        System.out.println("XML jest poprawny.");
        System.out.println("==================");

    }

    private void pobierzLeksem(int s) {
        if (currentLeks == s) {
            currentLeks = lekser.lekser();
        } else {
            if(currentLeks == Constant.TAG_NOT_EQUALS)
                System.err.println("Tag zamykający: " + lekser.getClosingTag() + " nie zgadza się z otwierającym: " + lekser.getOpeningTag());
            System.err.println("Błąd składni " + Constant.hm.get(s) + " linia: " + getLineNumber() + " numer " + getNumberAtLine());
            System.exit(0);

        }
    }

    //atrybuty ---> atrybut atrybuty | ?
    private void atrybuty() {
        switch (currentLeks) {
            case Constant.ATTR_NAME:
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
            case Constant.ATTR_NAME:
                pobierzLeksem(Constant.ATTR_NAME);
                pobierzLeksem(Constant.EQ);
                pobierzLeksem(Constant.ATTR_VALUE);
                break;
            default:
                break;
        }
    }

    //tag_root ---> LESSTHAN_ID atrybuty GREATERTHAN tag LESSTHAN_SLASH_ID GREATERTHAN
    private void tag_root() {
        switch (currentLeks) {

            case Constant.LESSTHAN_ID:
                pobierzLeksem(Constant.LESSTHAN_ID);
                atrybuty();
                pobierzLeksem(Constant.GREATERTHAN);
                tag();
                pobierzLeksem(Constant.LESSTHAN_SLASH_ID);
                pobierzLeksem(Constant.GREATERTHAN);
                break;
            default:
                System.err.println("Blad skladni tag_root");
        }
    }
    //tag ---> LESSTHAN_ID atrybuty tag_kont | ANY_STRING tag | ?
    private void tag() {
        switch (currentLeks) {
            case Constant.LESSTHAN_ID:
                pobierzLeksem(Constant.LESSTHAN_ID);
                atrybuty();
                tag_kont();
                break;
            case Constant.ANY_STRING:
                pobierzLeksem(Constant.ANY_STRING);
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
            case Constant.SLASH:
                pobierzLeksem(Constant.SLASH);
                pobierzLeksem(Constant.GREATERTHAN);
                tag();
                break;
            case Constant.GREATERTHAN:
                pobierzLeksem(Constant.GREATERTHAN);
                tag();
                pobierzLeksem(Constant.LESSTHAN_SLASH_ID);
                pobierzLeksem(Constant.GREATERTHAN);
                tag();
                break;
            default:
                System.err.println("Blad skladni tag_kont");

        }
    }
}
