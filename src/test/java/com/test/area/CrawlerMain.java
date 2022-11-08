package com.test.area;

import com.gjw.common.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/13.html
public class CrawlerMain {
    static String urlP = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/";

    public static void main(String[] args) throws IOException {


        requestCity("13","河北省");
    }


    /**
     * 获取市级数据
     */
    public static String requestCity(String pId,String pName) throws IOException {
        String url = urlP.concat(pId).concat(".html");
        //获取 市 数据
        System.out.println("请求地址："+url);
        //String responseStr = HttpClientUtils.get(url,500);
        String responseStr = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd\"><HTML><HEAD><META content=\"text/html; charset=utf-8\" http-equiv=Content-Type><TITLE>2021年统计用区划代码</TITLE><STYLE type=text/css>BODY {MARGIN: 0px}BODY {FONT-SIZE: 12px}TD {FONT-SIZE: 12px}TH {FONT-SIZE: 12px}.redBig {COLOR: #d00018; FONT-SIZE: 18px; FONT-WEIGHT: bold}.STYLE3 a{COLOR: #fff; text-decoration:none;}.STYLE5 {COLOR: #236fbe; FONT-WEIGHT: bold}.content {LINE-HEIGHT: 1.5; FONT-SIZE: 10.4pt}.tdPading {PADDING-LEFT: 30px}.blue {COLOR: #0000ff}.STYLE6 {COLOR: #ffffff}.a2 {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}a2:link {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}a2:hover {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px; TEXT-DECORATION: underline}a2:visited {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}</STYLE><SCRIPT language=javascript>function doZoom(size){document.getElementById(\"zoom\").style.fontSize=size+\"px\";}</SCRIPT><META name=GENERATOR content=\"MSHTML 8.00.7600.16700\"></HEAD><BODY><TABLE border=0 cellSpacing=0 cellPadding=0 width=778 align=center><TBODY><TR><TD colSpan=2><IMG src=\"http://www.stats.gov.cn/images/banner.jpg\" width=778 height=135></TD></TR></TBODY></TABLE><MAP id=Map name=Map><AREA href=\"http://www.stats.gov.cn/english/\" shape=rect coords=277,4,328,18><AREA href=\"http://www.stats.gov.cn:82/\" shape=rect coords=181,4,236,18><AREA href=\"http://www.stats.gov.cn/\" shape=rect coords=85,4,140,17></MAP><TABLE border=0 cellSpacing=0 cellPadding=0 width=778 align=center><TBODY><TR><TD vAlign=top><TABLE style=\"MARGIN-TOP: 15px; MARGIN-BOTTOM: 18px\" border=0 cellSpacing=0 cellPadding=0 width=\"100%\" align=center><TBODY><TR><TD style=\" BACKGROUND-REPEAT: repeat-x; BACKGROUND-POSITION: 50% top\" background=/images/topLine.gif align=right></TD></TR><TR><TD style=\"BACKGROUND-REPEAT: repeat-y; BACKGROUND-POSITION: right 50%\" vAlign=top background=images/rightBorder.gif><TABLE border=0 cellSpacing=0 cellPadding=0 width=\"100%\"><TBODY><TR><TD width=\"1%\" height=\"200\" vAlign=top><table class=\"citytable\"><tr class=\"cityhead\"><td width=150>统计用区划代码</td><td>名称</td></tr><tr class=\"citytr\"><td><a href=\"13/1301.html\">130100000000</a></td><td><a href=\"13/1301.html\">石家庄市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1302.html\">130200000000</a></td><td><a href=\"13/1302.html\">唐山市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1303.html\">130300000000</a></td><td><a href=\"13/1303.html\">秦皇岛市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1304.html\">130400000000</a></td><td><a href=\"13/1304.html\">邯郸市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1305.html\">130500000000</a></td><td><a href=\"13/1305.html\">邢台市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1306.html\">130600000000</a></td><td><a href=\"13/1306.html\">保定市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1307.html\">130700000000</a></td><td><a href=\"13/1307.html\">张家口市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1308.html\">130800000000</a></td><td><a href=\"13/1308.html\">承德市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1309.html\">130900000000</a></td><td><a href=\"13/1309.html\">沧州市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1310.html\">131000000000</a></td><td><a href=\"13/1310.html\">廊坊市</a></td></tr><tr class=\"citytr\"><td><a href=\"13/1311.html\">131100000000</a></td><td><a href=\"13/1311.html\">衡水市</a></td></tr></table></TD></TR></TBODY></TABLE></TD></TR><TR><TD style=\"BACKGROUND-REPEAT: repeat-x; BACKGROUND-POSITION: 50% top\" background=images/borderBottom.gif></TD></TR></TBODY></TABLE></TD></TR>  <TR>    <TD bgColor=#e2eefc height=2></TD></TR>  <TR>    <TD class=STYLE3 height=60>      <DIV align=center style=\"background-color:#1E67A7; height:75px; color:#fff;\"><br/>版权所有：国家统计局　　<A class=STYLE3       href=\"http://www.miibeian.gov.cn/\"       target=_blank>京ICP备05034670号</A><BR><BR>地址：北京市西城区月坛南街57号（100826）<BR></DIV></TD></TR></TBODY></TABLE></BODY></HTML>";
        System.out.println(responseStr);

        Document document = Jsoup.parse(responseStr);
        List<Element> elements = document.selectXpath(("//table[@class='citytable'] //tr[@class='citytr']"), Element.class);

        System.out.println("***********  获取市数据集合  "+elements.size());
        for (Element element:elements){
            List<Element> elementList = element.getElementsByTag("a");

            String hrefUrl = elementList.get(0).attr("href");
            String cId = hrefUrl.replace(pId+"/","").replace(".html","");
            String cName = elementList.get(1).text();

            requestQx(hrefUrl,pId,pName,cId,cName);
        }
        //获取 区县 数据

        return responseStr;
    }

