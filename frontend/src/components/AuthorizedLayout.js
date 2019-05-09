import React, { lazy } from 'react';
import { Route, Switch } from 'react-router-dom';
import { TopNavigation } from './TopNavigation';
import { WaitingComponent } from './WaitingComponent';
import { Layout } from 'antd';
import Exception from '../pages/Exception/';

const HomePage = lazy(() => import('../pages/HomePage/'));

export const AuthorizedLayout = userStore => ({ match: { url } }) => {

  return (
    <Layout hasSider={false}>
      <TopNavigation />

      <Layout.Content style={{ marginTop: 48 }}>
        <Switch>
          <Route exact path={`${url}`} component={WaitingComponent(HomePage)} />

          <Route component={WaitingComponent(Exception)} />
        </Switch>
      </Layout.Content>

      <Layout.Footer>© Copyright 2019 SonKontrol</Layout.Footer>
    </Layout>
  );
};
