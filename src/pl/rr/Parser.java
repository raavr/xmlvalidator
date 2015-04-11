package pl.rr;

import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */

/*\
S ---> XML_ST atrybuty XML_END tag_root KONIEC
tag_root ---> OTW_ID atrybuty ZAM tag OTW_SL_ID ZAM
tag ---> OTW_ID atrybuty tag_kont | NAPIS_DOWOLNY tag | ?
tag_kont ---> UKOS ZAM tag
tag_kont ---> ZAM tag OTW_SL_ID ZAM tag
atrybuty ---> atrybut atrybuty | ?
atrybut ---> NAPIS_SECJALNY ROW WAR_ATR | ?
 */
public class Parser {


    private ArrayList<Integer> listind;
    private int biezacy_leks;
    private Lekser lekser;


    public Parser(String file, ArrayList list) {
        Const.wypelnij();
        listind = new ArrayList<Integer>(list);
        lekser = new Lekser(file);
    }

    public void wypisz() {
        System.err.println("blad skladni " + Const.ht.get(biezacy_leks));
    }

    //do sygnalizacji bledow - zwraca indeks blednego znaku z oryginalnego pliku XML
    private int zwrocIndeks() {
        int i = 1;
        for(int j : listind)
            if (j > lekser.getChar_num())
                if(listind.get(1) > lekser.getChar_num())
                    return i-1;
                else
                    return i;
            else if (j == lekser.getChar_num())
                return i+1;
            else i++;
        return -1;
    }

    public void parser() {
        System.out.println("...Zaczynam parsowaæ plik XML...");
        biezacy_leks = 0;
        pobierzLeksem(0);

        //s -> XML_ST atrybuty XML_END tag_root KONIEC
        pobierzLeksem(Const.XML_ST);
        atrybuty();
        pobierzLeksem(Const.XML_END);
        tag_root();
        pobierzLeksem(Const.KONIEC);
        System.out.println("==================");
        System.out.println("XML jest poprawny.");
        System.out.println("==================");

    }

    //atrybuty ---> atrybut atrybuty | ?
    public void atrybuty() {
        switch (biezacy_leks) {
            case Const.NAPIS_SPECJALNY:
                atrybut();
                atrybuty();
                break;
            default:
                break;
        }
    }

    //atrybut ---> NAPIS_SECJALNY ROW WAR_ATR | ?
    public void atrybut() {
        switch (biezacy_leks) {
            case Const.NAPIS_SPECJALNY:
                pobierzLeksem(Const.NAPIS_SPECJALNY);
                pobierzLeksem(Const.ROW);
                pobierzLeksem(Const.WAR_ATR);
                break;
            default:
                break;
        }
    }

    //tag_root ---> OTW_ID atrybuty ZAM tag OTW_SL_ID ZAM
    public void tag_root() {
        switch (biezacy_leks) {

            case Const.OTW_ID:
                pobierzLeksem(Const.OTW_ID);
                atrybuty();
                pobierzLeksem(Const.ZAM);
                tag();
                pobierzLeksem(Const.OTW_SL_ID);
                pobierzLeksem(Const.ZAM);
                break;
            default:
                System.err.println("Blad skladni tag_root");
        }
    }
    //tag ---> OTW_ID atrybuty tag_kont | NAPIS_DOWOLNY tag | ?
    public void tag() {
        switch (biezacy_leks) {
            case Const.OTW_ID:
                pobierzLeksem(Const.OTW_ID);
                atrybuty();
                tag_kont();
                break;
            case Const.NAPIS_DOWOLNY:
                pobierzLeksem(Const.NAPIS_DOWOLNY);
                tag();
                break;
            default:
                break;
        }
    }

    //tag_kont ---> UKOS ZAM tag
    //tag_kont ---> ZAM tag OTW_SL_ID ZAM tag
    public void tag_kont() {
        switch (biezacy_leks) {
            case Const.UKOS:
                pobierzLeksem(Const.UKOS);
                pobierzLeksem(Const.ZAM);
                tag();
                break;
            case Const.ZAM:
                pobierzLeksem(Const.ZAM);
                tag();
                pobierzLeksem(Const.OTW_SL_ID);
                pobierzLeksem(Const.ZAM);
                tag();
                break;
            default:
                System.err.println("Blad skladni tag_kont");

        }
    }



    public void pobierzLeksem(int s) {
        if (biezacy_leks == s) {
            biezacy_leks = lekser.lekser();
        } else {
            System.err.println("blad skladni " + Const.ht.get(s) + " linia: " + (zwrocIndeks()-1) + " numer " + (lekser.getChar_num() - listind.get(zwrocIndeks()-2)));
            System.exit(0);

        }
    }


}
