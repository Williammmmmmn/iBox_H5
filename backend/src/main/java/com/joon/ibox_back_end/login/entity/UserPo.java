package com.joon.ibox_back_end.login.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: iBox_back_end
 * @description: 用户数据库对应字段
 * @author: Joonnn
 * @create: 2025-03-1111:15
 **/
@Data
public class UserPo {
    private Integer userId; // 用户ID，主键
    private String username; // 用户名，唯一
    private String password; // 密码，加密存储
    private String phoneNumber; // 手机号
    private String fullName; // 姓名
    private String identityId; // 身份证号码
    private String status; // 用户状态：active（激活）、inactive（禁用）、pending（待激活）
    private String role; // 用户角色：user（普通用户）、admin（管理员）
    private String avatar; // 头像URL
    private LocalDateTime createdAt; // 创建时间
    private Integer referrerId; // 用户推荐人ID，外键，关联推荐人
    private String referralCode; // 用户推荐码，唯一
    private Integer referralCount; // 已推荐人数
    private String walletAddress;//用户的钱包地址
    private String transactionPassword;//用户的交易密码
}
