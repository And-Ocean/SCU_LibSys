
### 接口授权

2024/11/18

gy说的那个“添加完新页面后去mock里面授权的地方把对应的路径加上”的用后端接口的话路径是在src\main\java\com\example\backend\controllers\Auth.java，mock文件夹是还没有后端的时候用来临时模拟后端响应的，现在后端搭好了mock就已经弃用了

### 用户信息

2024/11/30

当前登录用户的信息都在localStorage里面，可以直接取用，虽然很不严谨
也可以在sessionStorage里面通过accessToken来取