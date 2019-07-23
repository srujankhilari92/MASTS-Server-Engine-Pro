package ethernet;

import config.MastsvCong;

public class MastsvCB {

    private String key1;
    private String key2;
    private String key3;
    private String key4;

    public MastsvCB() {
        super();

        MastsvCong res = new MastsvCong();
        String a = res.toChange(69) + res.toChange(110) + res.toChange(99) + res.toChange(114)
                + res.toChange(121) + res.toChange(68) + res.toChange(50) + res.toChange(50)
                + res.toChange(77) + res.toChange(48) + res.toChange(54) + res.toChange(89)
                + res.toChange(50) + res.toChange(48) + res.toChange(49) + res.toChange(53);
        String b = res.toChange(77) + res.toChange(79) + res.toChange(77) + res.toChange(84)
                + res.toChange(48) + res.toChange(53) + res.toChange(77) + res.toChange(52)
                + res.toChange(53) + res.toChange(90) + res.toChange(80) + res.toChange(77)
                + res.toChange(83) + res.toChange(48) + res.toChange(51) + res.toChange(51);

        String a1 = res.toChange(97) + res.toChange(49) + res.toChange(66) + res.toChange(48)
                + res.toChange(99) + res.toChange(51) + res.toChange(68) + res.toChange(57)
                + res.toChange(101) + res.toChange(56) + res.toChange(102) + res.toChange(48)
                + res.toChange(66) + res.toChange(52) + res.toChange(102) + res.toChange(57);

        String b1 = res.toChange(101) + res.toChange(56) + res.toChange(67) + res.toChange(49)
                + res.toChange(70) + res.toChange(56) + res.toChange(97) + res.toChange(57)
                + res.toChange(99) + res.toChange(56) + res.toChange(68) + res.toChange(57)
                + res.toChange(69) + res.toChange(51) + res.toChange(102) + res.toChange(55);

        setKey1(res.encrypt(a, b).get(0));
        setKey2(res.encrypt(a, b).get(1));
        setKey3(res.encrypt(a1, b1).get(0));
        setKey4(res.encrypt(a1, b1).get(1));

    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

}
