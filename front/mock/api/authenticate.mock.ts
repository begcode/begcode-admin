import { getMockData } from 'mock/allMockData';

const baseApiUrl = '/api/';

const allData: any[] = [];
let feteched = false;

const fetchData = () => {
  if (!feteched) {
    allData.push(...getMockData('jhi_user'));
    feteched = true;
  }
};

export function setAuthMock(mock) {
  mock?.onPost(`${baseApiUrl}authenticate/withoutCaptcha`).reply(config => {
    console.log('withoutCaptcha.config', config);
    const { username = '', password = '' } = JSON.parse(config.data);
    // return an array in the form of [status, data, headers]
    if ((username === 'admin' && password === 'admin') || (username === 'user' && password === 'user')) {
      return [200, { id_token: 'id_token:' + username }];
    } else {
      return [401, {}];
    }
  });
  const urlRegex = new RegExp(`${baseApiUrl}randomImage/\\d+`);
  mock?.onGet(urlRegex).reply(config => {
    console.log('randomImage.config:::', config);
    return [
      200,
      'data:image/jpg;base64, /9j/4 AAQSkZJRgABAgAAAQABAAD/2 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 //9k=',
    ];
  });
  mock?.onGet('/api/account').reply(config => {
    console.log('account.config:::', config);
    fetchData();
    const user = allData.find(user => user.login === 'admin') || {};
    console.log('user.mock:::', user);
    return [200, user];
  });
}
