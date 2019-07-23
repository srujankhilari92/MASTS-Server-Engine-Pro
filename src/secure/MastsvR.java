/**
 * @Class Name : Resources.java
 */
package secure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jun 23, 2015 10:58:25 AM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvR {

    public List<String> encrypt(String key1, String key2) {

        List<String> list = new ArrayList<String>();
        String res1 = "", res2 = "";
        int l = key2.length() - 1;
        int l1 = key1.length() - 1;
        for (int i = 0; i < key1.length() / 2; i++) {

            res1 = res1 + key1.charAt(i) + key2.charAt(l);
            l = l - 1;

        }
        for (int i = 0; i < key2.length() / 2; i++) {
            res2 = res2 + key2.charAt(i) + key1.charAt(l1);
            l1 = l1 - 1;
        }

        list.add(res1);
        list.add(res2);
        return list;
    }

    public String toChange(int i) {

        return Character.toString((char) i);
    }
}
