package com.sl.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.itheima.auth.factory.AuthTemplateFactory;
import com.itheima.auth.sdk.AuthTemplate;
import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.sl.gateway.config.MyConfig;
import com.sl.gateway.properties.JwtProperties;
import com.sl.transport.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户端token拦截处理
 */
@Slf4j
@Component
public class CustomerTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements AuthFilter {

    @Resource
    private MyConfig myConfig;

    @Resource
    private JwtProperties jwtProperties;

    @Value("${role.manager}")
    private List<Long> managerRoleIds;

    @Override
    public GatewayFilter apply(Object config) {
        return new TokenGatewayFilter(this.myConfig, this);
    }

    @Override
    public AuthUserInfoDTO check(String token) {
        // 普通用户的token没有对接权限系统，需要自定实现
        return null;
    }

    @Override
    public Boolean auth(String token, AuthUserInfoDTO authUserInfoDTO, String path) {
        //普通用户不需要校验角色
        //获取AuthTemplate对象
        AuthTemplate authTemplate = AuthTemplateFactory.get(token);
        //查询该用户的角色
        List<Long> roleIds = authTemplate.opsForRole().findRoleByUserId(authUserInfoDTO.getUserId()).getData();
        //取交集，判断是否有交集即可判断出是否有权限
        Collection<Long> intersection = CollUtil.intersection(roleIds, this.managerRoleIds);
        return CollUtil.isNotEmpty(intersection);
    }

    @Override
    public String tokenHeaderName() {
        return Constants.GATEWAY.ACCESS_TOKEN;
    }
}
