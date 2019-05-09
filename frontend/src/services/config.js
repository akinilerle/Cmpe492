import configs from '../configs/';
import axios from 'axios';
import { history } from '../helpers';

const API_ROOT = configs.apiUrl;
axios.defaults.baseURL = API_ROOT;

axios.interceptors.request.use(
  config => {
    let token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = token;
    }
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  response => {
    checkAuth(response);
    return response;
  },
  function(error) {
    checkAuth(error);
    return Promise.reject(error);
  }
);

const checkAuth = response => {
  if (response.status === 401 || response.status === 403) {
    localStorage.removeItem('token');
    localStorage.removeItem('profile');
    history.push('/login');
  }
};
