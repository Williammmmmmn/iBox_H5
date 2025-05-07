import request from '@/utils/request';


/**
 * 上传用户头像
 * @param {FormData} formData 包含头像文件的FormData对象
 * @returns {Promise} 包含上传结果的Promise
 */
export const uploadAvatar = async (formData) => {
    try {
      const response = await request.post('/setting/uploadAvatar', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer ' + localStorage.getItem('token') 
        }
      });
      return response;
    } catch (error) {
      console.error('上传头像失败:', error);
      throw error;
    }
  };



  /**
 * 获取用户信息
 * @returns {Promise} 包含用户信息的Promise
 */
export const getUserInfo = async () => {
    try {
      const response = await request.get('/setting/info',{
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token') // 添加 token
        }
      });
      return response;
    } catch (error) {
      console.error('获取用户信息失败:', error);
      throw error;
    }
  };


  /**
 * 更新用户昵称
 * @param {string} nickname 新昵称
 * @returns {Promise} 包含更新结果的Promise
 */
  export const updateNickname = async (nickname) => {
    try {
      const token = localStorage.getItem('token'); // 从存储中获取 token
      const response = await request.post(
        '/setting/updateNickname',
        { nickname },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      return response;
    } catch (error) {
      console.error('更新昵称失败:', error);
      throw error;
    }
  };

  // 发送更换手机号验证码
export const sendChangePhoneCode = (newPhone) => {
  return request.post('/setting/sendChangePhoneCode', 
    { newPhone },
    {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      }
    }
  );
};

// 提交更换手机号请求
export const changePhone = (newPhone, code) => {
  return request.post('/setting/changePhone', 
    { newPhone, code },
    {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      }
    }
  );
};