package hz.com.androidlib.list;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.hz.lib.webapi.WebAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分页加载的测试接口
 * Created by LiuPeng on 16/9/7.
 */
public class ListDemoAPI extends WebAPI{

    //一些静态长文本数据
    String[] longTests = new String[]{
            "各位应该还记得，去年7月份，中央出台了“关于加强和改进党的群团工作的意见”，对包括工青妇在内的群团组织的改革发展做出具体的谋划和安排。“意见”提出，全国总工会、共青团中央、全国妇联等中央管理的群团组织，要根据意见要求制定实施方案。",
            "本周给大家带来的是APAC的赛事战报，线上海选赛决赛C组的Black Ananas战队对战 JH Gaming.G，地图为花村的一场激烈的攻防战。在A点秒掉的情况下，BA面对JH猛烈的进攻，拿出了小美，防守成功。整整7分钟，JH对BA神一样的小美发挥无可奈何，让我们来看看BA的小美到底是怎么做到的。",
            "南海风平浪静，偏有几个国家上蹿下跳，兴风作浪，惟恐天下不乱。除了美国和日本之外，还有一个就是弹丸小国新加坡。一个城市般大小的国家，说中国话、写中国字，干的却是反华的事：",
            "航母一直以来都被公认为海上霸主，拥有航母的国家其的海上战斗力必然不可小觑。而目前拥有最多航美的国家是美国，该国共拥有11艘航母，其海上军事实力也是世界各国无人能及的。而据俄国的网站称，中国的第一首航母也将建成，据报道该艘航母的造价已经达到了500-700亿元人民币，如此天价是很多国家都无法承受的。",
            "中国天气网讯 预计，未来三天西南地区依旧阴雨绵绵，以小到中雨为主，局地有大雨或暴雨。在北方地区，内蒙古中东部、华北中北部、东北地区大部将迎来一次降水过程，局地中到大雨。尽管未来几天北方将有降雨，但由于冷空气总体偏弱偏北，北方气温将明显回升，东北、华北最高气温将回升至25℃，与常年同期相比气温偏高。",
            "你家里有闲置不用的银行卡吗？如果有，请查查这些卡片有没有给你带来麻烦。而且，如何处理才更安全呢？",
            "“导演也好，演员也好，如果总拿一个作品说事儿，那不光荣，那是他们太懒了，丢人。”所以被问到十周年最想对《士兵突击》说的话，康洪雷说：拜拜。",
            "近日，一则9名中国留学生买下加拿大温哥华格雷岬区共计5700万加元（约2.88亿人民币）豪宅的新闻引发大量关注。因买卖文件上显示9名买家职业信息均为“学生”，且多是通过银行贷款买房，在当地引起争议。",
            "昨天晚上，微信揭开了外界期盼已久的“应用号”神秘面纱——小程序。这可能是今年微信最重要的产品。",
            "大家都知道，今年苹果iPhone 7的亮黑色很受欢迎，国行自从16号上市以来，最高的时候亮黑色的iPhone 7炒作到1.3万元，翻倍还不止。这让苹果的黄牛陷入了疯狂，虽然官方此前承诺的预定要4-6周才可以发货，但是世界4-6天就发货，毫无疑问，苹果iPhone 7和iPhone 7plus的备货比较充足的。"};

    public int pageNumber;
    public int pageSize = 10;

    public List<ListDemoBean> list = new ArrayList<>();

    /**
     * 构造方法
     *
     * @param context
     */
    public ListDemoAPI(Context context) {
        super(context);
        setTest(true);
    }

    @Override
    protected String getURL() {
        return "";
    }

    @Override
    protected String getNAMESPACE() {
        return null;
    }

    @Override
    protected String getMethodName() {
        return null;
    }

    @Override
    protected void getParams(HashMap<String, Object> params) {
        params.put("page", pageNumber);
    }

    @Override
    protected boolean analysisOutput(String result) throws JSONException {
        list.clear();
        if(pageNumber > 10){
            return true;
        }
        for(int i = 0 ; i < pageSize ;i++){
            list.add(new ListDemoBean("第"+pageNumber+"页第"+(i+1)+"项", "这里是副标题，测试数据", longTests[i%10]));
        }
        return true;
    }
}
