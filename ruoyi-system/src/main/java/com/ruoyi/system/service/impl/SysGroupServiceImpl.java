package com.ruoyi.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.ruoyi.system.runnable.InvoteGroupRunnable;
import com.ruoyi.system.runnable.JoinGroupRunnable;
import com.ruoyi.system.runnable.addContactRunnable;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysGroupMapper;
import com.ruoyi.system.domain.SysGroup;
import com.ruoyi.system.service.ISysGroupService;

import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-14
 */
@Service
@Slf4j
public class SysGroupServiceImpl implements ISysGroupService 
{
    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private MyJobMapper myJobMapper;

    @Autowired
    private SysContactMapper sysContactMapper;

    @Autowired
    private TGUtil tgUtil;


    /**
     * 查询
     * 
     * @param sysGroupId 主键
     * @return 
     */
    @Override
    public SysGroup selectSysGroupBySysGroupId(Long sysGroupId)
    {
        return sysGroupMapper.selectSysGroupBySysGroupId(sysGroupId);
    }

    /**
     * 查询列表
     * 
     * @param sysGroup 
     * @return 
     */
    @Override
    public List<SysGroup> selectSysGroupList(SysGroup sysGroup)
    {
        sysGroup.setSysUserId(getLoginUser().getUserId());
        return sysGroupMapper.selectSysGroupList(sysGroup);
    }

    /**
     * 新增
     * 
     * @param sysGroup 
     * @return 结果
     */
    @Override
    public int insertSysGroup(SysGroup sysGroup)
    {
        return sysGroupMapper.insertSysGroup(sysGroup);
    }

    /**
     * 修改
     * 
     * @param sysGroup 
     * @return 结果
     */
    @Override
    public int updateSysGroup(SysGroup sysGroup)
    {
        return sysGroupMapper.updateSysGroup(sysGroup);
    }

    /**
     * 批量删除
     * 
     * @param sysGroupIds 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteSysGroupBySysGroupIds(Long[] sysGroupIds)
    {
        return sysGroupMapper.deleteSysGroupBySysGroupIds(sysGroupIds);
    }

    /**
     * 删除信息
     * 
     * @param sysGroupId 主键
     * @return 结果
     */
    @Override
    public int deleteSysGroupBySysGroupId(Long sysGroupId)
    {
        return sysGroupMapper.deleteSysGroupBySysGroupId(sysGroupId);
    }

