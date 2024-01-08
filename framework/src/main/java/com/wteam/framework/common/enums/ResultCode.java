package com.wteam.framework.common.enums;

import cn.hutool.crypto.asymmetric.Sign;

/**
 * 返回状态码
 * 第一位 1:商品；2:用户；3:交易,4:促销,5:店铺,6:页面,7:设置,8:其他
 *
 * @author Chopper
 * @since 2020/4/8 1:36 下午
 */
public enum ResultCode {

    /**
     * 成功状态码
     */
    SUCCESS(200, "成功"),

    /**
     * 失败返回码
     */
    ERROR(400, "服务器繁忙，请稍后重试"),

    /**
     * 失败返回码
     */
    DEMO_SITE_EXCEPTION(4001, "演示站点禁止使用"),
    /**
     * 参数异常
     */
    PARAMS_ERROR(4002, "参数异常"),
    /**
     * 用户
     */

    USER_EDIT_SUCCESS(20001, "用户修改成功"),
    USER_NOT_EXIST(20002, "用户不存在"),
    USER_NOT_LOGIN(20003, "用户未登录"),
    USER_AUTH_EXPIRED(20004, "用户已退出，请重新登录"),
    USER_AUTHORITY_ERROR(20005, "权限不足"),
    USER_CONNECT_LOGIN_ERROR(20006, "未找到登录信息"),
    USER_EXIST(20008, "该用户名或手机号已被注册"),
    USER_PHONE_NOT_EXIST(20009, "手机号解密失败"),
    USER_PASSWORD_ERROR(20010, "密码不正确"),
    USER_ORIG_PASSWORD_ERROR(20010, "原密码不正确"),
    USER_NOT_PHONE(20011, "非当前用户的手机号"),
    USER_CONNECT_ERROR(20012, "联合第三方登录，授权信息错误"),
    USER_RECEIPT_REPEAT_ERROR(20013, "会员发票信息重复"),
    USER_RECEIPT_NOT_EXIST(20014, "会员发票信息不存在"),
    USER_EDIT_ERROR(20015, "用户修改失败"),
    USER_OLD_PASSWORD_ERROR(20016, "旧密码不正确"),
    USER_COLLECTION_EXIST(20017, "无法重复收藏"),
    USER_GRADE_IS_DEFAULT(20018, "会员等级为默认会员等级"),
    USER_NOT_BINDING(20020, "未绑定用户"),
    USER_AUTO_REGISTER_ERROR(20021, "自动注册失败,请稍后重试"),
    USER_OVERDUE_CONNECT_ERROR(20022, "授权信息已过期，请重新授权/登录"),
    USER_CONNECT_BANDING_ERROR(20023, "当前联合登陆方式，已绑定其他账号，需进行解绑操作"),
    USER_TEAM_NOT_REGISTER_ERROR(20024, "暂无战队信息，请先选择加入一支战队"),
    USER_POINTS_ERROR(20025, "用户积分不足"),
    USER_SIGN_ERROR(20026, "签到失败"),
    USER_COLLEGE_AUTH_ERROR(20027, "未进行高校认证"),
    PERMISSION_BEYOND_TEN(21005, "最多可以设置10个角色"),
    PERMISSION_NOT_ROLE(20028,"没有给用户添加角色"),
    PHONE_USED(20029,"该手机号码已经注册过"),
    PHONENUMBER_ERROR(20031,"手机号码有误!"),
    CODE_ERROR(20032,"手机验证码错误"),
    OLD_NEWPASSWORD_SAME(20033,"新密码和旧密码一样"),
    USERNAME_USED(20034,"用户名已经被使用"),
    /**
     * 微信
     */
    WECHAT_CODE_NOT_EXIST(1001, "微信登录code发送失败"),
    WECHAT_APPID_NOT_EXIST(1002, "微信登录appid未配置"),
    WECHAT_CONNECT_NOT_EXIST(1003, "微信登录配置为空"),
    WECHAT_LOGIN_ERROR(1004, "微信登录失败"),
    WECHAT_JSAPI_SIGN_ERROR(1005, "微信JsApi签名异常"),
    WECHAT_ERROR(1006, "微信接口异常"),
    WECHAT_MP_MESSAGE_TMPL_ERROR(1007, "未能获取到微信模版消息id"),

