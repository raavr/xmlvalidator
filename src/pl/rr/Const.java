package pl.rr;

import java.util.Hashtable;

/**
 *
 * @author rykowskr
 */
public class Const {
    public static final int XML_ST = 256;// <?xml
    public static final int XML_END = 257;// ?>

    public static final int ZAM = 259;// >
    public static final int UKOS = 260;// /
    public static final int ROW = 261;// =


    public static final int NAPIS_DOWOLNY = 263;// wszystko bez <
    public static final int WAR_ATR = 264; // " ciag znakow oprocz < "
    public static final int OTW_SL_ID = 265; // </ ciag znakow a-z A-Z 0-9 . - _ bez liczb na pocz�tku
    public static final int OTW_ID = 266; // < ciag znakow a-z A-Z 0-9 . - _ bez liczb na pocz�tku

    public static final int NAPIS_SPECJALNY = 268; // ciag znakow bez spacji i <
    public static final int ERROR = -1;

    public static final int KONIEC = 11;

    public static Hashtable<Integer, String> ht = new Hashtable<Integer, String>();

    //do sygnalizacji bledow
    public static void wypelnij() {
        ht.put(256, "<?xml");
        ht.put(257, "?>");
        ht.put(259, "domkni�cia '>'");
        ht.put(260, "uko�nika '/'");
        ht.put(261, "rowno�ci '='");
        ht.put(263, "we wn�trzu taga");
        ht.put(264, "warto�ci atrybutu");
        ht.put(265, "id tagu zamykaj�cego");
        ht.put(266, "id tagu otwieraj�cego");
        ht.put(268, "rodzaju atybutu");
        ht.put(11, "ko�cu");


    }


}

