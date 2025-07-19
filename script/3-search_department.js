// search_department.js
const axios = require('axios').default;
const tough = require('tough-cookie');
const { wrapper } = require('axios-cookiejar-support');
// const { signupUsers } = require('./1-register.js');

// 클라이언트 생성 함수
function createClient() {
  const jar = new tough.CookieJar();
  return wrapper(axios.create({ jar }));
}


async function registerUserA(client, userId, departments) {
  const response = await client.post('http://localhost:8080/api/user/a/signup', {
    userId: `${userId}`,
    password: '1234',
    departments: departments
  });
  console.log(response.data);
}
async function searchDepartment(client,keyword) {
  const response = await client.get(`http://localhost:8080/api/user/a/search/department?keyword=${keyword}`);
  console.log(response.data);
}

async function main() {
    const client = createClient();
    await registerUserA(client, 'test1', ['dept3', 'dept2', 'dept1']);
    await registerUserA(client, 'test2', ['dept1']);
    await registerUserA(client, 'test3', ['dept1']);
    await searchDepartment(client, 'dept1');
}

main();