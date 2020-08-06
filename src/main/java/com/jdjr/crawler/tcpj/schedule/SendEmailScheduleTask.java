package com.jdjr.crawler.tcpj.schedule;

import com.jd.jr.service.notice.IMailService;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.common.util.DateFormatUtils;
import com.jdjr.crawler.tcpj.repository.TaskLogRepository;
import com.jdjr.crawler.tcpj.repository.UserEmailRepository;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.repository.domain.TaskLog;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import com.jdjr.crawler.tcpj.repository.domain.UserEmail;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/5 16:54
 **/
@Component
@Slf4j
public class SendEmailScheduleTask {
    @Resource
    private UserEmailRepository userEmailRepository;
    @Resource
    private TaskLogRepository taskLogRepository;
    @Resource
    private UserAccountService userAccountService;
    @Resource
    private IMailService mailService;

    /**
     * 定期检测同城账号 是否命中风控信息，或Token过期
     * 0 0 9,11,14,19 * * ? 每天9,11,14,19点启动任务
     */
    @Scheduled(cron = "0 0 9,11,14,19 * * ?")
    public void sendEmailTask() {
        try {
            Boolean result = sendEmail();
            logger.info("send_email_schedule complete result:{}", result);
        } catch (Exception e) {
            logger.error("send_email_schedule exception {}", e.getMessage(), e);
        }
    }

    public Boolean sendEmail() {
        List<UserEmail> emails = listUsedEmailByCon();
        if (emails == null || emails.isEmpty()) {
            logger.warn("send_email_schedule email not config");
            return true;
        }
        //收件人集合
        List<String> emailList = emails.stream().map(UserEmail::getEmail).collect(Collectors.toList());
        if (emailList == null || emailList.isEmpty()) {
            logger.warn("send_email_schedule email not safe");
            return true;
        }
        //获取邮件内容
        String content = getContent();
        String title = String.format("票据服务-运行情况报告 %s", DateFormatUtils.getNowDate(DateFormatUtils.FormatEnums.yyyy_MM_dd_HH));
        //发送邮件
        Boolean result = mailService.sendHtml("票据服务", title, content, emailList, null, "token");
        return result;
    }

    /**
     * 查询所有可用email表数据
     */
    public List<UserEmail> listUsedEmailByCon() {
        List<UserEmail> list = userEmailRepository.findAll((Specification<UserEmail>) (root, criteriaQuery, cb) -> {
            List<Predicate> list1 = new ArrayList<>();
            list1.add(cb.equal(root.get("delete_flag").as(Integer.class), 0));//未删除状态
            Predicate[] predicates = new Predicate[list1.size()];
            return cb.and(list1.toArray(predicates));
        });
        return list;
    }

    /**
     * 组合邮件内容
     *
     * @return
     */
    private String getContent() {
        //获取所有Token
        List<LoginData> tcToken = userAccountService.getAllToken(BusinessEnums.TCPJ);
        List<LoginData> bhToken = userAccountService.getAllToken(BusinessEnums.BIHU);
        //获取所有账号
        List<UserAccount> tcUsers = userAccountService.getAll(BusinessEnums.TCPJ);
        List<UserAccount> bhUsers = userAccountService.getAll(BusinessEnums.BIHU);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h2>票据服务运行情况报告 " + DateFormatUtils.getNowDate() + "</h2></br>");
        stringBuilder.append("<p><h3>同城票据网-账号信息情况:</h3></p></br>");
        //同城票据记录处理
        String tcTable = getHtmlStr(BusinessEnums.TCPJ, tcUsers, tcToken);
        stringBuilder.append(tcTable);
        //壁虎票据记录处理
        stringBuilder.append("</br>");
        stringBuilder.append("<p><h3>壁虎找票网-账号信息情况:</h3></p></br>");
        String bhTable = getHtmlStr(BusinessEnums.BIHU, bhUsers, bhToken);
        stringBuilder.append(bhTable);
        //日志信息情况
        stringBuilder.append("</br>");
        stringBuilder.append("<p><h3>最近10条日志信息:</h3></p></br>");
        String logStr = getLogHtml();
        stringBuilder.append(logStr);

        return stringBuilder.toString();
    }


