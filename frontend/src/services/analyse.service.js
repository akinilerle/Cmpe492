import axios from 'axios';
import configs from '../configs';

const check = data => {
  return axios.post(configs.analyse.endpoint, data);
};

export const analyseService = {
  check
};
