package com.wteam.framework.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wteam.framework.common.mybatis.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//展示树形数据
@Data
public class ViewComment extends BaseEntity {

    /**
     *  baseEntity:
     *  String id评论id
     *  String creatorBy创建人
     *  Date createTime创建时间
     *  String updaterBy更新者
     *  Date updateTime更新时间
     *  deleteFlag 是否删除
     */

    /**
     * 所属帖子id
     */
    private Integer postId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论的父级id
     */
    private Integer parentId;
    /**
     * 评论的用户id
     */
    private Integer userId;
    /**
     * 评论点赞数量
     */
    private Integer like;
    /**
     * 排序 默认 1 正序，0 倒序
     */
    private Integer sort;
    /**
     * 回复列表
     * (遍历每条非回复评论，递归添加回复评论)
     */
    @TableField(exist = false)
    private List<ViewComment> children = new ArrayList<>();
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 用户昵称
     */
    private String nickName;

    @TableField(exist = false)
    private Boolean ifNotLike;



}