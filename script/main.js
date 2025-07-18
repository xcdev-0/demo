// call.js
const axios = require('axios').default;
const tough = require('tough-cookie');
const { wrapper } = require('axios-cookiejar-support');
const { signupUsers } = require('./1-register.js');

// 클라이언트 생성 함수
function createClient() {
  const jar = new tough.CookieJar();
  return wrapper(axios.create({ jar }));
}

const clientA = createClient();
const clientB = createClient();

async function requestChat(clientB) {
  try {
    await clientB.post('http://localhost:8080/api/user/b/login', {
      userId: 'testB',
      password: '1234'
    });
    console.log('[B] 로그인 성공');

    await clientB.post('http://localhost:8080/api/chat/call', {
      aId: 1
    });
    console.log('[B → A] 채팅 요청 성공');
  } catch (err) {
    if (err.response) {
      console.error(
        `[에러] status: ${err.response.status}, error: ${err.response.data?.error}, path: ${err.response.data?.path}`
      );
    } else {
      console.error('[에러] 네트워크 또는 알 수 없는 에러:', err.message);
    }
  }
}

async function handleChatRequests(clientA) {
  try {
    await clientA.post('http://localhost:8080/api/user/a/login', {
      userId: 'testA',
      password: '1234'
    });
  } catch (err) {
    if (err.response) {
      console.error(
        `[에러] status: ${err.response.status}, error: ${err.response.data?.error}, path: ${err.response.data?.path}`
      );
    } else {
      console.error('[에러] 네트워크 또는 알 수 없는 에러:', err.message);
    }
    return;
  }

  try {
    const pendingList = await clientA.get('http://localhost:8080/api/chat/pending');
    console.log('[A] 채팅 요청 팬딩 리스트: ', pendingList.data);

    const acceptChatRequest = await clientA.post('http://localhost:8080/api/chat/accept', {
      chatRequestId: pendingList.data[0].id
    });
    console.log('[A] 채팅 요청 수락 성공: ', acceptChatRequest.data);
  } catch (err) {
    if (err.response) {
      console.error(
        `[에러] status: ${err.response.status}, error: ${err.response.data?.error}, path: ${err.response.data?.path}`
      );
    } else {
      console.error('[에러] 네트워크 또는 알 수 없는 에러:', err.message);
    }
  }
}

async function main() {
  await signupUsers();
  await requestChat(clientB);
  await handleChatRequests(clientA);

  // 필요하다면 clientA, clientB로 추가 요청도 가능!
}

main(); 