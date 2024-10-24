export function useUpload() {
  const internalInstance = getCurrentInstance();
  const result: any = {};
  result.apiService = internalInstance?.appContext.config.globalProperties.$apiService;
  if (result.apiService) {
    result.uploadImage = result.apiService.files.uploadImageService.create;
    result.uploadImageUrl = result.apiService.files.uploadImageService.uploadImageUrl;
    result.uploadFile = result.apiService.files.uploadFileService.create;
    result.uploadFileUrl = result.apiService.files.uploadFileService.uploadFileUrl;
    result.getToken = internalInstance?.appContext.config.globalProperties.$getToken;
  }
  return result;
}
