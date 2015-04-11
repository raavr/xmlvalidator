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

    private ArrayList<Integer> listind;
    private int biezacy_leks;
    private Lekser lekser;


    public Parser(String file, ArrayList list) {
        listind = new ArrayList<Integer>(list);
        lekser = new Lekser(file);
    }

    public void wypisz() {
        System.err.println("Blad skladni " + Constant.hm.get(biezacy_leks));
    }

    //do sygnalizacji bledow - zwraca indeks blednego znaku z oryginalnego pliku XML
    private int zwrocIndeks() {
        int i = 1;
        for(int j : listind)
            if (j > lekser.getNextCharNumber())
                if(listind.get(1) > lekser.getNextCharNumber())
                    return i-1;
                else
                    return i;
            else if (j == lekser.getNextCharNumber())
                return i+1;
            else i++;
        return -1;
    }

    public void parser() {
        System.out.println("...Zaczynam parsowaæ plik XML...");
        biezacy_leks = 0;
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

    //atrybuty ---> atrybut atrybuty | ?
    private void atrybuty() {
        switch (biezacy_leks) {
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
        switch (biezacy_leks) {
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
        switch (biezacy_leks) {

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
        switch (biezacy_leks) {
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
        switch (biezacy_leks) {
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



    private void pobierzLeksem(int s) {
        if (biezacy_leks == s) {
            biezacy_leks = lekser.lekser();
        } else {
            System.err.println("Blad skladni " + Constant.hm.get(s) + " linia: " + (zwrocIndeks()-1) + " numer " + (lekser.getNextCharNumber() - listind.get(zwrocIndeks()-2)));
            System.exit(0);

        }
    }


}
