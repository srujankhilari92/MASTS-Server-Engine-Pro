package secure;

/**
 *
 * @author swapnil
 */
public class MastsvFE {

    private String keygard1;

    public MastsvFE() {
        super();
        MastsvR c = new MastsvR();
        String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);

        setKeygard1(key);

    }

    public String getKeygard1() {
        return keygard1;
    }

    public void setKeygard1(String keygard1) {
        this.keygard1 = keygard1;
    }
}
