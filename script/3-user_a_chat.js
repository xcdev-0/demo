// 3-enterChatA.js

const axios = require('axios').default;
const tough = require('tough-cookie');
const { wrapper } = require('axios-cookiejar-support');
const { Client } = require('@stomp/stompjs');
const WebSocket = require('websocket').w3cwebsocket;

// ====== STEP 1. Axios 클라이언트 생성 (쿠키 지원) ======
const jar = new tough.CookieJar();
const client = wrapper(axios.create({ jar }));

// ====== STEP 2. 로그인 후 WebSocket 연결 및 채팅방 입장 ======
async function enterChatRoom(roomId) {
  try {
    // 유저 A 로그인
    await client.post('http://localhost:8080/api/user/a/login', {
      userId: 'testA',
      password: '1234'
    });
    console.log('[A] 로그인 성공');

    // 세션 쿠키 가져오기 (JSESSIONID)
    const cookies = await jar.getCookies('http://localhost:8080');
    const jsessionCookie = cookies.find(c => c.key === 'JSESSIONID');
    if (!jsessionCookie) throw new Error('JSESSIONID 쿠키 없음!');

    const jsessionId = jsessionCookie.value;

    // STOMP 클라이언트 생성
    const stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws-chat',
      connectHeaders: {},
      webSocketFactory: () =>
        new WebSocket(`ws://localhost:8080/ws-chat`, null, null, {
          Cookie: `JSESSIONID=${jsessionId}`
        }),
      onConnect: (frame) => {
        console.log('[A] WebSocket 연결 성공:', frame.headers);

        // 채팅방 구독
        stompClient.subscribe(`/topic/chat/${roomId}`, (msg) => {
          console.log(`[A] 받은 메시지: ${msg.body}`);
        });

        // 메시지 전송
        stompClient.publish({
          destination: `/app/chat.send/${roomId}`,
          body: JSON.stringify({
            sender: 'testA',
            content: 'Hello! 👋 from A',
            timestamp: new Date()
          }),
        });
        console.log(`[A] 메시지 전송 완료 to room ${roomId}`);
      },
      onStompError: (frame) => {
        console.error('STOMP 오류:', frame);
      },
    });

    stompClient.activate();
  } catch (err) {
    console.error('[A] 오류 발생:', err.message || err);
  }
}

// ====== 실행 ======
const roomId = process.argv[2]; // 예: node 3-enterChatA.js 1
if (!roomId) {
  console.error('⚠️ 채팅방 ID를 인자로 넘겨주세요! 예: node 3-enterChatA.js 1');
  process.exit(1);
}
enterChatRoom(roomId);
