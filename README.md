ewcms_v3
=================

与ewcms项目不同之处:
1.不再使用stauts2,转而使用Spring自带的MVC;
2.Servlet2.5升级到Servlet3.0;
3.Spring security转换到Apache Shiro;
4.把JSON的引用包从jackson切换成fastjson;
5.数据库连接池使用Durid;
6.Tomcat从6.X升级到7.X(8.X对异步消息支持不是很好);
7.把所有JS放到页面底部;
8.修改所有Dao,并使Dao继承PagingAndSortingRepository<T, K>接口,简化代码;
9.使用动态组合进行查询;
10.引入Logback,能打印详细的SQL语句(包括传入的参数值),而不仅仅只是HQL表达语句.