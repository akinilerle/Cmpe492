import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'mobx-react';
import App from './App';
import CommonStore from './stores/CommonStore';
import * as serviceWorker from './serviceWorker';

import './index.css';

require('./services/config');

const stores = {
  commonStore: CommonStore
};

render(
  <Provider {...stores}>
    <App />
  </Provider>,
  document.getElementById('root')
);

serviceWorker.unregister();