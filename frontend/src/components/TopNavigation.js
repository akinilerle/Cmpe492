import React from 'react';

import { observer } from 'mobx-react';
import { Layout, Menu, Icon } from 'antd';
import { Link } from 'react-router-dom';

@observer(['commonStore'])
export class TopNavigation extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      menuItems: [
        
      ]
    };
  }

  onLanguageClick = ({ item, key }) => {
    this.props.commonStore.setLanguage(key);
  };

  render() {
    const { menuItems } = this.state;
    const language = this.props.commonStore.getLanguage;
    console.log(`language has changed: ${language}`);

    return (
      <Layout.Header style={{ position: 'fixed', zIndex: 1, width: '100%' }}>
        <Menu
          mode="horizontal"
          defaultSelectedKeys={['/admin']}
          style={{ lineHeight: '48px' }}
        >
          <Menu.Item>SonKontrol</Menu.Item>

          {menuItems.map(menuItem =>
            menuItem.isSubMenu ? (
              <Menu.SubMenu
                key={menuItem.label}
                style={{ float: menuItem.float }}
                title={
                  <span className="submenu-title-wrapper">
                    <Icon type={menuItem.icon} />
                    {menuItem.label}
                  </span>
                }
              >
                {menuItem.children.map(sMenuItem => (
                  <Menu.Item
                    key={sMenuItem.label}
                    onClick={e => sMenuItem.onClick && sMenuItem.onClick()}
                  >
                    {sMenuItem.to ? (
                      <Link to={sMenuItem.to}>
                        <Icon
                          style={{
                            color: sMenuItem.iconColor || 'rgba(0, 0, 0, 0.65)'
                          }}
                          type={sMenuItem.icon}
                        />{' '}
                        {sMenuItem.label}
                      </Link>
                    ) : (
                      <>
                        <Icon
                          style={{
                            color: sMenuItem.iconColor || 'rgba(0, 0, 0, 0.65)'
                          }}
                          type={sMenuItem.icon}
                        />{' '}
                        {sMenuItem.label}
                      </>
                    )}
                  </Menu.Item>
                ))}
              </Menu.SubMenu>
            ) : (
              <Menu.Item key={menuItem.label}>
                <Link to={menuItem.to}>
                  <Icon type={menuItem.icon} /> {menuItem.label}
                </Link>
              </Menu.Item>
            )
          )}
        </Menu>
      </Layout.Header>
    );
  }
}
