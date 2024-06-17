import Papa from 'papaparse';
import { camelCase } from 'lodash-es';

const allDataModules = import.meta.glob('./data/*.csv');
const allFakeDataModules = import.meta.glob('./fake-data/*.csv');

const allData: any = {};

for (const path in allFakeDataModules) {
  const name = (path.split('/').pop() || '').replace('.csv', '');
  const url = path.replace('./fake-data/', '/mock/fake-data/');
  fetch(url)
    .then(response => response.text())
    .then(csvString => {
      if (!allData[name]) {
        allData[name] = [];
        Papa.parse(csvString, {
          header: true,
          transformHeader: header => camelCase(header),
          skipEmptyLines: true,
          complete: function (results: any) {
            allData[name].push(...(results.data as any[]));
          },
        });
      }
    })
    .catch(error => {
      console.error('Error fetching or parsing CSV:', error);
    });
}

for (const path in allDataModules) {
  const name = (path.split('/').pop() || '').replace('.csv', '');
  const url = path.replace('./data/', '/mock/data/');
  fetch(url)
    .then(response => response.text())
    .then(csvString => {
      allData[name] = [];
      Papa.parse(csvString, {
        header: true,
        transformHeader: header => camelCase(header),
        skipEmptyLines: true,
        complete: function (results: any) {
          allData[name].push(...(results.data as any[]));
        },
      });
    })
    .catch(error => {
      console.error('Error fetching or parsing CSV:', error);
    });
}

export function getMockData(name) {
  return allData[name] || [];
}
