package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LockNftInstanceMapper {
    Instances findByInstanceId(Integer instanceId);

    int lockNftInstance(Instances nftInstance);

    void unlockExpiredNfts();

    int unlockNftInstance(Integer instanceId); // 确保这个方法存在

    List<Instances> findLockedByUser(String walletAddress);
}
