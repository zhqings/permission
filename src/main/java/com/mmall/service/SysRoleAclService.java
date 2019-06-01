package com.mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.beans.LogType;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysLogMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.model.SysLogWithBLOBs;
import com.mmall.model.SysRoleAcl;
import com.mmall.util.IpUtil;
import com.mmall.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysLogService sysLogService;

    /*
     * create by zhang 2019/5/31
     * 通过角色索引，权限模块索引
     */
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
//        根据roleId来获取权限索引列表
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
//        如果传进行的索引和原有索引相同
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
//            移除相同的索引
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
//        更新索引
        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    /*
     * create by zhang 2019/5/31
     * 更新角色权限
     */
    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
//        删除原有权限
        sysRoleAclMapper.deleteByRoleId(roleId);
//        判空
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
//        封装角色权限信息
        for (Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
//        更新权限信息
        sysRoleAclMapper.batchInsert(roleAclList);
    }

    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
