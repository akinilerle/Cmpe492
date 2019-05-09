import React, { Suspense } from 'react';
import { Spin } from 'antd';
import ErrorBoundary from './ErrorBoundary';

export const WaitingComponent = Component => {
  return props => (
    <Suspense
      fallback={
        <Spin
          tip="Loading..."
          size="large"
          style={{ marginLeft: '50%', marginTop: '20%' }}
        />
      }
    >
      <ErrorBoundary>
        <Component {...props} />
      </ErrorBoundary>
    </Suspense>
  );
};
