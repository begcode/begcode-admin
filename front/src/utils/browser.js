//判断是否IE<11浏览器
export function isIE() {
  return navigator.userAgent.indexOf('compatible') > -1 && navigator.userAgent.indexOf('MSIE') > -1;
}

export function isIE11() {
  return navigator.userAgent.indexOf('Trident') > -1 && navigator.userAgent.indexOf('rv:11.0') > -1;
}

//判断是否IE的Edge浏览器
export function isEdge() {
  return navigator.userAgent.indexOf('Edge') > -1 && !isIE();
}

export function getIEVersion() {
  const userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
  const isIE = isIE();
  const isIE11 = isIE11();
  const isEdge = isEdge();

  if (isIE) {
    const reIE = new RegExp('MSIE (\\d+\\.\\d+);');
    reIE.test(userAgent);
    const fIEVersion = parseFloat(RegExp['$1']);
    if (fIEVersion === 7 || fIEVersion === 8 || fIEVersion === 9 || fIEVersion === 10) {
      return fIEVersion;
    }
    return 6; // IE版本<7
  } else if (isEdge) {
    return 'edge';
  } else if (isIE11) {
    return 11;
  }
  return -1;
}
