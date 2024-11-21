package com.example.chat.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.chat.dao.MessageDao;
import com.example.chat.pojo.Message;
import com.example.chat.service.impl.MessageService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/server/{username}")
@Component
public class WebSocketServer {
    private MessageService messageService = SpringContextHolder.getBean(MessageService.class);
    private static final Logger log = (Logger) LoggerFactory.getLogger(WebSocketServer.class);
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static final Map<String,Long> justTimeList = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username")String username){
        sessionMap.put(username,session);
        log.info("有新用户加入,username={},当前在线人数为：{}",username,sessionMap.size());
        getUserList();
    }
    @OnClose
    public void onClose(Session session,@PathParam("username") String username){
        sessionMap.remove(username);
        log.info("username={}的用户断开连接，当前在线人数：{}",username,sessionMap.size());
        getUserList();
    }

    @OnMessage
    public void onMessage(String message,Session session,@PathParam("username")String username){
        log.info("用户username={}发送了一条消息",username);
        JSONObject obj = JSONUtil.parseObj(message);
        String type = obj.getStr("type");
        String toUsername = obj.getStr("to");
        log.info("to={}",toUsername);
        String text = obj.getStr("message");
        Session toSession = null;


        //发送时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        JSONObject jsonObject = new JSONObject();
        //转换为json
        //添加时间显示功能
        String temp_a; //用于set传入
        if (type.equals("group")){
            temp_a = "group";
        }else{
            temp_a = username;
        }
        log.info("name={}",username);
        boolean showTime = getShowTime(temp_a);
        messageService.addMessage(new Message(username,toUsername,text,null,type,showTime));


        if (type.equals("single")){
            toSession = sessionMap.get(toUsername);
            if (toSession == null){
                log.info("发送失败，未找到用户username={}的session",toUsername);
                return;
            }
        }

        jsonObject.set("showTime",showTime);
        jsonObject.set("type",type);
        jsonObject.set("from",username);
        jsonObject.set("to",toUsername);
        jsonObject.set("time",time);
        jsonObject.set("message",text);
        switch (type){
            case "group":
                this.sendAllMessage(username,jsonObject.toString());
                log.info("{}发送了一条群组消息",username);
            break;
            case "single":
                this.sendMessage(toSession,jsonObject.toString());
                log.info("用户username={}的消息已转发给{}",username,toUsername);
            break;
        }

    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    private void sendMessage(Session toSession,String message) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     *  用于群组发送消息
     * @param from  发送人
     * @param message   发送消息
     */
    private void sendAllMessage(String from,String message){
        try{
            Set<Map.Entry<String, Session>> entries = sessionMap.entrySet();
            for (Map.Entry<String,Session> entry: entries){
                if (entry.getKey().equals(from)){
                    continue;
                }
                entry.getValue().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            log.error("发送消息失败");
            throw new RuntimeException(e);
        }
    }
    //发送全体广播
    private void sendAllMessage(String message){
        try{
            for (Session session : sessionMap.values()){
                log.info("服务端给客户端[{}]发送消息{}",session.getId(),message);
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            log.error("发送消息失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前在线成员的列表以及其对应的邮箱
     * {"users":[{"username":"114514@qq.com"},{"username":"1919810@qq.com"}]}
     */
    public void getUserList(){
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("type","system");
        result.set("count",sessionMap.size());
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            array.add(jsonObject);
        }
        sendAllMessage(JSONUtil.toJsonStr(result));
    }

    //用于返回一个时间差是否大于某个数,传入一个用于获取justTimeList的key值
    @Test
    public boolean getShowTime(String key){
        long nowTime = System.currentTimeMillis()/1000;
        long justTime;
        if (justTimeList.get(key) == null){
            justTimeList.put(key,nowTime);
            return true;
        }else{
            justTime = justTimeList.get(key);
            justTimeList.put(key,nowTime);
            return (nowTime - justTime > 1200);
        }

    }

}
