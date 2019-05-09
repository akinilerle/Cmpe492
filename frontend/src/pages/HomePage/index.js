import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { analyseService } from '../../services';
import reqwest from 'reqwest';
import classnames from 'classnames';
import {
  Alert,
  Button,
  Card,
  Col,
  Form,
  Input,
  Icon,
  Upload,
  Row,
  Tabs
} from 'antd';

import configs from '../../configs';

import './index.css';

const InputGroup = Input.Group;
const TabPane = Tabs.TabPane;


class HomePage extends Component {
  state = {
    filter: 'pending',
    upperTab: 'editor',
    formatTab: 'format1',
    showAlert: false,
    alertMessages: [],
    fileList: [],
    uploading: false
  };

  constructor(props) {
    super(props);
    this.linkRef = React.createRef();
  }

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        let benefit, userStory;

        if(this.state.formatTab === 'format1' && (!values.role1 || !values.request1)){
          alert("Rol ve İstek alanları zorunludur");
          return;
        }
        if(this.state.formatTab === 'format2' && (!values.role2 || !values.request2)){
          alert("Rol ve İstek alanları zorunludur");
          return;
        }

        if (this.state.formatTab === 'format1') {
          benefit = values.benefit1 ? `Böylece ${values.benefit1}.` : '';
          userStory = `Bir ${values.role1} olarak, ${
            values.request1
          } istiyorum. ${benefit}`;
        } else {
          benefit = values.benefit2 ? `${values.benefit2} için, ` : '';
          userStory = `Bir ${values.role2} olarak, ${benefit}${
            values.request2
          } istiyorum.`;
        }

        analyseService
          .check({ userStory })
          .then(r => {
            if (r.data && r.data.messages.length > 0) {
              this.setState({
                showAlert: true,
                alertMessages: r.data.messages
              });
            } else {
              this.setState({
                showAlert: false,
                alertMessages: []
              });
            }
            console.log(r.data);
          })
          .catch(e => {
            console.error(e);
            this.setState({ showAlert: true });
          });

