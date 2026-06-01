// 简单版
Page({
    data: {
        content: '',
        // 当前登录者信息
        login: {
            id: '2023',
            user: 'AI客服',
            avatar: 'https://img2.baidu.com/it/u=1581320883,910100018&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1686045994&t=9135844131fa04a7ad00686d88c76752'
        },
        // 聊天信息
        chatList: [
            {
                msgId: '2022',
                nickname: 'AI客服',
                avatar: 'https://img0.baidu.com/it/u=3997048646,1811313428&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1686045994&t=0e1a0c5e962337e74750fba219bb3b51',
                message: '有关于养老方面的问题，你都可以向我提问哦！',
                type: 'text',
                date: '05-06 11:21'
            }
        ],
    },
    onLoad() {
        // 读取本地存储的聊天记录
        var localChatList = wx.getStorageSync('chatList');
        if (localChatList) {
            this.setData({ chatList: localChatList });
        }
        this.scrollToBottom();
    },
    // 输入监听
    inputClick(e) {
        this.setData({
            content: e.detail.value
        })
    },
    // 发送监听
    sendClick() {
        var that = this;
        var list = this.data.chatList;
        // 获取当前时间
        var date = new Date();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hour = date.getHours();
        var minu = date.getMinutes();
        var now1 = month < 10 ? '0' + month : month;
        var now2 = day < 10 ? '0' + day : day;
        // 组装用户消息
        var userMsg = {
            msgId: this.data.login.id,
            nickname: this.data.login.user,
            avatar: this.data.login.avatar,
            message: this.data.content,
            type: 'text',
            date: now1 + '-' + now2 + ' ' + hour + ':' + minu
        };
        this.setData({
            chatList: list.concat(userMsg)
        }, () => {
            that.scrollToBottom();
            that.setData({
                content: ''
            });
            // 保存聊天记录到本地存储
            wx.setStorageSync('chatList', this.data.chatList);
        });
        // 发送请求到coze API
        wx.request({
            url: 'https://api.coze.cn/v3/chat',
            method: 'POST',
            header: {
                'Authorization': CONFIG.AUTHORIZATION,
                'Content-Type': 'application/json'
            },
            data: {
                "bot_id": CONFIG.BOT_ID,
                "user_id": "123",
                "stream": false,
                "auto_save_history": true,
                "additional_messages": [
                    {
                        "role": "user",
                        "content": this.data.content,
                        "content_type": "text"
                    }
                ]
            },
            success: function (res) {
                console.log('coze API响应:', res.data);
                var chatid = res.data.data.id;
                var conversation_id = res.data.data.conversation_id;
                var checkStatus = function() {
                    wx.request({
                        url: `https://api.coze.cn/v3/chat/retrieve?conversation_id=${conversation_id}&chat_id=${chatid}`,
                        method: 'GET',
                        header: {
                            'Authorization': CONFIG.AUTHORIZATION,
                            'Content-Type': 'application/json'
                        },
                        success: function (statusRes) {
                            var status = statusRes.data.data.status;
                            if (status == 'completed') {
                                wx.request({
                                    url: `https://api.coze.cn/v3/chat/message/list?chat_id=${chatid}&conversation_id=${conversation_id}`,
                                    method: 'GET',
                                    header: {
                                        'Authorization': CONFIG.AUTHORIZATION,
                                        'Content-Type': 'application/json'
                                    },
                                    data: {
                                        "bot_id": CONFIG.BOT_ID,
                                         "task_id": chatid
                                    },
                                    success: function (answerRes) {
                                        if (answerRes.data.code) {
                                            console.log("应答异常：", answerRes.data.msg);
                                        } else {
                                            var data = answerRes.data.data;
                                            for (var i = 0; i < data.length; i++) {
                                                var item = data[i];
                                                if (item.type === 'answer') {
                                                    var botMsg = {
                                                        msgId: 'bot',
                                                        nickname: 'AI客服',
                                                        avatar: 'https://img0.baidu.com/it/u=3997048646,1811313428&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1686045994&t=0e1a0c5e962337e74750fba219bb3b51',
                                                        message: item.content,
                                                        type: 'text',
                                                        date: now1 + '-' + now2 + ' ' + hour + ':' + minu
                                                    };
                                                    that.setData({
                                                        chatList: that.data.chatList.concat(botMsg)
                                                    }, () => {
                                                        that.scrollToBottom();
                                                        // 保存聊天记录到本地存储
                                                        wx.setStorageSync('chatList', that.data.chatList);
                                                    });
                                                } else if (item.type === 'follow_up') {
                                                    console.log("您可以参考如下方式提问：", item.content);
                                                }
                                            }
                                        }
                                    },
                                    fail: function (err) {
                                        console.error('获取答案请求失败:', err);
                                    }
                                });
                            } else {
                                console.log(`任务仍在处理中，状态: ${status}`);
                                setTimeout(checkStatus, 1000);
                            }
                        },
                        fail: function (err) {
                            console.error('检查状态请求失败:', err);
                        }
                    });
                };
                checkStatus();
            },
            fail: function (err) {
                console.error('请求coze API失败:', err);
            }
        });
    },
    // 滑动到最底部
    scrollToBottom() {
        setTimeout(() => {
            wx.pageScrollTo({
                scrollTop: 200000,
                duration: 3
            });
        }, 600)
    },
})

import CONFIG from '../../config.js';
