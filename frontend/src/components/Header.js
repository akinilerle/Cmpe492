import React from 'react';
import { observer } from 'mobx-react';
import { Avatar, Layout, Dropdown, Icon, Menu, Tag, Tooltip } from 'antd';
import NoticeIcon from 'ant-design-pro/lib/NoticeIcon';
import HeaderSearch from 'ant-design-pro/lib/HeaderSearch';
import moment from 'moment';
import groupBy from 'lodash/groupBy';

const data = [
  {
    id: '000000001',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
    title: '你收到了 14 份新周报',
    datetime: '2017-08-09',
    type: '通知'
  },
  {
    id: '000000002',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png',
    title: '你推荐的 曲妮妮 已通过第三轮面试',
    datetime: '2017-08-08',
    type: '通知'
  },
  {
    id: '000000003',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png',
    title: '这种模板可以区分多种通知类型',
    datetime: '2017-08-07',
    read: true,
    type: '通知'
  },
  {
    id: '000000004',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
    title: '左侧图标用于区分不同的类型',
    datetime: '2017-08-07',
    type: '通知'
  },
  {
    id: '000000005',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
    title: '内容不要超过两行字，超出时自动截断',
    datetime: '2017-08-07',
    type: '通知'
  },
  {
    id: '000000006',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
    title: '曲丽丽 评论了你',
    description: '描述信息描述信息描述信息',
    datetime: '2017-08-07',
    type: '消息',
    clickClose: true
  },
  {
    id: '000000007',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
    title: '朱偏右 回复了你',
    description: '这种模板用于提醒谁与你发生了互动，左侧放『谁』的头像',
    datetime: '2017-08-07',
    type: '消息',
    clickClose: true
  },
  {
    id: '000000008',
    avatar:
      'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
    title: '标题',
    description: '这种模板用于提醒谁与你发生了互动，左侧放『谁』的头像',
    datetime: '2017-08-07',
    type: '消息',
    clickClose: true
  },
  {
    id: '000000009',
    title: '任务名称',
    description: '任务需要在 2017-01-12 20:00 前启动',
    extra: '未开始',
    status: 'todo',
    type: '待办'
  },
  {
    id: '000000010',
    title: '第三方紧急代码变更',
    description: '冠霖提交于 2017-01-06，需在 2017-01-07 前完成代码变更任务',
    extra: '马上到期',
    status: 'urgent',
    type: '待办'
  },
  {
    id: '000000011',
    title: '信息安全考试',
    description: '指派竹尔于 2017-01-09 前完成更新并发布',
    extra: '已耗时 8 天',
    status: 'doing',
    type: '待办'
  },
  {
    id: '000000012',
    title: 'ABCD 版本发布',
    description: '冠霖提交于 2017-01-06，需在 2017-01-07 前完成代码变更任务',
    extra: '进行中',
    status: 'processing',
    type: '待办'
  }
];

@observer(['commonStore'])
export default class Header extends React.Component {
  onItemClick = (item, tabProps) => {
    console.log(item, tabProps);
  };

  onClear = tabTitle => {
    console.log(tabTitle);
  };

  getNoticeData = notices => {
    if (notices.length === 0) {
      return {};
    }
    const newNotices = notices.map(notice => {
      const newNotice = { ...notice };
      if (newNotice.datetime) {
        newNotice.datetime = moment(notice.datetime).fromNow();
      }
      // transform id to item key
      if (newNotice.id) {
        newNotice.key = newNotice.id;
      }
      if (newNotice.extra && newNotice.status) {
        const color = {
          todo: '',
          processing: 'blue',
          urgent: 'red',
          doing: 'gold'
        }[newNotice.status];
        newNotice.extra = (
          <Tag color={color} style={{ marginRight: 0 }}>
            {newNotice.extra}
          </Tag>
        );
      }
      return newNotice;
    });
    return groupBy(newNotices, 'type');
  };

  onMenuClick = e => {};

  onLanguageClick = ({ item, key }) => {
    this.props.commonStore.setLanguage(key);
  };

  render() {
    const noticeData = this.getNoticeData(data);
    const { commonStore } = this.props;
    const menu = (
      <Menu selectedKeys={[]} onClick={this.onMenuClick}>
        <Menu.Item key="userCenter">
          <Icon type="user" />
          account center
        </Menu.Item>
        <Menu.Item key="userinfo">
          <Icon type="setting" />
          account settings
        </Menu.Item>
        <Menu.Item key="triggerError">
          <Icon type="close-circle" />
          Trigger Error
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item key="logout">
          <Icon type="logout" />
          Logout
        </Menu.Item>
      </Menu>
    );
    const languages = (
      <Menu
        selectedKeys={[this.props.commonStore.getLanguage]}
        onClick={this.onLanguageClick}
      >
        <Menu.Item key="en">
          <>🇬🇧 English</>
        </Menu.Item>
        <Menu.Item key="de">
          <>🇩🇪 German</>
        </Menu.Item>
        <Menu.Item key="ru">
          <>🇷🇺 Russian</>
        </Menu.Item>
      </Menu>
    );

    return (
      <Layout.Header style={{ background: '#fff', padding: 0 }}>
        <Icon
          className="trigger"
          type={commonStore.menuCollapsed ? 'menu-unfold' : 'menu-fold'}
          onClick={e => commonStore.toggleMenu()}
        />

        <div style={{ float: 'right' }}>
          <HeaderSearch
            placeholder="Search"
            dataSource={['Candidate1', 'Pool1', 'Position1']}
            onSearch={value => {
              console.log('input', value);
            }}
            onPressEnter={value => {
              console.log('enter', value);
            }}
          />

          <Tooltip title="Tooltip" className="header-tooltip">
            <a
              target="_blank"
              href="https://pro.ant.design/docs/getting-started"
              rel="noopener noreferrer"
              title="Title"
            >
              <Icon type="question-circle-o" />
            </a>
          </Tooltip>

          <NoticeIcon
            className="notice-icon"
            count={5}
            onItemClick={this.onItemClick}
            onClear={this.onClear}
          >
            <NoticeIcon.Tab
              list={noticeData['通知']}
              title="通知"
              emptyText="你已查看所有通知"
              emptyImage="https://gw.alipayobjects.com/zos/rmsportal/wAhyIChODzsoKIOBHcBk.svg"
            />
            <NoticeIcon.Tab
              list={noticeData['消息']}
              title="消息"
              emptyText="您已读完所有消息"
              emptyImage="https://gw.alipayobjects.com/zos/rmsportal/sAuJeJzSKbUmHfBQRzmZ.svg"
            />
            <NoticeIcon.Tab
              list={noticeData['待办']}
              title="待办"
              emptyText="你已完成所有待办"
              emptyImage="https://gw.alipayobjects.com/zos/rmsportal/HsIsxMZiWKrNUavQUXqx.svg"
            />
          </NoticeIcon>

          <Dropdown overlay={menu}>
            <span className="header-avatar">
              <Avatar
                size="small"
                src="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png"
                alt="avatar"
              />
              <span>{fullName}</span>
            </span>
          </Dropdown>

          <Dropdown overlay={languages} className="header-language">
            <Icon type="global" />
          </Dropdown>
        </div>
      </Layout.Header>
    );
  }
}
