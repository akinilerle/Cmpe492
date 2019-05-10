const dev = {
  analyse: { endpoint: 'http://54.167.124.169:5000/analyse' },
  upload: {
    txt: {
      endpoint: 'http://54.167.124.169:5000/analyse/file'
    }
  },
  csv: {
    endpoint: 'http://54.167.124.169:5000/analyse/txt'
  }
};

const prod = {
  analyse: { endpoint: 'http://54.167.124.169:5000/' },
  upload: {
    txt: {
      endpoint: 'http://54.167.124.169:5000/'
    }
  },
  csv: {
    endpoint: 'http://54.167.124.169:5000/'
  }
};

const config = process.env.REACT_APP_STAGE === 'production' ? prod : dev;

export default {
  ...config
};
