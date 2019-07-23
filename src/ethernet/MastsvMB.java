package ethernet;

import config.MastsvCong;

public class MastsvMB {

    private String mkey1;
    private String mkey2;

    public MastsvMB() {
        super();
        MastsvCong c = new MastsvCong();
        String key1 = c.toChange(68) + c.toChange(114) + c.toChange(49) + c.toChange(110)
                + c.toChange(107) + c.toChange(53) + c.toChange(48) + c.toChange(48)
                + c.toChange(104) + c.toChange(48) + c.toChange(118) + c.toChange(49)
                + c.toChange(114) + c.toChange(100) + c.toChange(48) + c.toChange(57);

        String key2 = c.toChange(49) + c.toChange(52) + c.toChange(51) + c.toChange(104)
                + c.toChange(97) + c.toChange(99) + c.toChange(107) + c.toChange(101)
                + c.toChange(114) + c.toChange(109) + c.toChange(101) + c.toChange(55)
                + c.toChange(109) + c.toChange(57) + c.toChange(101) + c.toChange(102);

        setMkey1(c.encrypt(key1, key2).get(0));
        setMkey2(c.encrypt(key1, key2).get(1));

    }

    public String getMkey1() {

        return mkey1;
    }

    public void setMkey1(String mkey1) {
        this.mkey1 = mkey1;
    }

    public String getMkey2() {

        return mkey2;
    }

    public void setMkey2(String mkey2) {
        this.mkey2 = mkey2;
    }

}