    /**
     * 支付
     */
    PAY_UN_WANTED(32000, "当前订单不需要付款，返回订单列表等待系统订单出库即可"),
    PAY_SUCCESS(32001, "支付成功"),
    PAY_INCONSISTENT_ERROR(32002, "付款金额和应付金额不一致"),
    PAY_DOUBLE_ERROR(32003, "订单已支付，不能再次进行支付"),
    PAY_CASHIER_ERROR(32004, "收银台信息获取错误"),
    PAY_ERROR(32005, "支付业务异常，请稍后重试"),
    PAY_BAN(32006, "当前订单不需要付款，请返回订单列表重新操作"),
    PAY_PARTIAL_ERROR(32007, "该订单已部分支付，请前往订单中心进行支付"),
    PAY_NOT_SUPPORT(32008, "支付配置参数异常"),
    PAY_CLIENT_TYPE_ERROR(32009, "错误的客户端"),
    PAY_POINT_ENOUGH(32010, "积分不足，不能兑换"),
    PAY_NOT_EXIST_ORDER(32011, "支付订单不存在"),
    CAN_NOT_RECHARGE_WALLET(32012, "不能使用余额进行充值"),
    QUERY_ORDER_ERROR(32013,"订单查询失败"),


    /**
     * 任务
     */
    SIGN_SUCCESS_NOW(10020, "今日已签到"),


    /**
     * 帖子
     */
    LIKE_FAILURE(50001, "点赞失败，服务器繁忙"),

    /**
     * 赛事
     */
    MATCH_NOT_EXIST(60001, "赛事不存在"),
    MATCH_START_TIME_ERROR(60002, "报名开始时间未开始"),
    MATCH_END_TIME_ERROR(60003, "报名已截止"),
    MATCH_TIME_ERROR(60004, "报名时间未安排"),

    /**
     *  战队
     */
    TEAM_PASSWORD_ERROR(70001,"战队密码无效"),
    TEAM_NOT_BELONG_ERROR(70002,"不属于战队成员"),
    TEAM_NOT_LEADER_ERROR(70002,"不属于战队队长，无权操作"),
    /**
     * 业务
     */
    ENT_NULL_WORKER(70005,"当前企业还没有员工呢"),

    /**
     * 设置
     */
    SETTING_NOT_TO_SET(70001, "该参数不需要设置"),

    /**
     * 验证码
     */
    VERIFICATION_SEND_SUCCESS(80201, "短信验证码,发送成功"),
    VERIFICATION_ERROR(80202, "验证失败"),
    VERIFICATION_CODE_INVALID(80204, "验证码已失效，请重新校验"),
    VERIFICATION_SMS_CHECKED_ERROR(80210, "短信验证码错误，请重新校验"),
    /**
     * OSS
     */
    OSS_NOT_EXIST(80101, "OSS未配置"),
    OSS_EXCEPTION_ERROR(80102, "文件上传失败，请稍后重试"),
    OSS_DELETE_ERROR(80103, "图片删除失败"),
    FILE_NOT_EXIST_ERROR(1011, "上传文件不能为空"),
    FILE_EXAMINED(80105,"素材已经审核完了"),
    FILE_LOG_EMPTY(80107,"暂无记录"),
    BUSINESSFILE_NULL(80106,"当前商家和商家下的企业还没有素材"),


    /**
     * tencent
     */
    QRCODE_NULL(11001,"无法识别"),




    /**
     * jetlinks
     *
     */
    PRODUCTS_NULL(90002,"暂无产品"),
    DEVICE_NULL(90002,"无此设备ID"),
    DEVICE_EXIST(90002,"此设备已存在"),
    JETLINKS_ERROR(90001,"连接超时"),
    DEVICE_EXISTS(90002,"无此设备ID"),

    ;

    private final Integer code;
    private final String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    }
