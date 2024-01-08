/**
 * 
 */
package com.wteam.framework.common.cache.config.redis;

/**
 * @author chenhan
 */
public class RedisKeyConstants {

    public final static String COMMENT_USER_PATTERN                                      = "likecomment";

    public final static String COMMENT_SUM_PATTERN                                       = "comment";

    public final static String POST_USER_PATTERN                                      = "likepost";

    public final static String POST_SUM_PATTERN                                       = "post";

    //人员签到位图key,一个位图存一个用户一年的签到状态,以userSign为标识,后面的两个参数是今年的年份和用户的id
    public final static String USER_SIGN_IN                                           = "userSign:%d:%d";
    //人员补签key,一个Hash列表存用户一个月的补签状态,以userSign:retroactive为标识,后面的两个参数是当月的月份和用户的id
    public final static String USER_RETROACTIVE_SIGN_IN                               = "userSign:retroactive:%d:%d";
    //人员签到总天数key,以userSign:count为标识,后面的参数是用户的id
    public final static String USER_SIGN_IN_COUNT                                     = "userSign:count:%d";

}