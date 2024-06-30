import { resultPageSuccess } from 'mock/_util';
import qs from 'qs';
import { getMockData } from 'mock/allMockData';

const allData: any[] = [];
let feteched = false;

const baseApiUrl = '/api/sms-templates';

const fetchData = () => {
  if (!feteched) {
    allData.push(...getMockData('sms_template'));
    feteched = true;
  }
};

const getById = id => {
  return allData.find(item => item.id === id);
};

const updateById = (id, data) => {
  const result = allData.find(item => item.id === id);
  if (result) {
    Object.assign(result, JSON.parse(data));
  }
  return result;
};

const getIdFromUrl = url => {
  const regExp = new RegExp(`${baseApiUrl}/`.replace('/', '\\/') + '(?<id>\\d+)');
  const match = regExp.exec(url);
  if (match) {
    const { id } = match.groups as any;
    return id;
  }
};

export function setSmsTemplateMock(mock) {
  mock.onGet(`${baseApiUrl}`).reply(config => {
    fetchData();
    const { params = '' } = config;
    const { page = '0', size = '15' } = qs.parse(params);
    return [200, resultPageSuccess(Number(page), Number(size), allData)];
  });
  mock.onGet(new RegExp(`${baseApiUrl}/\\d+`)).reply(config => {
    fetchData();
    const { url } = config;
    const id = getIdFromUrl(url);
    if (id) {
      const entity = getById(id);
      if (entity) {
        return [200, entity];
      }
    }
    return [404, {}];
  });
  mock.onPost(baseApiUrl).reply(config => {
    fetchData();
    const { data } = config;
    console.log('data', data);
    return [201, data];
  });
  mock.onPut(new RegExp(`${baseApiUrl}/\\d+`)).reply(config => {
    fetchData();
    const { url, params, data } = config;
    const id = getIdFromUrl(url);
    if (id) {
      console.log('id:', id);
      const entity = updateById(id, data);
      if (entity) {
        console.log('entity:', entity);
        return [200, entity];
      }
    }
    console.log('data', data);
    console.log('params', params);
    return [200, {}];
  });
  mock.onDelete(new RegExp(`${baseApiUrl}/\\d+`)).reply(config => {
    fetchData();
    const { params, url } = config;
    console.log('params', params);
    const id = getIdFromUrl(url);
    if (id) {
      const findIndex = allData.findIndex(item => item.id === id);
      if (findIndex > -1) {
        allData.splice(findIndex, 1);
      }
      return [204, {}];
    }
    return [404, {}];
  });
  mock.onGet(`${baseApiUrl}/stats`).reply(config => {
    fetchData();
    console.log('config', config);
    return [200, { id_count: allData.length }];
  });
  mock.onDelete(`${baseApiUrl}`).reply(config => {
    fetchData();
    const { data = '{}' } = config;
    const { ids = [] } = JSON.parse(data);
    const newData = allData.filter(item => !ids.includes(item.id));
    allData.length = 0;
    allData.push(...newData);
    return [204, {}];
  });
  mock.onPut(new RegExp(`${baseApiUrl}/relations/\\w+`)).reply(config => {
    fetchData();
    console.log('config:', config);
    return [200, {}];
  });
}
