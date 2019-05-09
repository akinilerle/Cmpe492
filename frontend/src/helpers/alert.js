import { message } from 'antd';

const success = (alertMessage, duration = 3) => {
  message.success(alertMessage, duration);
};

const error = (alertMessage, duration = 3) => {
  message.error(alertMessage, duration);
};

const info = (alertMessage, duration = 3) => {
  message.info(alertMessage, duration);
};

const warning = (alertMessage, duration = 3) => {
  message.warning(alertMessage, duration);
};

export const alert = {
  success,
  error,
  info,
  warning
};
