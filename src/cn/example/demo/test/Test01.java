package cn.example.demo.test;

import cn.example.util.HttpRequest;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Test01 {
    @Test
    public void t1() {
        String res = HttpRequest.post("http://www.123.com/vue", "action=list");
        System.out.print(res);
    }

    @Test
    public void t2() {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("action", "list");

        String result3 = HttpUtil.get("http://www.123.com/vue", paramMap);
        System.out.print(result3);
    }

    @Test
    public void t3() {
        //请求列表页
        String listContent = HttpUtil.get("https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=2");
//使用正则获取所有标题
        List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
        for (String title : titles) {
            //打印标题
            Console.log(title);
        }
    }

    @Test
    public void t4() {
        String m5 = SecureUtil.md5("123456");
        System.out.print(m5);
    }

    @Test
    public void t5() {
        File file = FileUtil.file("D:/javawebproject/demo/web/static/image/D:/javawebproject/1.jpg");
        String type = FileTypeUtil.getType(file);
//输出 jpg则说明确实为jpg文件
        Console.log(type);
    }

    @Test
    public void t6() {
        FileWriter writer = new FileWriter("D:/javawebproject/demo/web/static/image/test.properties");
        writer.write("test");
    }

    /**
     * 分页
     */
    @Test
    public void t7() {
        int totalPage = PageUtil.totalPage(2000, 10);//7
        int[] startEnd2 = PageUtil.transToStartEnd(1, 10);//[10, 20]
        int[] page = PageUtil.rainbow(30, totalPage);
        Console.log(page);
    }

    /**
     * 图片缩放
     */
    @Test
    public void t8() {
        ImgUtil.scale(
                FileUtil.file("D:/javawebproject/demo/web/static/image/1.jpg"),
                FileUtil.file("D:/javawebproject/demo/web/static/image/heght.jpg"),
                0.5f//缩放比例
        );
    }

    /**
     * 剪裁图片
     */
    @Test
    public void t9() {
        ImgUtil.cut(
                FileUtil.file("D:/javawebproject/demo/web/static/image/1.jpg"),
                FileUtil.file("D:/javawebproject/demo/web/static/image/cut.jpg"),
                new Rectangle(200, 200, 100, 100)//裁剪的矩形区域
        );
    }

    /**
     * 生成二维码
     */
    @Test
    public void t10() {
        QrCodeUtil.generate("https://hutool.cn/", 300, 300,
                FileUtil.file("D:/javawebproject/demo/web/static/image/qrcode.jpg"));
    }
}
