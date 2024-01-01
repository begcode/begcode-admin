export interface PageParams {
  page: number;
  size: number;
}

export interface PageRecord<T> {
  records: T[];
  total: number;
  size: number;
  page: number;
}