    /**
     * 获取区县数据
     */
    private static String requestQx(String url,String pId,String pName,String cId,String cName) throws IOException {
        if(!Objects.equals("13/1305.html",url)){
            return "";
        }

        String reqUrl = urlP.concat(url);
        //String responseStr = HttpClientUtils.get(reqUrl,500);
        String responseStr = " <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd\"><HTML><HEAD><META content=\"text/html; charset=utf-8\" http-equiv=Content-Type><TITLE>2021年统计用区划代码</TITLE><STYLE type=text/css>BODY {MARGIN: 0px}BODY {FONT-SIZE: 12px}TD {FONT-SIZE: 12px}TH {FONT-SIZE: 12px}.redBig {COLOR: #d00018; FONT-SIZE: 18px; FONT-WEIGHT: bold}.STYLE3 a{COLOR: #fff; text-decoration:none;}.STYLE5 {COLOR: #236fbe; FONT-WEIGHT: bold}.content {LINE-HEIGHT: 1.5; FONT-SIZE: 10.4pt}.tdPading {PADDING-LEFT: 30px}.blue {COLOR: #0000ff}.STYLE6 {COLOR: #ffffff}.a2 {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}a2:link {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}a2:hover {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px; TEXT-DECORATION: underline}a2:visited {LINE-HEIGHT: 1.5; COLOR: #2a6fbd; FONT-SIZE: 12px}</STYLE><SCRIPT language=javascript>function doZoom(size){document.getElementById(\"zoom\").style.fontSize=size+\"px\";}</SCRIPT><META name=GENERATOR content=\"MSHTML 8.00.7600.16700\"></HEAD><BODY><TABLE border=0 cellSpacing=0 cellPadding=0 width=778 align=center><TBODY><TR><TD colSpan=2><IMG src=\"http://www.stats.gov.cn/images/banner.jpg\" width=778 height=135></TD></TR></TBODY></TABLE><MAP id=Map name=Map><AREA href=\"http://www.stats.gov.cn/english/\" shape=rect coords=277,4,328,18><AREA href=\"http://www.stats.gov.cn:82/\" shape=rect coords=181,4,236,18><AREA href=\"http://www.stats.gov.cn/\" shape=rect coords=85,4,140,17></MAP><TABLE border=0 cellSpacing=0 cellPadding=0 width=778 align=center><TBODY><TR><TD vAlign=top><TABLE style=\"MARGIN-TOP: 15px; MARGIN-BOTTOM: 18px\" border=0 cellSpacing=0 cellPadding=0 width=\"100%\" align=center><TBODY><TR><TD style=\" BACKGROUND-REPEAT: repeat-x; BACKGROUND-POSITION: 50% top\" background=..//images/topLine.gif align=right></TD></TR><TR><TD style=\"BACKGROUND-REPEAT: repeat-y; BACKGROUND-POSITION: right 50%\" vAlign=top background=../images/rightBorder.gif><TABLE border=0 cellSpacing=0 cellPadding=0 width=\"100%\"><TBODY><TR><TD width=\"1%\" height=\"200\" vAlign=top><table class=\"countytable\"><tr class=\"countyhead\"><td width=150>统计用区划代码</td><td>名称</td></tr><tr class=\"countytr\"><td>130501000000</td><td>市辖区</td></tr><tr class=\"countytr\"><td><a href=\"05/130502.html\">130502000000</a></td><td><a href=\"05/130502.html\">襄都区</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130503.html\">130503000000</a></td><td><a href=\"05/130503.html\">信都区</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130505.html\">130505000000</a></td><td><a href=\"05/130505.html\">任泽区</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130506.html\">130506000000</a></td><td><a href=\"05/130506.html\">南和区</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130522.html\">130522000000</a></td><td><a href=\"05/130522.html\">临城县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130523.html\">130523000000</a></td><td><a href=\"05/130523.html\">内丘县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130524.html\">130524000000</a></td><td><a href=\"05/130524.html\">柏乡县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130525.html\">130525000000</a></td><td><a href=\"05/130525.html\">隆尧县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130528.html\">130528000000</a></td><td><a href=\"05/130528.html\">宁晋县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130529.html\">130529000000</a></td><td><a href=\"05/130529.html\">巨鹿县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130530.html\">130530000000</a></td><td><a href=\"05/130530.html\">新河县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130531.html\">130531000000</a></td><td><a href=\"05/130531.html\">广宗县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130532.html\">130532000000</a></td><td><a href=\"05/130532.html\">平乡县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130533.html\">130533000000</a></td><td><a href=\"05/130533.html\">威县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130534.html\">130534000000</a></td><td><a href=\"05/130534.html\">清河县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130535.html\">130535000000</a></td><td><a href=\"05/130535.html\">临西县</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130571.html\">130571000000</a></td><td><a href=\"05/130571.html\">河北邢台经济开发区</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130581.html\">130581000000</a></td><td><a href=\"05/130581.html\">南宫市</a></td></tr><tr class=\"countytr\"><td><a href=\"05/130582.html\">130582000000</a></td><td><a href=\"05/130582.html\">沙河市</a></td></tr></table></TD></TR></TBODY></TABLE></TD></TR><TR><TD style=\"BACKGROUND-REPEAT: repeat-x; BACKGROUND-POSITION: 50% top\"          background=../images/borderBottom.gif></TD></TR></TBODY></TABLE></TD></TR>  <TR>    <TD bgColor=#e2eefc height=2></TD></TR>  <TR>    <TD class=STYLE3 height=60>      <DIV align=center style=\"background-color:#1E67A7; height:75px; color:#fff;\"><br/>版权所有：国家统计局　　<A class=STYLE3       href=\"http://www.miibeian.gov.cn/\"       target=_blank>京ICP备05034670号</A><BR><BR>地址：北京市西城区月坛南街57号（100826）<BR></DIV></TD></TR></TBODY></TABLE></BODY></HTML>";

        System.out.println("获取区县 数据 "+responseStr);


        Document document = Jsoup.parse(responseStr);
        List<Element> elements = document.selectXpath(("//table[@class='countytable'] //tr[@class='countytr']"), Element.class);

        System.out.println("***********  获取市数据集合  "+elements.size());
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if(i==1){
                List<Element> elementList = element.getElementsByTag("td");
            }



            List<Element> elementList = element.getElementsByTag("a");

            String hrefUrl = elementList.get(1).attr("href");
            String xId = hrefUrl.replace(pId+"/","").replace(".html","");
            String xName = elementList.get(1).text();

            System.out.println("区县 "+xId +","+xName);
        }
        return "";
    }


    @Setter
    @Getter
    @AllArgsConstructor
    public static class AreaInfo {
        private Long id;
        private Long pId;
        private String name;
    }
}
