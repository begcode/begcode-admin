export enum SendStatus {
  /**
   * 未推送
   */
  WAITING = 'WAITING',

  /**
   * 推送成功
   */
  SUCCESS = 'SUCCESS',

  /**
   * 推送失败
   */
  FAILURE = 'FAILURE',

  /**
   * 失败不再发送
   */
  NOT_TRY = 'NOT_TRY',
}
