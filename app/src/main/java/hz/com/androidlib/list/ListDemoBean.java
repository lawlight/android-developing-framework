package hz.com.androidlib.list;

/**
 * Created by LiuPeng on 16/6/12.
 */
public class ListDemoBean {
    public String text1;
    public String text2;
    public String longText;

    public ListDemoBean(String text1, String text2, String longText) {
        this.text1 = text1;
        this.text2 = text2;
        this.longText = longText;
    }


    @Override
    public String toString() {
        return text1 + "," + text2;
    }
}
