package com.wteam.framework.common.cache.lua.like;


import com.wteam.framework.common.cache.config.redis.RedisKeyConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//TODO: 用ZSET 存用户id 点赞数也不用另外存了

/**
 * 点赞箱
 */
@Repository
public class VoteBox {
    private final RedisTemplate<Object, Object> redisTemplate;

    private final DefaultRedisScript<Integer[]> likeScript;


    public VoteBox(RedisTemplate<Object, Object> redisTemplate, DefaultRedisScript<Integer[]> likeScript) {
        this.redisTemplate = redisTemplate;
        this.likeScript = likeScript;
    }


    /**
     *
     * @param userId 点赞人
     * @param commentId 评论id
     * @return 返回当前最新点赞数
     */
    public Integer[] likeComment(String userId, String commentId){
        //使用lua脚本
        List<String> list = new ArrayList<>();
        list.add(MessageFormat.format(RedisKeyConstants.COMMENT_USER_PATTERN,commentId));
        return redisTemplate.execute(likeScript, Collections.singletonList(list),userId,System.currentTimeMillis());
    }


    /**
     * 给帖子投票(点赞)，用户增加评价点赞记录，评价点赞次数+1.该操作是原子性、幂等性的。
     * @param userId 点赞人
     * @param postId 帖子id
     * @return 返回当前最新点赞数
     */
    //TODO :使用ZADD 添加
    public Integer[] likePost(String userId, String postId){
        //使用lua脚本
        List<String> list = new ArrayList<>();
//        list.add(MessageFormat.format(RedisKeyConstants.POST_USER_PATTERN, userId, postId));
        list.add(MessageFormat.format(RedisKeyConstants.POST_USER_PATTERN, postId));
        return redisTemplate.execute(likeScript, Collections.singletonList(list),userId,System.currentTimeMillis());
    }

//    /**
//     * @method
//     * @description 判断评论是否点赞
//     * @date: 2022/11/10 20:09
//     * @author: lsllsl
//     * @return * @return Integer[]
//     */
//    public Boolean checkLikePost(String userId, String postId){
//        //使用lua脚本
//        List<String> list = new ArrayList<>();
////        list.add(MessageFormat.format(RedisKeyConstants.POST_USER_PATTERN, userId, postId));
//        list.add(MessageFormat.format(RedisKeyConstants.POST_USER_PATTERN, postId));
//        return redisTemplate.execute(likeScript, Collections.singletonList(list),userId,System.currentTimeMillis());
//    }
}
