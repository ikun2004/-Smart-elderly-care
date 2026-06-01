// pages/service/ai-assistant/index.js
import CONFIG from '../../config.js';
import common_vendor from '../../common/vendor.js';

Page({
  data: {
    messages: [], // 消息列表
    inputMessage: '', // 输入框内容
    scrollToMessage: '', // 滚动到指定消息
    loading: false, // 是否正在加载
    messageId: 0, // 消息ID计数器
    reservations: [] // 预订信息列表
  },

  onLoad: function() {
    // 先尝试读取本地消息
    const localMessages = wx.getStorageSync('ai_messages') || [];
    const localMessageId = wx.getStorageSync('ai_messageId') || 0;
    if (localMessages.length > 0) {
      this.setData({
        messages: localMessages,
        messageId: localMessageId
      });
    } else {
      // 页面加载时，添加一条欢迎消息
      this.addMessage({
        type: 'ai',
        content: CONFIG.WELCOME_MESSAGE,
        timestamp: this.getCurrentTime()
      });
    }
    // 获取预订信息
    this.getReservations();
  },

  // 获取当前时间
  getCurrentTime: function() {
    const now = new Date();
    return now.toLocaleDateString() + " " + now.toLocaleTimeString();
  },

  // 返回上一页
  handleBack: function() {
    wx.navigateBack();
  },

  // 监听输入变化
  onInputChange: function(e) {
    this.setData({
      inputMessage: e.detail.value
    });
  },

  // 发送消息
  sendMessage: function() {
    const message = this.data.inputMessage.trim();
    if (!message) return;

    // 添加用户消息到列表
    this.addMessage({
      type: 'user',
      content: message,
      timestamp: this.getCurrentTime()
    });

    // 清空输入框
    this.setData({
      inputMessage: ''
    });

    // 添加"正在思考中..."消息
    const thinkingMessageId = this.addMessage({
      type: 'ai',
      content: '正在思考中...',
      timestamp: this.getCurrentTime(),
      thinking: true
    });

    // 使用EventSource获取AI回复
    this.getAiResponseStream(message, thinkingMessageId);
  },

  // 添加消息到列表，并返回消息ID
  addMessage: function(message) {
    const messageId = this.data.messageId + 1;
    const newMessage = {
      ...message,
      id: messageId
    };
    const newMessages = [...this.data.messages, newMessage];
    this.setData({
      messages: newMessages,
      messageId: messageId,
      scrollToMessage: `msg-${messageId}`
    });
    // 保存到本地
    wx.setStorageSync('ai_messages', newMessages);
    wx.setStorageSync('ai_messageId', messageId);
    return messageId;
  },

  // 更新特定消息的内容
  updateMessageContent: function(messageId, content) {
    const messages = this.data.messages;
    const index = messages.findIndex(msg => msg.id === messageId);
    if (index !== -1) {
      const updatedMessages = [...messages];
      updatedMessages[index] = {
        ...updatedMessages[index],
        content: content
      };
      this.setData({
        messages: updatedMessages,
        scrollToMessage: `msg-${messageId}`
      });
      // 保存到本地
      wx.setStorageSync('ai_messages', updatedMessages);
    }
  },

  // 使用EventSource获取AI流式回复
  getAiResponseStream: function(message, thinkingMessageId, retryCount = 0) {
    const that = this;
    const url = `${CONFIG.AI_BASE_URL}/customer/ai/generateStreamAsString?message=${encodeURIComponent(message)}`;
    const token = common_vendor.index.getStorageSync('token');
    const MAX_RETRY = 3;
    // 创建一个新的请求任务
    const requestTask = wx.request({
      url: url,
      method: 'GET',
      responseType: 'text',
      enableChunked: true, // 启用分块传输
      header: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json;charset=UTF-8',
        'authorization': token
      },
      success: function(res) {
        if (res.statusCode === 200 && res.data) {
          let result = res.data.replace(/data:/g, '').replace(/\[complete\]/g, '').replace(/[\r\n]/g, '');
          that.updateMessageContent(thinkingMessageId, result);
        }
      },
      fail: function(err) {
        if (retryCount < MAX_RETRY) {
          setTimeout(() => {
            that.getAiResponseStream(message, thinkingMessageId, retryCount + 1);
          }, 1000 * (retryCount + 1));
        } else {
          that.updateMessageContent(thinkingMessageId, '抱歉，连接服务器失败，请检查网络连接后重试。');
          console.error('请求失败:', err);
        }
      },
      complete: function() {
        that.getReservations();
      }
    });

    // 监听数据块接收事件
    requestTask.onChunkReceived(function(res) {
      try {
        const decoder = new TextDecoder('utf-8');
        let chunk = decoder.decode(new Uint8Array(res.data));
        // 去除所有 data: 和 [complete]，以及换行符
        chunk = chunk.replace(/data:/g, '')
                     .replace(/\[complete\]/g, '')
                     .replace(/[\r\n]/g, '');
        // 如果分块内容为空，直接返回
        if (!chunk.trim()) return;
        // 获取当前消息内容
        const currentMessage = that.data.messages.find(msg => msg.id === thinkingMessageId);
        let newContent = currentMessage.content;
        // 如果是第一个数据块，清除"正在思考中..."
        if (newContent === '正在思考中...') {
          newContent = chunk;
        } else {
          newContent += chunk;
        }
        // 再次保险去除所有 data: 和 [complete] 以及换行符
        newContent = newContent.replace(/data:/g, '').replace(/\[complete\]/g, '').replace(/[\r\n]/g, '');
        that.updateMessageContent(thinkingMessageId, newContent);
      } catch (error) {
        console.error('处理数据块时出错:', error);
      }
    });
  },

  // 获取预订信息
  getReservations: function() {
    // 这里可以实现获取预订信息的逻辑
    // 模拟数据
    this.setData({
      reservations: [
        { id: 1, room: '豪华套房', date: '2023-12-25', status: '已确认' },
        { id: 2, room: '标准间', date: '2023-12-26', status: '待支付' }
      ]
    });
  },

  // 加载更多消息（向上滚动时）
  loadMoreMessages: function() {
    // 这里可以实现加载历史消息的逻辑
    // 目前暂不实现
  },

  clearSession: function() {
    wx.removeStorageSync('ai_messages');
    wx.removeStorageSync('ai_messageId');
    this.setData({
      messages: [],
      messageId: 0
    });
    // 重新添加欢迎语
    this.addMessage({
      type: 'ai',
      content: CONFIG.WELCOME_MESSAGE,
      timestamp: this.getCurrentTime()
    });
  }
});