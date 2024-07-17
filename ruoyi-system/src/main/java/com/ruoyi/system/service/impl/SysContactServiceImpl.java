package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.system.mapper.SysAccountMapper;
import com.ruoyi.system.mapper.SysContactMapper;
import com.ruoyi.system.service.ISysContactService;
import com.ruoyi.system.util.TGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-11
 */
@Service
public class SysContactServiceImpl implements ISysContactService {
    @Autowired
    private SysContactMapper sysContactMapper;


    @Autowired
    private TGUtil tgUtil;


    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private MyJobMapper myJobMapper;

    /**
     * 查询
     *
     * @param sysContactId 主键
     * @return
     */
    @Override
    public SysContact selectSysContactBySysContactId(Long sysContactId) {
        return sysContactMapper.selectSysContactBySysContactId(sysContactId);
    }


    @Override
    public AjaxResult syncContact(List<SysAccount> sysAccountList) {
        sysAccountList.forEach(e -> {
            String sysAccountStringSession = e.getSysAccountStringSession();
            HashMap parms = new HashMap();
            parms.put("sysAccountStringSession", sysAccountStringSession);
            try {
                String syncContact = tgUtil.GenerateCommand("syncContact", parms);
                for (String s : syncContact.split("\n")) {
                    String substring = s.substring(s.indexOf("{"), s.indexOf("}") + 1);
                    Map map = JSON.parseObject(tgUtil.decodeJson(substring).replace("\\\\\"", "\\\""), Map.class);
                    SysContact sysContact = new SysContact();
                    sysContact.setSysStatus(Long.parseLong((String) map.get("stutas")));
                    String firstName=StringUtils.isEmpty((String) map.get("firstname"))? "" :(String) map.get("firstname");
                    String lastName=StringUtils.isEmpty((String) map.get("lastname"))? "" :(String) map.get("lastname");
                    String name = firstName+lastName;
                    sysContact.setSysContactName(name);
                    sysContact.setSysContactUserName((String) map.get("sysContactUserName"));
                    sysContact.setSysMutualContact(Long.parseLong((String) map.get("sysMutualContact")));
                    sysContact.setSysUserId(getLoginUser().getUserId());
                    sysContact.setSysContactPhone((String) map.get("contactNumberr"));
                    sysContact.setSysContactId(Long.parseLong((String) map.get("contactId")));
                    sysContact.setSysAccountId(e.getSysAccountId());
                    sysContactMapper.insertSysContact(sysContact);
                }
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        });

        return AjaxResult.success("同步成功");
    }

    /**
     * 查询列表
     *
     * @param sysContact
     * @return
     */
    @Override
    public List<SysContact> selectSysContactList(SysContact sysContact) {
        sysContact.setSysUserId(getLoginUser().getUserId());
        List<SysContact> sysContactList = sysContactMapper.selectSysContactList(sysContact);
        for (SysContact contact : sysContactList) {
            contact.setSysContactName(contact.getSysContactName().replaceAll("null", "").replace("None", ""));
        }
        return sysContactList;
    }

    @Override
    public AjaxResult addContact(SysContact Contact) throws InterruptedException, IOException {
        String userNameAdd = Contact.getUserNameAdd();
        String[] split = userNameAdd.split("\\\n");
        List<String> sysAccountIds = Contact.getSysAccountIds();
        List<String> list = new ArrayList<>();
        Long date = System.currentTimeMillis();
        for (String s : split) {
            String[] split1 = s.split(",");
            for (String s1 : split1) {
                list.add(s1);
            }
        }
        boolean jobFlag=list.size()>1;
        HashMap jobIds = new HashMap();
        List<SysAccount> sysAccounts = sysAccountMapper.selectByStrings(sysAccountIds);

        //创建任务
        if(jobFlag){
            for (SysAccount sysAccount : sysAccounts) {
                HashMap parms = new HashMap();
                parms.put("sessionPath", sysAccount.getSysAccountStringSession());
                MyJob job = new MyJob();
                job.setTarNum(list.size());
                job.setJobType("1");
                job.setJobName("用户名添加好友");
                job.setJobClass("com.ruoyi.system.runnable.addContactRunnable");
                job.setIntervals(3+"");
                job.setIntervalsUnit(0);
                job.setJobGroup(String.valueOf(date));
                job.setCreateDate(DateUtils.getNowDate());
                job.setParms(JSON.toJSONString(parms));
                String firstName=StringUtils.isEmpty(sysAccount.getSysAccountFirstName())?"":sysAccount.getSysAccountFirstName();
                String lastName=StringUtils.isEmpty(sysAccount.getSysAccountLastName())?"":sysAccount.getSysAccountLastName();
                String name = firstName+lastName;
                job.setSysAccountName(name);
                job.setUserId(getLoginUser().getUserId());
                myJobMapper.insertMyJob(job);
                jobIds.put(sysAccount.getSysAccountId(),job.getJobId());
                int index =1;
                List<MyJobDetail> myJobDetails= new ArrayList<>();
                for (String s1 : list) {
                        MyJobDetail jobDetail = new MyJobDetail();
                        long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                        jobDetail.setJobId(jobId);
                        jobDetail.setJobDetailDate(DateUtils.getNowDate());
                        jobDetail.setJobDetailStatus(-1);
                        jobDetail.setTaskClass("com.ruoyi.system.runnable.addContactRunnable");
                        jobDetail.setIndex(index);
                        jobDetail.setTarg(s1);
                        myJobDetails.add(jobDetail);
                        index++;
                }
                List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
                partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
            }
        }else{
            for (String s1 : list) {
            for (SysAccount sysAccount : sysAccounts) {
                HashMap parms = new HashMap();
                parms.put("sessionPath", sysAccount.getSysAccountStringSession());
                parms.put("userName", s1);
                String result = tgUtil.GenerateCommand("addContact", parms);
                Map map = JSON.parseObject(result, Map.class);
                if (map.get("code").equals("304")) {
                    return new AjaxResult(304, "账号失效");
                }else if (map.get("code").equals("200")){
                    JSONObject jsonObject = (JSONObject) map.get("msg");
                    SysContact sysContact = new SysContact();
                    sysContact.setSysStatus(Long.parseLong((String) jsonObject.get("stutas")));
                    String firstName=StringUtils.isEmpty((String) map.get("firstname"))? "" :(String) map.get("firstname");
                    String lastName=StringUtils.isEmpty((String) map.get("lastname"))? "" :(String) map.get("lastname");
                    String name = firstName+lastName;
                    sysContact.setSysContactName(name);
                    sysContact.setSysContactUserName((String) jsonObject.get("sysContactUserName"));
                    sysContact.setSysMutualContact(Long.parseLong((String) jsonObject.get("sysMutualContact")));
                    sysContact.setSysUserId(getLoginUser().getUserId());
                    sysContact.setSysContactPhone((String) jsonObject.get("contactNumber"));
                    sysContact.setSysContactId(Long.parseLong((String) jsonObject.get("contactId")));
                    sysContact.setSysAccountId(sysAccount.getSysAccountId());
                    sysContactMapper.insertSysContact(sysContact);
                }else{
                    return new AjaxResult(400, (String) map.get("msg"));
                }
            }
            }
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult addContactTgPhone(SysContact Contact) throws InterruptedException, IOException {
        String userNameAdd = Contact.getUserNameAdd();
        String[] split = userNameAdd.split("\\\n");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            String[] split1 = s.split(",");
            for (String s1 : split1) {
                list.add(s1);
            }
        }
        HashMap jobIds = new HashMap();
        long date = System.currentTimeMillis();
        List<String> sysAccountIds = Contact.getSysAccountIds();
        List<SysAccount> sysAccounts = sysAccountMapper.selectByStrings(sysAccountIds);
        boolean jobFlag=list.size()>1;
        if(jobFlag){
            for (SysAccount sysAccount : sysAccounts) {
                HashMap parms = new HashMap();
                parms.put("sessionPath", sysAccount.getSysAccountStringSession());
                MyJob job = new MyJob();
                job.setTarNum(list.size());
                job.setJobType("1");
                job.setJobName("用户名添加好友");
                job.setJobClass("com.ruoyi.system.runnable.addContactPhoneRunnable");
                job.setIntervals(3+"");
                job.setIntervalsUnit(0);
                job.setJobGroup(String.valueOf(date));
                job.setCreateDate(DateUtils.getNowDate());
                job.setParms(JSON.toJSONString(parms));
                String firstName=StringUtils.isEmpty(sysAccount.getSysAccountFirstName())?"":sysAccount.getSysAccountFirstName();
                String lastName=StringUtils.isEmpty(sysAccount.getSysAccountLastName())?"":sysAccount.getSysAccountLastName();
                String name = firstName+lastName;
                job.setSysAccountName(name);
                job.setUserId(getLoginUser().getUserId());
                myJobMapper.insertMyJob(job);
                jobIds.put(sysAccount.getSysAccountId(),job.getJobId());
                int index =1;
                List<MyJobDetail> myJobDetails= new ArrayList<>();
                for (String s1 : list) {

                    MyJobDetail jobDetail = new MyJobDetail();
                    long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                    jobDetail.setJobId(jobId);
                    jobDetail.setJobDetailDate(DateUtils.getNowDate());
                    jobDetail.setJobDetailStatus(-1);
                    jobDetail.setTaskClass("com.ruoyi.system.runnable.addContactPhoneRunnable");
                    jobDetail.setIndex(index);
                    jobDetail.setTarg(s1);
                    myJobDetails.add(jobDetail);
                    index++;
                }
                List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
                partition.forEach(e -> myJobMapper.batchMyJobDetail(e));

            }
        }else{
            for (String s1 : list) {
                for (SysAccount sysAccount : sysAccounts) {
                    HashMap parms = new HashMap();
                    parms.put("sessionPath", sysAccount.getSysAccountStringSession());
                    parms.put("userName", s1);
                    String result = tgUtil.GenerateCommand("addContact", parms);
                    Map map = JSON.parseObject(result, Map.class);
                    if (map.get("code").equals("304")) {
                        return new AjaxResult(304, "账号失效");
                    }else if (map.get("code").equals("200")){
                        JSONObject jsonObject = (JSONObject) map.get("msg");
                        SysContact sysContact = new SysContact();
                        sysContact.setSysStatus(Long.parseLong((String) jsonObject.get("stutas")));
                        String firstName=StringUtils.isEmpty((String) map.get("firstname"))? "" :(String) map.get("firstname");
                        String lastName=StringUtils.isEmpty((String) map.get("lastname"))? "" :(String) map.get("lastname");
                        String name = firstName+lastName;
                        sysContact.setSysContactName(name);
                        sysContact.setSysContactUserName((String) jsonObject.get("sysContactUserName"));
                        sysContact.setSysMutualContact(Long.parseLong((String) jsonObject.get("sysMutualContact")));
                        sysContact.setSysUserId(getLoginUser().getUserId());
                        sysContact.setSysContactPhone((String) jsonObject.get("contactNumber"));
                        sysContact.setSysContactId(Long.parseLong((String) jsonObject.get("contactId")));
                        sysContact.setSysAccountId(sysAccount.getSysAccountId());
                        sysContactMapper.insertSysContact(sysContact);
                    }else{
                        return new AjaxResult(400, "未知错误");
                    }
                }

            }
        }



        return AjaxResult.success();
    }


    /**
     * 新增
     *
     * @param sysContact
     * @return 结果
     */
    @Override
    public int insertSysContact(SysContact sysContact) {
        return sysContactMapper.insertSysContact(sysContact);
    }

    /**
     * 修改
     *
     * @param sysContact
     * @return 结果
     */
    @Override
    public int updateSysContact(SysContact sysContact) {
        return sysContactMapper.updateSysContact(sysContact);
    }

    /**
     * 批量删除
     *
     * @param sysContactIds 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteSysContactBySysContactIds(Long[] sysContactIds) {
        return sysContactMapper.deleteSysContactBySysContactIds(sysContactIds);
    }

    /**
     * 删除信息
     *
     * @param sysContactId 主键
     * @return 结果
     */
    @Override
    public int deleteSysContactBySysContactId(Long sysContactId) {
        return sysContactMapper.deleteSysContactBySysContactId(sysContactId);
    }
}
