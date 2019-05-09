const replaceAddress = (str) => {
  let x = String(str) || '';
  return x.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '');
};

const isValidAddress = (val) => {
  let str = replaceAddress(val);
  if (str.length < 26 || str.length > 35) {
    return false;
  }
  let re = /^[A-Z0-9]+$/i;
  return re.test(str);
};

const hasErrors = (fieldsError) => {
  return Object.keys(fieldsError).some(field => fieldsError[field]);
};

export const validationUtil = {
  isValidAddress,
  hasErrors
};
