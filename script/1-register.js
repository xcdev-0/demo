// signup.js
const axios = require('axios').default;

async function signupUsers() {
  try {
    // 유저 A 회원가입
    await axios.post('http://localhost:8080/api/user/a/signup', {
      userId: 'testA',
      password: '1234',
      departments: ['one', 'two']
    });
    console.log('[A] 회원가입 성공');

    // 유저 B 회원가입
    await axios.post('http://localhost:8080/api/user/b/signup', {
      userId: 'testB',
      password: '1234',
      information: 'testB information'
    });
    console.log('[B] 회원가입 성공');
  } catch (err) {
    console.error('회원가입 실패:', err.response?.data || err.message);
  }
}

// 함수를 export
module.exports = { signupUsers };

// 직접 실행도 가능하게
if (require.main === module) {
  signupUsers();
}
