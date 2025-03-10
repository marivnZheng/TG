package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.dto.TgLogin;
import com.ruoyi.system.mapper.SysAccountMapper;
import com.ruoyi.system.mapper.SysChromeUserMapper;
import com.ruoyi.system.mapper.SysContactMapper;
import com.ruoyi.system.mapper.SysGroupMapper;
import com.ruoyi.system.properties.TgProperties;
import com.ruoyi.system.service.ISysAccountDetailService;
import com.ruoyi.system.service.ISysAccountService;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
@Slf4j
@Service
public class SysAccountServiceImpl implements ISysAccountService 
{
    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private TGUtil tgUtil;

    @Autowired
    private  ISysAccountDetailService sysAccountDetailService;

    @Autowired
    private SysContactMapper sysContactMapper;

    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Resource
    private TgProperties tgProperties;


    @Autowired
    private SysChromeUserMapper sysChromeUserMapper;

    private ExecutorService executor = Executors.newCachedThreadPool() ;

    /**
     * 查询
     * 
     * @param sysAccountId 主键
     * @return
     */
    @Override
    public SysAccount selectSysAccountBySysAccountId(Long sysAccountId)
    {
        return sysAccountMapper.selectSysAccountBySysAccountId(sysAccountId);
    }

    /**
     * 查询列表
     * 
     * @param sysAccount
     * @return
     */
    @Override
    public List<SysAccount> selectSysAccountList(SysAccount sysAccount)
    {
        LoginUser loginUser = getLoginUser();
        sysAccount.setSysUserId(loginUser.getUserId());
        return sysAccountMapper.selectSysAccountList(sysAccount);
    }

    @Override
    public List<SysAccount> selectListOnline(SysAccount sysAccount) {
        sysAccount.setSysUserId(getLoginUser().getUserId());
        return sysAccountMapper.selectListOnline(sysAccount);
    }

    /**
     * 新增
     * 
     * @param sysAccount
     * @return 结果
     */
    @Override
    public int insertSysAccount(SysAccount sysAccount)
    {
        return sysAccountMapper.insertSysAccount(sysAccount);
    }

    /**
     * 修改
     * 
     * @param sysAccount
     * @return 结果
     */
    @Override
    public int updateSysAccount(SysAccount sysAccount)
    {
        return sysAccountMapper.updateSysAccount(sysAccount);
    }

