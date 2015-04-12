package pl.rr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rykowskr
 */
public class ErrorMsgs {

    public static final Map<Gramma, String> hm;
    //for error msgs
    static {
        HashMap<Gramma, String> _hm = new HashMap<>();
        _hm.put(Gramma.XML_START, "<?xml");
        _hm.put(Gramma.XML_END, "?>");
        _hm.put(Gramma.GREATERTHAN, "domknięcia '>'");
        _hm.put(Gramma.SLASH, "ukośnika '/'");
        _hm.put(Gramma.EQ, "rowności '='");
        _hm.put(Gramma.ANY_STRING, "we wnętrzu taga");
        _hm.put(Gramma.ATTR_VALUE, "wartości atrybutu");
        _hm.put(Gramma.LESSTHAN_SLASH_ID, "id tagu zamykającego");
        _hm.put(Gramma.LESSTHAN_ID, "id tagu otwierającego");
        _hm.put(Gramma.ATTR_NAME, "rodzaju atybutu");
        _hm.put(Gramma.END, "końcu");
        hm = Collections.unmodifiableMap(_hm);
    }



}

