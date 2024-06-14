import fs from 'fs';
import path from 'path';
import { resultPageSuccess } from 'mock/_util';
import qs from 'qs';
import Papa from 'papaparse';
import { camelCase } from 'lodash-es';

const allData: any[] = [];
const dataPath = path.resolve(__dirname, '../../data/site_config.csv');
const fakePath = path.resolve(__dirname, '../../fake-data/site_config.csv');
let filePath = fakePath;
if (fs.existsSync(dataPath)) {
  filePath = dataPath;
}
const csv = fs.readFileSync(filePath);
Papa.parse(csv.toString(), {
  header: true,
  transformHeader: header => camelCase(header),
  skipEmptyLines: true,
  complete: function (results: any) {
    allData.push(...(results.data as any[]));
  },
});

const baseApiUrl = '/api/site-configs';

const getById = id => {
  return allData.find(item => item.id === id);
};

const getIdFromUrl = url => {
  const regExp = new RegExp(`${baseApiUrl}/`.replace('/', '\\/') + '(?<id>\\d+)');
  const match = regExp.exec(url);
  if (match) {
    const { id } = match.groups as any;
    return id;
  }
};

export function setOssConfigMock(mock) {
  mock.onGet(`${baseApiUrl}`).reply(config => {
    const { params = '' } = config;
    const { page = '0', size = '15' } = qs.parse(params);
    return [200, resultPageSuccess(Number(page), Number(size), allData)];
  });
  mock.onGet(new RegExp(`${baseApiUrl}/\\d+`)).reply(config => {
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
    const { data } = config;
    console.log('data', data);
    return [201, data];
  });
  mock.onPut(new RegExp(`${baseApiUrl}/\\d+`)).reply(config => {
    const { url, params, data } = config;
    const id = getIdFromUrl(url);
    if (id) {
      console.log('id:', id);
      const entity = getById(id);
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
    const { params, url } = config;
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
}