    @Override
    public AjaxResult syncGroup(List<SysAccount> accountList) {
        accountList.forEach(e->{
            String sysAccountStringSession = e.getSysAccountStringSession();
            Long sysAccountId = e.getSysAccountId();
            HashMap parms = new HashMap();
            parms.put("sysAccountStringSession",sysAccountStringSession);
            try {
                log.info("当前账号为：{}，账号为：{}，开始同步好友。",getLoginUser().getUsername(),e.getSysAccountId());
                sysGroupMapper.deleteSysContactByAccountId(e.getSysAccountId());
                String syncGroup = tgUtil.GenerateCommand("syncGroup", parms);
                for (String s : syncGroup.split("\n")) {
                    String substring = s.substring(s.indexOf("{"), s.indexOf("}")+1);
                    Map map = JSON.parseObject(tgUtil.decodeJson(substring), Map.class);
                    SysGroup group =new SysGroup();
                    group.setSysGroupLink((String) map.get("link"));
                    group.setSysGroupTitle((String) map.get("title"));
                    group.setSysGroupId(Long.valueOf((String) map.get("id")));
                    group.setSysGroupDetail(String.valueOf((Boolean)map.get("isGroup")));
                    group.setSysGroupSendMessage(Long.valueOf((Boolean)map.get("sendMessages")?0:1));
                    group.setSysGroupSendPhoto(Long.valueOf((Boolean)map.get("sendPhotos")?0:1));
                    group.setSysGroupInvite(Long.valueOf((Boolean)map.get("inviteUsers")?0:1));
                    group.setSysUserId(getLoginUser().getUserId());
                    group.setSysAccountStringSession(sysAccountStringSession);
                    group.setSysAccountId(sysAccountId);
                    sysGroupMapper.insertSysGroup(group);
                }
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public AjaxResult inviteGroup(HashMap map) throws InterruptedException, IOException {
        Integer groupId= (Integer) map.get("groupId");
        List<HashMap> contactList = (List<HashMap>) map.get("concatsList");
        String sysAccountId = map.get("sysAccountId").toString();
        String min = map.get("min").toString();
        Integer minCount = (Integer) map.get("minCount");
        int tarNum=contactList.size();
        long date = System.currentTimeMillis();
        boolean jobFlag=tarNum>1;
        HashMap jobIds = new HashMap();
        SysAccount sysAccount = sysAccountMapper.selectSysAccountBySysAccountId(Long.valueOf(sysAccountId));
        SysGroup sysGroup = sysGroupMapper.selectSysGroupBySysGroupId(Long.valueOf(groupId));
        if(jobFlag){
            HashMap parms = new HashMap();
            parms.put("sessionString", sysAccount.getSysAccountStringSession());
            parms.put("channelId", groupId);
            MyJob job = new MyJob();
            job.setTarNum(contactList.size());
            job.setJobType("1");
            job.setJobName("邀请进群"+sysGroup.getSysGroupTitle());
            job.setJobClass("com.ruoyi.system.runnable.InvoteGroupRunnable");
            job.setIntervals(minCount+"");
            job.setIntervalsUnit(Integer.valueOf(min));
            job.setJobGroup(String.valueOf(date));
            job.setCreateDate(DateUtils.getNowDate());
            job.setParms(JSON.toJSONString(parms));
            String firstName= StringUtils.isEmpty(sysAccount.getSysAccountFirstName())?"":sysAccount.getSysAccountFirstName();
            String lastName=StringUtils.isEmpty(sysAccount.getSysAccountLastName())?"":sysAccount.getSysAccountLastName();
            String name = firstName+lastName;
            job.setSysAccountName(name);
            job.setUserId(getLoginUser().getUserId());
            myJobMapper.insertMyJob(job);
            jobIds.put(sysAccount.getSysAccountId(),job.getJobId());
            int index =1;
            List<MyJobDetail> myJobDetails= new ArrayList<>();
            for (Map s1 : contactList) {

                MyJobDetail jobDetail = new MyJobDetail();
                long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                jobDetail.setJobId(jobId);
                jobDetail.setJobDetailDate(DateUtils.getNowDate());
                jobDetail.setJobDetailStatus(-1);
                jobDetail.setTaskClass("com.ruoyi.system.runnable.InvoteGroupRunnable");
                jobDetail.setIndex(index);
                jobDetail.setTarg((String) s1.get("sysContactUserName"));
                myJobDetails.add(jobDetail);
                index++;
            }
            List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
            partition.forEach(e -> myJobMapper.batchMyJobDetail(e));

        }else{
            for (Map s1 : contactList) {
                HashMap parms = new HashMap();
                parms.put("sessionString", sysAccount.getSysAccountStringSession());
                parms.put("channelId",groupId);
                parms.put("sysContactUserName",s1.get("sysContactUserName"));
                String result = tgUtil.GenerateCommand("InvoteGroup", parms);
                Map resultMap = JSON.parseObject(result, Map.class);
                if (resultMap.get("code").equals("304")) {
                    return new AjaxResult(304, "账号失效");
                }else if (resultMap.get("code").equals("200")){
                    return success();
                }else{
                    return new AjaxResult(400, (String) resultMap.get("msg"));
                }

            }
        }
        return success();
    }

    @Override
    public AjaxResult inviteGroupList(HashMap map) {
        List<String> accountList = (List<String>) map.get("accountList");
        String groupLink = (String) map.get("groupLink");
        Integer onceTime =Integer.valueOf((String)map.get("onceTime"));
        Integer loopTime =  Integer.valueOf((String) map.get("loopTime"));
        List<SysAccount> sysAccounts = sysAccountMapper.selectByStrings(accountList);
        ExecutorService executor = Executors.newCachedThreadPool() ;
        JoinGroupRunnable runnable = new JoinGroupRunnable(sysAccounts,groupLink,onceTime,loopTime,myJobMapper,getLoginUser().getUserId());
        executor.execute(runnable);
        return success();
    }

    @Override
    public AjaxResult addConcats(HashMap map) throws InterruptedException, IOException {
        List<HashMap> contactList = (List<HashMap>) map.get("concatsList");
        String sysAccountId = map.get("sysAccountId").toString();
        String min = map.get("min").toString();
        Integer minCount = (Integer) map.get("minCount");
        int tarNum=contactList.size();
        long date = System.currentTimeMillis();
        boolean jobFlag=tarNum>1;
        HashMap jobIds = new HashMap();
        SysAccount sysAccount = sysAccountMapper.selectSysAccountBySysAccountId(Long.valueOf(sysAccountId));
        if(jobFlag){
            HashMap parms = new HashMap();
            parms.put("sessionPath", sysAccount.getSysAccountStringSession());
            MyJob job = new MyJob();
            job.setTarNum(contactList.size());
            job.setJobType("1");
            job.setJobName("用户名添加好友");
            job.setJobClass("com.ruoyi.system.runnable.addContactRunnable");
            job.setIntervals(minCount+"");
            job.setIntervalsUnit(Integer.valueOf(min));
            job.setJobGroup(String.valueOf(date));
            job.setCreateDate(DateUtils.getNowDate());
            job.setParms(JSON.toJSONString(parms));
            String firstName= StringUtils.isEmpty(sysAccount.getSysAccountFirstName())?"":sysAccount.getSysAccountFirstName();
            String lastName=StringUtils.isEmpty(sysAccount.getSysAccountLastName())?"":sysAccount.getSysAccountLastName();
            String name = firstName+lastName;
            job.setSysAccountName(name);
            job.setUserId(getLoginUser().getUserId());
            myJobMapper.insertMyJob(job);
            jobIds.put(sysAccount.getSysAccountId(),job.getJobId());
            int index =1;
            List<MyJobDetail> myJobDetails= new ArrayList<>();
            for (Map s1 : contactList) {

                MyJobDetail jobDetail = new MyJobDetail();
                long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                jobDetail.setJobId(jobId);
                jobDetail.setJobDetailDate(DateUtils.getNowDate());
                jobDetail.setJobDetailStatus(-1);
                jobDetail.setTaskClass("com.ruoyi.system.runnable.addContactRunnable");
                jobDetail.setIndex(index);
                jobDetail.setTarg((String) s1.get("sysContactUserName"));
                myJobDetails.add(jobDetail);
                index++;
            }
            List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
            partition.forEach(e -> myJobMapper.batchMyJobDetail(e));

        }else{
            for (Map s1 : contactList) {
                    HashMap parms = new HashMap();
                    parms.put("sessionPath", sysAccount.getSysAccountStringSession());
                    parms.put("userName", s1);
                    String result = tgUtil.GenerateCommand("addContact", parms);
                    Map resultMap = JSON.parseObject(result, Map.class);
                    if (resultMap.get("code").equals("304")) {
                        return new AjaxResult(304, "账号失效");
                    }else if (resultMap.get("code").equals("200")){
                        JSONObject jsonObject = (JSONObject) map.get("msg");
                        SysContact sysContact = new SysContact();
                        sysContact.setSysStatus(Long.parseLong((String) jsonObject.get("stutas")));
                        String firstName=StringUtils.isEmpty((String) resultMap.get("firstname"))? "" :(String) resultMap.get("firstname");
                        String lastName=StringUtils.isEmpty((String) resultMap.get("lastname"))? "" :(String) resultMap.get("lastname");
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
                        return new AjaxResult(400, (String) resultMap.get("msg"));
                    }

            }
        }
        return success();
    }

    @Override
    public List<SysContact> getGroupMember(SysGroup sysGroup) {
        String stringSession  =sysGroup.getSysAccountStringSession();
        String sysGroupId = sysGroup.getSysGroupLink();
        HashMap parms = new HashMap();
        parms.put("sysGroupId","https://t.me/"+sysGroupId);
        parms.put("sysAccountStringSession",stringSession);
        List<SysContact> sysContactList= new ArrayList<>();
        try {
            String getGroupMember = tgUtil.GenerateCommand("getGroupMember", parms);
            if(JSON.isValid(getGroupMember)){
                    return null;
            }
            for (String s : getGroupMember.split("\n")) {
                String substring = s.substring(s.indexOf("{"), s.lastIndexOf("}")+1);
                Map map = JSON.parseObject(tgUtil.decodeJson(substring).replace("\\\\\"", "\\\""), Map.class);
                SysContact sysContact = new SysContact();
                sysContact.setSysStatus(Long.parseLong((String) map.get("stutas")));
                sysContact.setSysContactName(map.get("firstname")+" "+map.get("lastname"));
                sysContact.setSysContactUserName((String) map.get("sysContactUserName"));
                sysContact.setSysMutualContact(Long.parseLong((String) map.get("sysMutualContact")));
                sysContact.setSysUserId(getLoginUser().getUserId());
                sysContact.setSysContactPhone((String) map.get("concatNumber"));
                sysContact.setSysContactId(Long.parseLong((String) map.get("contactId")));
                sysContact.setSysAccountId(Long.parseLong((String) map.get("contactId")));
                sysContact.setSysContactName(sysContact.getSysContactName().replaceAll("null","").replace("None",""));
                sysContactList.add(sysContact);

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return sysContactList;
    }
}
