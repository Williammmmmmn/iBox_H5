module.exports = {
    plugins: {
        'postcss-px-to-viewport': {
            //vw适配的标准屏宽度是iphonex
            //适配图750，调成一倍=>适配375标准屏
            //适配图610，调成一倍=>适配320标准屏

            'postcss-px-to-viewport': {
                viewportWidth: 750, // 设计稿宽度

            }
        }
    }
}