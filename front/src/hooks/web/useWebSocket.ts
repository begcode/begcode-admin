// noinspection JSUnusedGlobalSymbols

import { useWebSocket, UseWebSocketReturn } from '@vueuse/core';
import { getToken } from '@/utils/auth';

let result: UseWebSocketReturn<any>;
const listeners = new Map();

/**
 * 开启 WebSocket 链接，全局只需执行一次
 * @param url
 */
export function connectWebSocket(url: string) {
  let token = (getToken() || '') as string;
  result = useWebSocket(url, {
    // 自动重连 (遇到错误最多重复连接10次)
    autoReconnect: {
      retries: 10,
      delay: 5000,
    },
    // 心跳检测
    heartbeat: {
      message: 'ping',
      interval: 55000,
    },
    protocols: [token],
  });
  if (result) {
    result.open = onOpen;
    result.close = onClose;

    const ws = unref(result.ws);
    if (ws != null) {
      ws.onerror = onError;
      ws.onmessage = onMessage;
      ws.onopen = onOpen;
      ws.onclose = onClose;
    }
  }
}

function onOpen() {
  console.log('[WebSocket] 连接成功');
}

function onClose(e) {
  console.log('[WebSocket] 连接断开：', e);
}

function onError(e) {
  console.log('[WebSocket] 连接发生错误: ', e);
}

function onMessage(e) {
  console.debug('[WebSocket] -----接收消息-------', e.data);
  try {
    if (e === 'ping') {
      return;
    }
    const data = JSON.parse(e.data);
    for (const callback of listeners.keys()) {
      try {
        callback(data);
      } catch (err) {
        console.error(err);
      }
    }
  } catch (err) {
    console.error('[WebSocket] data解析失败：', err);
  }
}

/**
 * 添加 WebSocket 消息监听
 * @param callback
 */
export function onWebSocket(callback: (data: object) => any) {
  if (!listeners.has(callback)) {
    if (typeof callback === 'function') {
      listeners.set(callback, null);
    } else {
      console.debug('[WebSocket] 添加 WebSocket 消息监听失败：传入的参数不是一个方法');
    }
  }
}

/**
 * 解除 WebSocket 消息监听
 *
 * @param callback
 */
export function offWebSocket(callback: (data: object) => any) {
  listeners.delete(callback);
}

export function useMyWebSocket() {
  return result;
}
