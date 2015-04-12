package pl.rr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rykowskr
 */
public class ErrorMsgs {

    public static final Map<GrammaEnum, String> hm;
    //for error msgs
    static {
        HashMap<GrammaEnum, String> _hm = new HashMap<>();
        _hm.put(GrammaEnum.XML_START, "<?xml");
        _hm.put(GrammaEnum.XML_END, "?>");
        _hm.put(GrammaEnum.GREATERTHAN, "domknięcia '>'");
        _hm.put(GrammaEnum.SLASH, "ukośnika '/'");
        _hm.put(GrammaEnum.EQ, "rowności '='");
        _hm.put(GrammaEnum.ANY_STRING, "we wnętrzu taga");
        _hm.put(GrammaEnum.ATTR_VALUE, "wartości atrybutu");
        _hm.put(GrammaEnum.LESSTHAN_SLASH_ID, "id tagu zamykającego");
        _hm.put(GrammaEnum.LESSTHAN_ID, "id tagu otwierającego");
        _hm.put(GrammaEnum.ATTR_NAME, "rodzaju atybutu");
        _hm.put(GrammaEnum.END, "końcu");
        hm = Collections.unmodifiableMap(_hm);
    }



}

