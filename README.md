# crawler


##实现环境：
1. Spring Boot 可用初始化工程

##涉及技术：
1. xpath http://www.w3school.com.cn/xpath/xpath_syntax.asp
2. http请求
3. webmagic   http://webmagic.io/

##主要原理：
1. 爬虫原理：浏览目标网站，获取需要请求url，写入代码，循环发送http请求获取目标生成目标页面并解析
2. 解析html页面 - 使用 xpath定位获取节点元素及内容
3. 针对特定js动态生成的目标获取值 - 可使用java代码实现js实现 获取目标值

##代码工程参考：
实现目标 - 以请求网易考拉获取奶粉,强生沐浴露,保洁产品信息为例

##操作步骤：

执行case - KaoLaPipelineTest
1. KaoLaMilkProcess() - 奶粉 - http://www.kaola.com/category/2620.html
2. KaoLaJohnsonProcess() - 强生 - http://www.kaola.com/brand/1208-5119.html
3. KaoLaPGProcess() - 宝洁 - http://www.kaola.com/brand/1146.html

##更新
20161219 - 添加京东获取宝洁商品信息

20161229 - 重构京东抓取