    /**
     * 批量删除
     * 
     * @param sysAccountIds 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteSysAccountBySysAccountIds(Long[] sysAccountIds)
    {
        return sysAccountMapper.deleteSysAccountBySysAccountIds(sysAccountIds);
    }



    @Override
    public TgLogin sendPhoneCode(TgLogin tgLogin) throws InterruptedException, IOException {
        Map map = JSON.parseObject(JSON.toJSONString(tgLogin), Map.class);
        String sendPhoneCode = tgUtil.GenerateCommand("sendPhoneCode", (HashMap)map);
        return new TgLogin(sendPhoneCode,tgLogin.getPhoneNumber());
    }



    @Override
    public AjaxResult checkPhoneAndUserJurisdiction(String phoneNumber) {
        LoginUser loginUser = getLoginUser();
        SysAccountDetail sysAccountDetail = sysAccountDetailService.selectAccountDetailByLeverId(loginUser.getUser().getAccountDetailId());
        int i = sysAccountMapper.selectCountUserId(loginUser.getUserId());
        if(sysAccountDetail.getAccountNum()<i+1){
            String errorMsg= "最大只能登入"+sysAccountDetail.getAccountNum();
            return error(errorMsg);
        }
        List<SysAccount> sysAccounts = sysAccountMapper.selectSysAccountList(new SysAccount().setSysAccountPhone(phoneNumber));
        if(sysAccounts.size()>0){
            String errorMsg= "该账号已经登入其它用户";
            return  error(errorMsg);
        }
        return success();
    }
    public void syncGroup(HashMap parms,Long sysAccountId,SysUser user ){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    try {
                        log.info("当前账号为：{}，账号为：{}，开始同步群组。",user.getUserName(),sysAccountId);
                        sysGroupMapper.deleteSysContactByAccountId(sysAccountId);
                        String syncGroup = tgUtil.GenerateCommand("syncGroup", parms);
                        ArrayList groupList = new ArrayList();
                        for (String s : syncGroup.split("\n")) {
                            String substring = s.substring(s.indexOf("{"), s.indexOf("}")+1);
                            Map map = JSON.parseObject(tgUtil.decodeJson(substring), Map.class);
                            SysGroup group =new SysGroup();
                            group.setSysGroupLink((String) map.get("link"));
                            group.setSysGroupTitle((String) map.get("title"));
                            group.setSysGroupId(Long.valueOf((String) map.get("id")));
                            group.setSysGroupDetail(String.valueOf((Boolean)map.get("isGroup")));
                            group.setIsPrivate(Integer.valueOf((Boolean)map.get("isPrivate")?0:1));
                            group.setParticipantsCount(Long.valueOf((Integer) map.get("participantsCount")));
                            group.setSysGroupSendMessage(Long.valueOf((Boolean)map.get("sendMessages")?0:1));
                            group.setSysGroupSendPhoto(Long.valueOf((Boolean)map.get("sendPhotos")?0:1));
                            group.setSysGroupInvite(Long.valueOf((Boolean)map.get("inviteUsers")?0:1));
                            group.setSysUserId(user.getUserId());
                            group.setSysAccountStringSession((String) parms.get("sysAccountStringSession"));
                            group.setSysAccountId(sysAccountId);
                            groupList.add(group);
                        }
                        List<List<SysGroup>> partition = ListUtil.partition(groupList, 100);
                        partition.forEach(item -> sysGroupMapper.batchSysGroup(item));
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                    }
            }
        };
        executor.execute(runnable);
    }

 public void syncContact(HashMap parms,Long sysAccountId,SysUser user ){

     Runnable runnable = new Runnable() {
         @Override
         public void run() {
                 log.info("当前账号为：{}，账号为：{}，开始同步好友。",user.getUserName(),sysAccountId);
                 sysContactMapper.deleteSysContactByAccountId(sysAccountId);
                 try {
                     String syncContact = tgUtil.GenerateCommand("syncContact", parms);
                     ArrayList contactList = new ArrayList();
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
                         sysContact.setSysUserId(user.getUserId());
                         sysContact.setSysContactPhone((String) map.get("contactNumberr"));
                         sysContact.setSysContactId(Long.parseLong((String) map.get("contactId")));
                         sysContact.setSysAccountId(sysAccountId);
                         contactList.add(sysContact);
                     }
                     List<List<SysContact>> partition = ListUtil.partition(contactList, 100);
                     partition.forEach(item -> sysContactMapper.batchInsertSysContact(item));
                 } catch (InterruptedException | IOException ex) {
                     ex.printStackTrace();
                 }

         }
     };
     executor.execute(runnable);
 }


    @Override
    public AjaxResult loginAccountByPhoneCode(TgLogin tgLogin) throws InterruptedException, IOException {
        //使用验证码登入
        Map map = JSON.parseObject(JSON.toJSONString(tgLogin), Map.class);
        if(StringUtils.isEmpty(tgLogin.getPassword())){
            String loginByPhone = tgUtil.GenerateCommand("loginByPhone",(HashMap) map);

           Map resultMap =(Map)JSON.parse(loginByPhone);
           if(StringUtils.equals("200",String.valueOf(resultMap.get("code")))){
               SysAccount sysAccount = new SysAccount();
               JSONObject msg = (JSONObject) resultMap.get("msg");
               Integer concatNumber =(Integer) msg.get("concatNumber");
               Integer groupNumber = (Integer) msg.get("groupNumber");
               sysAccount.setSysAccountConcatsNumber(concatNumber.longValue());
               sysAccount.setSysAccountGroupNumber(groupNumber.longValue());
               sysAccount.setSysAccountPhone((String) msg.get("phoneNumber"));
               sysAccount.setSysAccountName((String)msg.get("username"));
               sysAccount.setSysAccountSessionFile((String)msg.get("sessionFilePath"));
               sysAccount.setSysAccountStringSession((String)msg.get("sessionString"));
               sysAccount.setSysAccountFirstName((String)msg.get("firstname"));
               sysAccount.setSysAccountLastName((String)msg.get("lastname"));
               sysAccount.setSysAccountCreateTime(DateUtils.getNowDate());
               sysAccount.setSysAccountCreateTimezone(ZoneId.systemDefault().toString());
               sysAccount.setSysUserId(getLoginUser().getUserId());
               sysAccount.setAppHash(tgProperties.getAppHash());
               sysAccount.setAppId(tgProperties.getAppId());
               sysAccount.setAppId(tgLogin.getAppId());
               sysAccountMapper.insertSysAccount(sysAccount);
               //开启线程同步好友及群聊

               HashMap parms = new HashMap();
               parms.put("sysAccountStringSession", (String)msg.get("sessionString"));
               parms.put("appId",tgLogin.getAppId());
               parms.put("appHash",tgLogin.getAppHash());
               SysUser user = getLoginUser().getUser();
               syncContact(parms,sysAccount.getSysAccountId(),user);
               syncGroup(parms,sysAccount.getSysAccountId(),user);

               return success();




           }else  if(StringUtils.equals("400",String.valueOf(resultMap.get("code")))){

               return  new AjaxResult(400,"需要输入二次验证密码",null);
           }else{
               return new AjaxResult(500,"未知错误",null);
           }
        }else{
            String loginByPhone = tgUtil.GenerateCommand("loginByPhoneAndPassword",(HashMap) map);
            Map loginEntity = JSON.parseObject(loginByPhone, Map.class);
            if(loginEntity.get("code").equals("400")){
                return new AjaxResult(400,(String) loginEntity.get("msg"));
            }
            if(loginEntity.get("code").equals("200")){
                SysAccount sysAccount = new SysAccount();
                JSONObject msg = (JSONObject) loginEntity.get("msg");
                Integer concatNumber =(Integer) msg.get("concatNumber");
                Integer groupNumber = (Integer) msg.get("groupNumber");
                sysAccount.setSysAccountConcatsNumber(concatNumber.longValue());
                sysAccount.setSysAccountGroupNumber(groupNumber.longValue());
                sysAccount.setSysAccountPhone((String) msg.get("phoneNumber"));
                sysAccount.setSysAccountName((String)msg.get("username"));
                sysAccount.setSysAccountSessionFile((String)msg.get("sessionFilePath"));
                sysAccount.setSysAccountStringSession((String)msg.get("sessionString"));
                sysAccount.setSysAccountFirstName((String)msg.get("firstname"));
                sysAccount.setSysAccountLastName((String)msg.get("lastname"));
                sysAccount.setSysAccountCreateTime(DateUtils.getNowDate());
                sysAccount.setSysAccountCreateTimezone(ZoneId.systemDefault().toString());
                sysAccount.setSysUserId(getLoginUser().getUserId());
                sysAccount.setAppHash(tgProperties.getAppHash());
                sysAccount.setAppId(tgProperties.getAppId());
                sysAccountMapper.insertSysAccount(sysAccount);

                HashMap parms = new HashMap();
                parms.put("sysAccountStringSession", (String)msg.get("sessionString"));
                parms.put("appId",tgLogin.getAppId());
                parms.put("appHash",tgLogin.getAppHash());
                SysUser user = getLoginUser().getUser();
                syncContact(parms,sysAccount.getSysAccountId(),user);
                syncGroup(parms,sysAccount.getSysAccountId(),user);

                return success();
            }else{
                return  new AjaxResult(400,"未知错误");
            }


        }
    }

    @Override
    public AjaxResult sessionFileUpload(MultipartFile file)  {
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            String executablePath = System.getProperty("user.dir");
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            inputStream =file.getInputStream();
            File tempFile =new File(executablePath+"/"+file.getOriginalFilename());
            if(tempFile.exists()){
                tempFile.delete();
                while(tempFile.exists()){
                    Thread.sleep(100);
                }
            }
            os = new FileOutputStream(tempFile.getPath());
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            return  success();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return error(e.getMessage());
            }
        }
        return success();
    }

    @Override
    public AjaxResult syncAccount(List<SysAccount> list) throws InterruptedException, IOException {
        for (SysAccount sysAccount : list) {
            HashMap parms = new HashMap();
            parms.put("sysAccountStringSession",sysAccount.getSysAccountStringSession());
            parms.put("appId",sysAccount.getAppId());
            parms.put("appHash",sysAccount.getAppHash());
            SysUser user = getLoginUser().getUser();

            String loginBySessionFile=  tgUtil.GenerateCommand("syncAccount", parms);
                String substring = loginBySessionFile.substring(loginBySessionFile.indexOf("{"), loginBySessionFile.indexOf("}")+1);
                Map map = JSON.parseObject(tgUtil.decodeJson(substring), Map.class);
                if(map.containsKey("code")&&StringUtils.equals(map.get("code").toString(),"444")){
                        // the use is delete
                    sysAccount.setSysAccountLoginStatus(1L);
                    sysAccountMapper.updateSysAccount(sysAccount);
                    return new AjaxResult(444,"该账号已经封禁");
                }
                Integer concatNumber =(Integer) map.get("concatNumber");
                Integer groupNumber = (Integer) map.get("groupNumber");
                sysAccount.setSysAccountConcatsNumber(concatNumber.longValue());
                sysAccount.setSysAccountGroupNumber(groupNumber.longValue());
                sysAccount.setSysAccountPhone((String) map.get("phoneNumber"));
                sysAccount.setSysAccountName((String)map.get("username"));
                sysAccount.setSysAccountSessionFile((String)map.get("sessionFilePath"));
                sysAccount.setSysAccountStringSession((String)map.get("sessionString"));
                sysAccount.setSysAccountCreateTime(DateUtils.getNowDate());
                sysAccount.setSysAccountCreateTimezone(ZoneId.systemDefault().toString());
                sysAccount.setSysUserId(getLoginUser().getUserId());
                sysAccount.setSysAccountFirstName((String)map.get("firstname"));
                sysAccount.setSysAccountLastName((String)map.get("lastname"));
                sysAccount.setSysAccountAbout((String)map.get("about"));
                sysAccountMapper.updateSysAccount(sysAccount);
                syncContact(parms,sysAccount.getSysAccountId(),user);
                syncGroup(parms,sysAccount.getSysAccountId(),user);

        }

         return success();
    }

    @Override
    public AjaxResult sessionFileLogin(String jsonStr) throws InterruptedException, IOException {
        JSONArray objects = JSON.parseArray(jsonStr);
        SysAccountDetail sysAccountDetail = sysAccountDetailService.selectAccountDetailByLeverId(getLoginUser().getUser().getAccountDetailId());
        log.info("登入用户为：{}，最大登录数量为：{}",getLoginUser().getUser().getUserId(),sysAccountDetail.getAccountNum());
        int num = sysAccountMapper.selectCountUserId(getLoginUser().getUser().getUserId());
        if(sysAccountDetail.getAccountNum()<num+objects.size()){
            String errorMsg= "最大只能登入"+sysAccountDetail.getAccountNum();
            return error(errorMsg);
        }
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            String name = jsonObject.getString("fileName");
            HashMap map = new HashMap();
            map.put("fileName",name);
            String loginBySessionFile = tgUtil.GenerateCommand("loginBySessionFile", map);
            Map loginEntity = JSON.parseObject(loginBySessionFile, Map.class);
            if(loginEntity==null){
                return new AjaxResult(400,"文件有误，无法登录");
            }
            if(loginEntity.get("code").equals("200")){
                JSONObject res = (JSONObject) loginEntity.get("msg");
                SysAccount sysAccount = new SysAccount();
                Integer concatNumber =(Integer) res.get("concatNumber");
                Integer groupNumber = (Integer) res.get("groupNumber");
                sysAccount.setSysAccountConcatsNumber(concatNumber.longValue());
                sysAccount.setSysAccountGroupNumber(groupNumber.longValue());
                sysAccount.setSysAccountPhone((String) res.get("phoneNumber"));
                sysAccount.setSysAccountName((String)res.get("username"));
                sysAccount.setSysAccountSessionFile((String)res.get("sessionFilePath"));
                sysAccount.setSysAccountStringSession((String)res.get("sessionString"));
                sysAccount.setSysAccountCreateTime(DateUtils.getNowDate());
                sysAccount.setSysAccountCreateTimezone(ZoneId.systemDefault().toString());
                sysAccount.setSysUserId(getLoginUser().getUserId());
                sysAccount.setSysAccountFirstName((String)res.get("firstname"));
                sysAccount.setSysAccountLastName((String)res.get("lastname"));
                sysAccount.setAppHash(tgProperties.getAppHash());
                sysAccount.setAppId(tgProperties.getAppId());
                sysAccountMapper.insertSysAccount(sysAccount);
            }else if(loginEntity.get("code").equals("304")){
                return new AjaxResult(304,"文件已经失效");
            }

        }
        return success();
    }

    @Override
    public AjaxResult updateListAccountOnline(List<SysAccount> list) throws InterruptedException, IOException {
        for (SysAccount sysAccount : list) {
            sysAccountMapper.updateSysAccount(sysAccount);
        }
        return success();
    }

    @Override
    public AjaxResult editAccountDetail(SysAccount sysAccount) throws InterruptedException, IOException {
        HashMap parms =new HashMap();
        parms.put("about",sysAccount.getSysAccountAbout());
        parms.put("lastName",sysAccount.getSysAccountLastName());
        parms.put("firstName",sysAccount.getSysAccountFirstName());
        parms.put("userName",sysAccount.getSysAccountName());
        parms.put("sessionString",sysAccount.getSysAccountStringSession());
        String updateProFile = tgUtil.GenerateCommand("updateProFile", parms);
        Map map = JSON.parseObject(updateProFile, Map.class);
        if(map.get("code").equals("200")){
            sysAccountMapper.updateSysAccountProFile(sysAccount);
        }else{
            return new AjaxResult(400,(String) map.get("msg"));
        }
        String updateUserName = tgUtil.GenerateCommand("updateUserName", parms);
        Map map1 = JSON.parseObject(updateUserName, Map.class);
        if(map1.get("code").equals("200")){
            sysAccountMapper.updateSysAccountProFile(sysAccount);
            return success();
        }else{
            return new AjaxResult(400,(String) map.get("msg"));
        }
    }

}
