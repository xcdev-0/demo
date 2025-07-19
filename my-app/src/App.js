import React, { useState, createContext, useEffect } from "react";
import ChatRoom from "./ChatRoom";

export const AuthContext = createContext();

const App = () => {
  const [user, setUser] = useState(null);
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [loginType, setLoginType] = useState("a");
  const [isLoading, setIsLoading] = useState(true);

  // 페이지 로드 시 로그인 상태 확인
  useEffect(() => {
    const checkLoginStatus = async () => {
      try {
        // A 로그인과 B 로그인 모두 확인
        const [resA, resB] = await Promise.all([
          fetch('http://localhost:8080/api/user/a/check', { credentials: 'include' }),
          fetch('http://localhost:8080/api/user/b/check', { credentials: 'include' })
        ]);

        if (resA.ok) {
          const userData = await resA.json();
          setUser({ userId: userData.userId, loginType: 'a' });
        } else if (resB.ok) {
          const userData = await resB.json();
          setUser({ userId: userData.userId, loginType: 'b' });
        }
      } catch (e) {
        console.log('로그인 상태 확인 실패:', e);
      } finally {
        setIsLoading(false);
      }
    };

    checkLoginStatus();
  }, []);

  const handleLogin = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/user/${loginType}/login`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, password })
      });
      if (!res.ok) throw new Error("로그인 실패");
      setUser({ userId, loginType });
    } catch (e) {
      alert("로그인 실패 😢");
    }
  };

  if (isLoading) {
    return (
      <div style={{ 
        maxWidth: 400, 
        margin: "100px auto", 
        textAlign: "center" 
      }}>
        <h2>로딩 중...</h2>
        <p>로그인 상태를 확인하고 있어요! 🔄</p>
      </div>
    );
  }

  if (!user) {
    return (
      <div style={{ maxWidth: 400, margin: "100px auto", textAlign: "center" }}>
        <h2>로그인</h2>
        
        {/* 로그인 타입 선택 */}
        <div style={{ marginBottom: 16 }}>
          <label style={{ marginRight: 16 }}>
            <input
              type="radio"
              value="a"
              checked={loginType === "a"}
              onChange={(e) => setLoginType(e.target.value)}
              style={{ marginRight: 4 }}
            />
            A 로그인
          </label>
          <label>
            <input
              type="radio"
              value="b"
              checked={loginType === "b"}
              onChange={(e) => setLoginType(e.target.value)}
              style={{ marginRight: 4 }}
            />
            B 로그인
          </label>
        </div>

        <input
          type="text"
          placeholder="User ID"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          style={{ padding: 8, width: "100%", marginBottom: 8 }}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={{ padding: 8, width: "100%", marginBottom: 12 }}
        />
        <button onClick={handleLogin} style={{ padding: 10, width: "100%", backgroundColor: "#228be6", color: "white", border: "none", borderRadius: 4 }}>
          {loginType.toUpperCase()} 로그인
        </button>
      </div>
    );
  }

  return (
    <AuthContext.Provider value={{ user }}>
      <ChatRoom roomId={1} />
    </AuthContext.Provider>
  );
};

export default App;
