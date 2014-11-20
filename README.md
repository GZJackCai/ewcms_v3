##介绍
* 网站群管理信息系统，

##与ewcms项目不同之处:
* 不再使用stauts2,转而使用Spring自带的MVC
* Servlet2.5升级到Servlet3.0
* Spring security转换到Apache Shiro
* 把JSON的引用包从jackson切换成fastjson
* 数据库连接池使用Durid
* Tomcat从6.X升级到7.X(8.X对异步消息支持不是很好)
* 把所有JS放到页面底部
* 修改所有Dao，并使Dao继承PagingAndSortingRepository<T, K>接口,简化代码
* 使用动态组合进行查询
* 引入Logback，能打印详细的SQL语句(包括传入的参数值)，而不仅仅只是HQL表达语句