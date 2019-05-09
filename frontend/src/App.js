import React from 'react';
import { Route, Router, Switch } from 'react-router-dom';
import { observer } from 'mobx-react';

import { history } from './helpers';
import { WaitingComponent } from './components/WaitingComponent';
import { AuthorizedLayout } from './components/AuthorizedLayout';

import Exception from './pages/Exception/';

@observer(['commonStore'])
export default class App extends React.Component {
  render() {
    const { commonStore } = this.props;

    return (
      <Router history={history}>
        <Switch>
          <Route path="/" render={AuthorizedLayout(commonStore)} />

          <Route render={WaitingComponent(Exception)} />
        </Switch>
      </Router>
    );
  }
}
