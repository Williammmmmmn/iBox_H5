package com.joon.ibox_back_end.login.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.PersonalPo;
import com.joon.ibox_back_end.commonEntity.po.Wallet;
import com.joon.ibox_back_end.utils.RandomUsernameGenerator;
import com.joon.ibox_back_end.login.mapper.UserLoginMapper;
import com.joon.ibox_back_end.login.service.UserLoginService;
import com.joon.ibox_back_end.wallet.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 * @program: iBox_back_end
 * @description: 用户登录实现类
 * @author: Joonnn
 * @create: 2025-03-1115:29
 **/
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private WalletMapper walletMapper;
    /**
     * 通过手机号查询用户
     * @param phone
     * @return
     */
    @Override
    public PersonalPo findByPhone(String phone) {
        PersonalPo user = userLoginMapper.findByPhone(phone);
        if(user != null){
            return user;
        }
        return null;
    }

    /**
     * 新增新用户
     * @param phone
     */
    @Transactional
    @Override
    public void saveNewUser(String phone) {
        // 1. 先插入用户（单独事务）
        PersonalPo user = createAndSaveUser(phone);

        // 2. 再插入钱包（当前事务）
        createWalletForUser(user);
    }

    // 单独事务插入用户
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PersonalPo createAndSaveUser(String phone) {
        PersonalPo user = new PersonalPo();
        user.setPhoneNumber(phone);
        user.setUsername(RandomUsernameGenerator.generateRandomUsername());
        user.setRole("user");
        user.setStatus("active");
        user.setAvatar("assets/images/z.jpg");
        user.setWalletAddress(generateWalletAddress());
        user.setPassword("123456"); // 默认密码
        userLoginMapper.saveNewUser(user);
        return user;
    }

    // 插入钱包
    private void createWalletForUser(PersonalPo user) {
        Wallet wallet = new Wallet();
        wallet.setWalletAddress(user.getWalletAddress());
        wallet.setUserId(user.getUserId());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("CNY");
        if (walletMapper.insertWallet(wallet) == 0) {
            throw new RuntimeException("钱包创建失败");
        }
    }


    /**
     * 生成随机的以太坊格式钱包地址
     * 格式: 0x + 40个十六进制字符 (共42位)
     */
    private String generateWalletAddress() {
        SecureRandom random = new SecureRandom();
        byte[] addressBytes = new byte[20]; // 20字节 = 40个十六进制字符
        random.nextBytes(addressBytes);

        StringBuilder sb = new StringBuilder("0x");
        for (byte b : addressBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
