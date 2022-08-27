package com.guli.mall.member.dao;

import com.guli.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lyh
 * @email ${email}
 * @date 2022-08-14 18:49:20
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
