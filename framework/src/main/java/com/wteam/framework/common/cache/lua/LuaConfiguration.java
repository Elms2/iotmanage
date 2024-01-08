package com.wteam.framework.common.cache.lua;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
/**
 * Lua脚本
 */
@Configuration
public class LuaConfiguration {
    /**
     * [点赞获取取消点赞]脚本  lua_set_and_dec
     */
    @Bean(value = "likeScript")
    public DefaultRedisScript<Integer[]> likeScript() {
        DefaultRedisScript<Integer[]> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/lua_set_and_dec.lua")));
        redisScript.setResultType(Integer[].class);
        return redisScript;
    }

    /**
     * 判断是否点赞
     */
    @Bean(value = "checkLikeScript")
    public DefaultRedisScript<Boolean> checkLikeScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/lua_check.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

}
