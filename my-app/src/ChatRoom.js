import React, { useState, useEffect, useContext, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import { AuthContext } from './App';

const ChatRoom = ({ roomId }) => {
  const { user } = useContext(AuthContext);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  // 채팅 내역 불러오기
  useEffect(() => {
    const loadChatHistory = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/chat/history?roomId=${roomId}`, {
          credentials: 'include'
        });
        
        if (response.ok) {
          const history = await response.json();
          // 백엔드에서 받은 채팅 내역을 프론트엔드 형식으로 변환
          const formattedHistory = history.map(msg => ({
            sender: msg.sender,
            content: msg.content,
            type: msg.type || 'TALK',
            roomId: roomId
          }));
          setMessages(formattedHistory);
        } else {
          console.log('채팅 내역을 불러올 수 없습니다.');
        }
      } catch (error) {
        console.error('채팅 내역 로딩 실패:', error);
      } finally {
        setIsLoadingHistory(false);
      }
    };

    loadChatHistory();
  }, [roomId]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    // STOMP 클라이언트 생성
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws-chat',
      connectHeaders: {
        // 쿠키를 포함하여 인증 정보 전달
        'Cookie': document.cookie
      },
      debug: function (str) {
        console.log(str);
      },
      // 재연결 비활성화
      reconnectDelay: 0,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    // 연결 성공 시
    client.onConnect = (frame) => {
      console.log('Connected to STOMP: ' + frame);
      setIsConnected(true);
      
      // 채팅방 구독
      client.subscribe(`/topic/chat/${roomId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        
        // 자기가 보낸 메시지는 무시 (이미 로컬에 추가됨)
        if (receivedMessage.sender === user.userId) {
          return;
        }
        
        // 백엔드에서 받은 메시지에 로컬 표시용 정보 추가
        const enhancedMessage = {
          ...receivedMessage,
          roomId: roomId,
        };
        setMessages(prev => [...prev, enhancedMessage]);
      });
    };

    // 연결 해제 시
    client.onDisconnect = () => {
      console.log('Disconnected from STOMP');
      setIsConnected(false);
    };

    // 에러 발생 시
    client.onStompError = (frame) => {
      console.error('STOMP error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    // 연결 시작
    client.activate();
    setStompClient(client);

    // 컴포넌트 언마운트 시 연결 해제
    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, [roomId]);

  const sendMessage = () => {
    if (newMessage.trim()) {
      // 백엔드 DTO에 맞춘 메시지 구조
      const message = {
        sender: user.userId,
        content: newMessage,
        type: "TALK"
      };

      // 로컬 표시용 메시지 (타임스탬프와 로그인 타입 포함)
      const localMessage = {
        ...message,
        roomId: roomId
      };

      // WebSocket 연결이 되어 있으면 서버로 전송
      if (stompClient && isConnected) {
        stompClient.publish({
          destination: `/app/chat.send/${roomId}`,
          body: JSON.stringify(message)
        });
      } else {
        // 연결이 안 되어 있으면 로컬에만 추가
        console.log('WebSocket 연결이 안 되어 있어서 로컬에만 메시지 추가');
      }

      // 메시지를 로컬 상태에 추가
      setMessages(prev => [...prev, localMessage]);
      setNewMessage('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };



  return (
    <div style={{ 
      maxWidth: 800, 
      margin: '20px auto', 
      border: '1px solid #ddd', 
      borderRadius: 8,
      height: '80vh',
      display: 'flex',
      flexDirection: 'column'
    }}>
      {/* 헤더 */}
      <div style={{ 
        padding: '16px', 
        backgroundColor: '#f8f9fa', 
        borderBottom: '1px solid #ddd',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
      }}>
        <div>
          <h3 style={{ margin: 0 }}>채팅방 {roomId}</h3>
          <small style={{ color: '#666' }}>
            {user.userId} ({user.loginType.toUpperCase()} 로그인) | 
            연결상태: {isConnected ? '🟢 연결됨' : '🔴 연결중...'}
          </small>
        </div>
      </div>

      {/* 메시지 영역 */}
      <div style={{ 
        flex: 1, 
        overflowY: 'auto', 
        padding: '16px',
        backgroundColor: '#f8f9fa'
      }}>
        {isLoadingHistory ? (
          <div style={{ textAlign: 'center', color: '#666', marginTop: '50px' }}>
            채팅 내역을 불러오는 중... 📚
          </div>
        ) : messages.length === 0 ? (
          <div style={{ textAlign: 'center', color: '#666', marginTop: '50px' }}>
            채팅방에 입장했습니다! 메시지를 보내보세요! 💬
          </div>
        ) : (
          messages.map((msg, index) => (
            <div 
              key={index} 
              style={{ 
                marginBottom: '12px',
                display: 'flex',
                justifyContent: msg.sender === user.userId ? 'flex-end' : 'flex-start'
              }}
            >
              <div style={{
                maxWidth: '70%',
                padding: '12px 16px',
                borderRadius: '18px',
                backgroundColor: msg.sender === user.userId ? '#007bff' : 'white',
                color: msg.sender === user.userId ? 'white' : 'black',
                border: msg.sender === user.userId ? 'none' : '1px solid #ddd',
                boxShadow: '0 1px 2px rgba(0,0,0,0.1)'
              }}>
                <div style={{ fontSize: '12px', marginBottom: '4px', opacity: 0.8 }}>
                  {msg.sender}
                </div>
                <div>{msg.content}</div>
              </div>
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* 메시지 입력 영역 */}
      <div style={{ 
        padding: '16px', 
        borderTop: '1px solid #ddd',
        backgroundColor: 'white'
      }}>
        <div style={{ display: 'flex', gap: '8px' }}>
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder={isConnected ? "메시지를 입력하세요..." : "연결 중... 메시지를 입력하세요..."}
            style={{
              flex: 1,
              padding: '12px',
              border: '1px solid #ddd',
              borderRadius: '20px',
              outline: 'none',
              backgroundColor: isConnected ? 'white' : '#f8f9fa'
            }}
          />
          <button
            onClick={sendMessage}
            disabled={!newMessage.trim()}
            style={{
              padding: '12px 20px',
              backgroundColor: isConnected && newMessage.trim() ? '#007bff' : '#ccc',
              color: 'white',
              border: 'none',
              borderRadius: '20px',
              cursor: newMessage.trim() ? 'pointer' : 'not-allowed'
            }}
          >
            {isConnected ? '전송' : '연결중...'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatRoom;