        console.log('Received values of form: ', values);
      }
    });
  };

  changeFormatTab = formatTab => {
    this.setState({ formatTab });
  };

  handleUpload = () => {
    const { fileList } = this.state;
    const formData = new FormData();
    fileList.forEach(file => {
      formData.append('file', file);
    });

    this.setState({
      uploading: true
    });

    reqwest({
      url: configs.upload.txt.endpoint,
      method: 'post',
      processData: false,
      data: formData,
      success: r => {
        this.setState({
          fileList: [],
          uploading: false
        });
        const href = window.URL.createObjectURL(
          new Blob([r.response], { type: 'text/plain'})
        );
        const a = this.linkRef.current;
        a.download = 'kullanici-hikayeleri.' + fileList[0].name.slice(-3);
        a.href = href;
        a.click();
        a.href = '';
      },
      error: (e) => {
        this.setState({
          uploading: false
        });

        console.log('error: ');
        console.log(e.me);
      }
    });
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const {
      uploading,
      fileList,
      upperTab,
      formatTab,
      showAlert,
      alertMessages
    } = this.state;

    const props = {
      onRemove: file => {
        this.setState(state => {
          const index = state.fileList.indexOf(file);
          const newFileList = state.fileList.slice();
          newFileList.splice(index, 1);
          return {
            fileList: newFileList
          };
        });
      },
      beforeUpload: file => {
        this.setState(state => ({
          fileList: [...state.fileList, file]
        }));
        return false;
      },
      fileList
    };

    return (
      <Row
        className="home-page"
        type="flex"
        justify="space-around"
        align="top"
        gutter={8}
      >
        <Col span={12}>
          <Card>
            <div>
              <Tabs
                defaultActiveKey="editor"
                value={upperTab}
                onChange={this.changeFormatTab}
                style={{ height: 700 }}
              >
                <TabPane tab="Editor" key="editor">
                  <Tabs
                    defaultActiveKey="format1"
                    value={formatTab}
                    tabPosition={'left'}
                    onChange={this.changeFormatTab}
                    style={{ height: 500 }}
                  >
                    <TabPane tab="Format 1" key="format1">
                      <Form onSubmit={this.handleSubmit} className="login-form">
                        <Form.Item>
                          <InputGroup compact>
                            <Input
                              style={{ width: 50 }}
                              readOnly
                              defaultValue="Bir"
                            />
                            {getFieldDecorator('role1', {
                              rules: [
                                {
                                  required: false,
                                  message: 'Zorunlu alan'
                                }
                              ]
                            })(<Input style={{ width: 100 }} />)}

                            <Input
                              style={{ width: 100 }}
                              readOnly
                              defaultValue="olarak,"
                            />
                          </InputGroup>
                        </Form.Item>
                        <Form.Item>
                          <InputGroup compact>
                            {getFieldDecorator('request1', {
                              rules: [
                                {
                                  required: false,
                                  message: 'Zorunlu alan'
                                }
                              ]
                            })(<Input 
                              style={{ width: 200 }} 
                              />)}
                            <Input
                              style={{ width: 100 }}
                              readOnly
                              defaultValue="istiyorum."
                            />
                          </InputGroup>
                        </Form.Item>
                        <Form.Item>
                          <InputGroup compact>
                            <Input
                              style={{ width: '20%' }}
                              readOnly
                              defaultValue="Boylece"
                            />
                            {getFieldDecorator('benefit1', {
                              rules: [
                                {
                                  required: false
                                }
                              ]
                            })(<Input style={{ width: 250 }} />)}
                            <Input
                              style={{ width: '30%' }}
                              readOnly
                              placeholder=".]"
                            />
                          </InputGroup>
                          <Input
                            type="hidden"
                            name="formatType"
                            defaultValue="format1"
                          />
                        </Form.Item>
                        <Form.Item>
                          <Button
                            type="primary"
                            htmlType="submit"
                            className="login-form-button"
                          >
                            Gonder
                          </Button>
                          &nbsp;
                          <Button type="danger" className="login-form-button">
                            Kaydet
                          </Button>
                        </Form.Item>

                        {showAlert && (
                          <Alert
                            message="Hata!.."
                            description={
                              <ul>
                                {alertMessages.map(message => (
                                  <li>{message}</li>
                                ))}
                              </ul>
                            }
                            type="error"
                          />
                        )}
                      </Form>
                    </TabPane>
                    <TabPane tab="Format 2" key="format2">
                      <Form onSubmit={this.handleSubmit} className="login-form">
                        <Form.Item>
                          <InputGroup compact>
                            <Input
                              style={{ width: 50 }}
                              readOnly
                              defaultValue="Bir"
                            />
                            {getFieldDecorator('role2', {
                              rules: [
                                {
                                  required: false,
                                  message: 'Zorunlu alan'
                                }
                              ]
                            })(<Input style={{ width: 100 }} />)}

                            <Input
                              style={{ width: 100 }}
                              readOnly
                              defaultValue="olarak,"
                            />
                          </InputGroup>
                        </Form.Item>
                        <Form.Item>
                          <InputGroup compact>
                            {getFieldDecorator('benefit2', {
                              rules: [
                                {
                                  required: false
                                }
                              ]
                            })(<Input style={{ width: 200 }} />)}
                            <Input
                              style={{ width: 50 }}
                              readOnly
                              defaultValue="icin,"
                            />
                          </InputGroup>
                        </Form.Item>
                        <Form.Item>
                          <InputGroup compact>
                            {getFieldDecorator('request2', {
                              rules: [
                                {
                                  required: false,
                                  message: 'Zorunlu alan'
                                }
                              ]
                            })(<Input style={{ width: 200 }} />)}
                            <Input
                              style={{ width: 100 }}
                              readOnly
                              defaultValue="istiyorum."
                            />
                          </InputGroup>
                        </Form.Item>
                        <Form.Item>
                          <Button
                            type="primary"
                            htmlType="submit"
                            className="login-form-button"
                          >
                            Gonder
                          </Button>
                          &nbsp;
                          <Button type="danger" className="login-form-button">
                            Kaydet
                          </Button>
                        </Form.Item>

                        {showAlert && (
                          <Alert
                            message="Hata!.."
                            description={
                              <ul>
                                {alertMessages.map((message, i) => (
                                  <li key={'hata2-' + i}>{message}</li>
                                ))}
                              </ul>
                            }
                            type="error"
                          />
                        )}
                      </Form>
                    </TabPane>
                  </Tabs>
                </TabPane>
                <TabPane tab="Dosya" key="file">
                  <div>
                    <Upload {...props}>
                      <Button>
                        <Icon type="upload" /> Select File
                      </Button>
                    </Upload>
                    <Button
                      type="primary"
                      onClick={this.handleUpload}
                      disabled={fileList.length === 0}
                      loading={uploading}
                      style={{ marginTop: 16 }}
                    >
                      {uploading ? 'Uploading' : 'Start Upload'}
                    </Button>
                    <a ref={this.linkRef} />
                  </div>
                </TabPane>
              </Tabs>
            </div>
          </Card>
        </Col>
      </Row>
    );
  }
}

const WrappedHomePageForm = Form.create({ name: 'homepage' })(HomePage);

export default WrappedHomePageForm;
