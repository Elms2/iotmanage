package com.wteam.contorller;

import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.jetlinks.DashboardUtils;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.websocket.CustomSpringConfigurator;
import com.wteam.framework.common.websocket.MessageOperation;
import com.wteam.framework.common.websocket.MessageResultType;
import com.wteam.framework.common.websocket.MessageVO;
import com.wteam.framework.modules.entDevice.entity.dto.Dashboard;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.system.server.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liushuai
 */
@Component
@ServerEndpoint(value = "/wteam/webSocket/{accessToken}", configurator = CustomSpringConfigurator.class)
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketServer {
    /**
     * 消息服务
     */
//    private final ImMessageService imMessageService;
//
//    private final ImTalkService imTalkService;

    /**
     * 在线人数
     * PS 注意，只能单节点，如果多节点部署需要自行寻找方案
     */
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    @Autowired
    private DashboardUtils dashboardUtils;

    @Autowired
    private EntDevService entDevService;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("accessToken") String accessToken, Session session) {


        AuthUser authUser = UserContext.getAuthUser(accessToken);

        String sessionId = authUser.getId();
        //如果已有会话，则进行下线提醒。
        if (sessionPools.containsKey(sessionId)) {
            log.info("用户重复登陆，旧用户下线");
            Session oldSession = sessionPools.get(sessionId);
            sendMessage(oldSession, MessageVO.builder().messageResultType(MessageResultType.OFFLINE).result("用户异地登陆").build());
            try {
                oldSession.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sessionPools.put(sessionId, session);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("accessToken") String accessToken) {
        AuthUser authUser = UserContext.getAuthUser(accessToken);
        log.info("用户断开断开连接:{}", JSONUtil.toJsonStr(authUser));
        sessionPools.remove(authUser);
    }

    /**
     * 发送消息
     *
     * @param msg
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("accessToken") String accessToken, String msg) throws Exception {
        log.info("发送消息：{}", msg);
        MessageOperation messageOperation = JSON.parseObject(msg, MessageOperation.class);
        operation(accessToken, messageOperation);
    }

    /**
     * 操作
     *
     * @param accessToken
     * @param messageOperation
     */
    private void operation(String accessToken, MessageOperation messageOperation) throws Exception {

        AuthUser authUser = UserContext.getAuthUser(accessToken);
        switch (messageOperation.getOperationType()) {
            case PING:
                break;
            case MESSAGE:
                //保存消息
//                ImMessage imMessage = new ImMessage(messageOperation);
//                imMessageService.save(imMessage);
//                //修改最后消息信息
//                imTalkService.update(new LambdaUpdateWrapper<ImTalk>().eq(ImTalk::getId, messageOperation.getTalkId()).set(ImTalk::getLastTalkMessage, messageOperation.getContext())
//                        .set(ImTalk::getLastTalkTime, imMessage.getCreateTime())
//                        .set(ImTalk::getLastMessageType, imMessage.getMessageType()));
//                //发送消息
//                sendMessage(messageOperation.getTo(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                break;
            case READ:
//                if (!StringUtils.isEmpty(messageOperation.getContext())) {
//                    imMessageService.read(messageOperation.getTalkId(), accessToken);
//                }
                break;
            case UNREAD:
//                sendMessage(authUser.getId(), new MessageVO(MessageResultType.UN_READ, imMessageService.unReadMessages(accessToken)));
                break;
            case SERVER:
                Server server = new Server();
                server.copyTo();
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.SERVER,
                        server));
                break;
            case SAMEMONTH:
                JSONObject sameMonth = dashboardUtils.getSameMonth();
//                System.out.println(sameMonth.toJSONString());
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.SAMEMONTH,
                        sameMonth));
                break;
            case SAMEDAY:
                JSONObject sameDay = dashboardUtils.getSameDay();
//                System.out.println(sameDay.toJSONString());
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.SAMEDAY,
                        sameDay));
                break;
            case DEVICECOUNT:
                List<Dashboard> dashboards = entDevService.selectDeviceCount();
//                System.out.println( ResultUtil.data(dashboards).toString());
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.DEVICECOUNT,
                        ResultUtil.data(dashboards)));
                break;
            default:
                break;
        }
    }

    /**
     * 发送消息
     *
     * @param sessionId sessionId
     * @param message   消息对象
     */
    private void sendMessage(String sessionId, MessageVO message) {
        Session session = sessionPools.get(sessionId);
        sendMessage(session, message);
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息对象
     */
    private void sendMessage(Session session, MessageVO message) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(message, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * socket exception
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("socket异常: {}", session.getId(), throwable);
    }


}