    private String getHtmlStr(BusinessEnums siteEnum, List<UserAccount> userAccounts, List<LoginData> loginDatas) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (userAccounts == null || userAccounts.isEmpty()) {
                stringBuilder.append("<p style='color:#ff0000;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无可用账户</p></br>");
            } else {
                stringBuilder.append("<table style='border:1px solid;'>" +
                        "<tr>" +
                        "<td>序号</td>" +
                        "<td>账号</td>" +
                        "<td>密码</td>" +
                        "<td>账号类型</td>" +
                        "<td>返回码</td>" +
                        "<td>返回码时间</td>" +
                        "<td>返回码描述</td>" +
                        "<td>Token</td>" +
                        "<td>Token更新时间</td>" +
                        "<td>是否使用</td>" +
                        "<td>是否可用</td>" +
                        "<td>是否可用备注</td>" +
                        "</tr>");
                for (int i = 0; i < userAccounts.size(); i++) {
                    UserAccount account = userAccounts.get(i);
                    stringBuilder.append("<tr>");
                    stringBuilder.append(String.format("<td>%s</td>", i + 1));//序号
                    if (!StringUtils.isEmpty(account.getCode()) && !account.getCode().equals("0")) {
                        stringBuilder.append(String.format("<td style='color:#ff0000;'>%s</td>", account.getAccount()));//账号
                    } else {
                        stringBuilder.append(String.format("<td>%s</td>", account.getAccount()));//账号
                    }
                    stringBuilder.append(String.format("<td>%s</td>", account.getPassword()));//密码
                    stringBuilder.append(String.format("<td>%s</td>", account.getType()));//账号类型
                    stringBuilder.append(String.format("<td>%s</td>", account.getCode()));//返回码
                    stringBuilder.append(String.format("<td>%s</td>", account.getCode_time() != null ? DateFormatUtils.dateFormat(account.getCode_time()) : "-"));//返回码时间
                    stringBuilder.append(String.format("<td>%s</td>", account.getMsg()));//返回码描述

                    if (loginDatas == null || loginDatas.isEmpty()) {
                        stringBuilder.append(String.format("<td>%s</td>", "-"));//Token
                        stringBuilder.append(String.format("<td>%s</td>", "-"));//Token更新时间
                        stringBuilder.append(String.format("<td>%s</td>", "-"));//是否使用
                        stringBuilder.append(String.format("<td>%s</td>", "-"));//是否可用
                        stringBuilder.append(String.format("<td>%s</td>", "-"));//是否可用备注
                    } else {
                        //查找出对应的Token数据
                        List<LoginData> loginDataList = loginDatas.stream().filter(token -> {
                            if (token.getSite().equals(siteEnum.getValue()) && token.getAccount().equals(account.getAccount())) {
                                return true;
                            }
                            return false;
                        }).collect(Collectors.toList());
                        //设置Token信息
                        if (loginDataList == null || loginDataList.isEmpty()) {
                            stringBuilder.append(String.format("<td>%s</td>", "-"));//Token
                            stringBuilder.append(String.format("<td>%s</td>", "-"));//Token更新时间
                            stringBuilder.append(String.format("<td>%s</td>", "-"));//是否使用
                            stringBuilder.append(String.format("<td>%s</td>", "-"));//是否可用
                            stringBuilder.append(String.format("<td>%s</td>", "-"));//是否可用备注
                        } else {
                            LoginData loginData = loginDataList.get(0);
                            stringBuilder.append(String.format("<td>%s</td>", loginData.getToken()));//Token
                            stringBuilder.append(String.format("<td>%s</td>", loginData.getTimeStamp() != null ? DateFormatUtils.dateFormat(loginData.getTimeStamp()) : "-"));//Token更新时间
                            stringBuilder.append(String.format("<td>%s</td>", loginData.getIsUsed()));//是否使用
                            stringBuilder.append(String.format("<td>%s</td>", loginData.getUseful()));//是否可用
                            stringBuilder.append(String.format("<td>%s</td>", loginData.getRemark()));//是否可用备注
                        }
                    }
                    stringBuilder.append("</tr>");
                }
                stringBuilder.append("</table>");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组装数据失败" + siteEnum.getValue();
        }
    }

    /**
     * 获取日志内容
     *
     * @return
     */
    private String getLogHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            Pageable pageable = PageRequest.of(0, 10, sort);
            Page<TaskLog> page = taskLogRepository.findAll(pageable);
            List<TaskLog> taskLogs = page.getContent();
            if (taskLogs == null || taskLogs.isEmpty()) {
                stringBuilder.append("<p style='color:#ff0000;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无任务运行日志</p></br>");
                return stringBuilder.toString();
            }
            stringBuilder.append("<ul>");
            for (int i = 0; i < taskLogs.size(); i++) {
                stringBuilder.append(String.format("<li>%s-%s</br></li>", taskLogs.get(i).getSite(), taskLogs.get(i).getLogStr()));//序号
            }
            return stringBuilder.append("</ul>").toString();
        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组装数据失败Task log";
        }
    }
}
