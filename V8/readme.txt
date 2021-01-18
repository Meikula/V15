上一个版本中我们已经在ClientHandler中完成了响应客户端请求的资源
或响应404的操作.但是代码出现了重复.并且也将响应的具体工作放在了ClientHandler
类中,我们也应当将工作拆分,如HTTPRequest一致.

设计一个类:HttpResponse,响应对象
用该类的每一个实例表示要给客户端发送的一个响应内容.ClientHandler在
处理请求的环节只需要根据处理设置要给客户端响应的内容到响应对象即可.
而发送响应的工作交给响应对象完成

实现:
1:在com.webserver.http包下新建一个类:HTTPResponse
2:在响应对象上定义方法flush,作为发送响应的工作
3:定义好对应的属性,并将发送响应的代码从ClientHandler移动到该类中
4:ClientHandler在处理请求的环节改为设置响应对象,并将第三部发送响应
时调用flush方法即可.














