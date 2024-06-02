import fs from 'fs';
import path from 'path';
import { resultPageSuccess, resultSuccess } from 'mock/_util';
import Papa from 'papaparse';
import { camelCase } from 'lodash-es';

const allData: any[] = [];
const dataPath = path.resolve(__dirname, '../../data/upload_file.csv');
const fakePath = path.resolve(__dirname, '../../fake-data/upload_file.csv');
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

const baseApiUrl = '/api/upload-files';

const getById = id => {
  return allData.find(item => (item.id = id));
};

const getIdFromUrl = url => {
  const regExp = new RegExp(`${baseApiUrl}/`.replace('/', '\\/') + '(?<id>\\d+)');
  let match = regExp.exec(url);
  if (match) {
    let { id } = match.groups as any;
    return id;
  }
};

export default [
  {
    url: `${baseApiUrl}`,
    timeout: 1000,
    method: 'get',
    response: ({ query }) => {
      const { page = 0, size = 15 } = query;
      return resultPageSuccess(page, size, allData);
    },
  },
  {
    url: `${baseApiUrl}/:id`,
    timeout: 1000,
    method: 'get',
    response: ({ url }) => {
      console.log('url:', url);
      const id = getIdFromUrl(url);
      if (id) {
        const entity = getById(id);
        if (entity) {
          return entity;
        }
      }
      return resultSuccess({});
    },
  },
  {
    url: `${baseApiUrl}`,
    timeout: 1000,
    method: 'post',
    response: ({ body }) => {
      console.log('body', body);
      return resultSuccess({});
    },
  },
  {
    url: `${baseApiUrl}/:id`,
    timeout: 1000,
    method: 'put',
    response: ({ body, params, url }) => {
      const id = getIdFromUrl(url);
      if (id) {
        console.log('id:', id);
        const entity = getById(id);
        if (entity) {
          console.log('entity:', entity);
          return resultSuccess(entity);
        }
      }
      console.log('body', body);
      console.log('params', params);
      return resultSuccess({});
    },
  },
  {
    url: `${baseApiUrl}/:id`,
    timeout: 1000,
    method: 'delete',
    response: ({ params, url }) => {
      console.log('params', params);
      const id = getIdFromUrl(url);
      if (id) {
        const findIndex = allData.findIndex(item => (item.id = id));
        if (findIndex > -1) {
          allData.splice(findIndex, 1);
        }
        return {};
      }
      return resultSuccess({});
    },
  },
];
