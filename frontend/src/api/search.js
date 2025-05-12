import request from '@/utils/request';

export const searchNFTs = (keyword) => {
  return request.get('/findNft/search', {
    params: {
      keyword
    }
  });
};