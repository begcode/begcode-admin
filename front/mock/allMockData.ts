import Papa from 'papaparse';
import { useGlobSetting } from '@/hooks/setting';

const allDataModules = useGlobSetting().useMock ? import.meta.glob('./data/*.csv') : {};
const allFakeDataModules = useGlobSetting().useMock ? import.meta.glob('./fake-data/*.csv') : {};

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
          delimiter: ';',
          dynamicTyping: true,
          header: true,
          transformHeader: header => _camelCase(header),
          skipEmptyLines: true,
          complete(results: any) {
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
        delimiter: ';',
        dynamicTyping: true,
        header: true,
        transformHeader: header => _camelCase(header),
        skipEmptyLines: true,
        complete(results: any) {
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
