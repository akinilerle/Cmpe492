import React, { createElement } from 'react';
import { Button, Layout } from 'antd';
import config from './typeConfig';
import styles from './index.css';

class Exception extends React.Component {
  static defaultProps = {
    backText: 'back to home',
    redirect: '/'
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    const {
      backText,
      linkElement = 'a',
      type,
      title,
      desc,
      img,
      actions,
      redirect
    } = this.props;
    const pageType = type in config ? type : '404';
    return (
      <Layout hasSider={false}>
        <Layout.Content
          className="forget-password-page"
          style={{
            alignItems: 'center',
            display: 'flex',
            justifyContent: 'center'
          }}
        >
          <div>
            <div className={styles.imgBlock}>
              <div
                style={{
                  width: 417,
                  height: 81,
                  backgroundImage: `url(${img || config[pageType].img})`
                }}
              />
            </div>
            <div className={styles.content}>
              <h1>{title || config[pageType].title}</h1>
              <div className={styles.desc}>{desc || config[pageType].desc}</div>
              <div className={styles.actions}>
                {actions ||
                  createElement(
                    linkElement,
                    {
                      to: redirect,
                      href: redirect
                    },
                    <Button type="primary">{backText}</Button>
                  )}
              </div>
            </div>
          </div>
        </Layout.Content>
      </Layout>
    );
  }
}

export default Exception;
