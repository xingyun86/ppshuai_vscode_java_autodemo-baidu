==========================================================================
1.安装java:
https://github-production-release-asset-2e65be.s3.amazonawhttps://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_windows_hotspot_8u222b10.msi
https://github-production-release-asset-2e65be.s3.amazonawhttps://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_windows_hotspot_8u222b10.zip

2.配置maven
vscode settings中搜索maven for Java节点项:
"maven.executable.path": ".\\tools\\apache-maven-3.6.3\\bin\\mvn"(此处最好使用绝对路径配置)

3.安装firefox
firefox:http://www.firefox.com.cn/download/

4.配置镜像
找到maven目录下的settings.xml,打开后在mirrors节点添加如下命令:
在mirrors节点中，添加镜像下载地址
```
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```
5.编译打包
mvn clean
mvn package

注意事项：
Windows下需要安装vc2015_redist依赖包：
http://xjp.ppsbbs.tech:8081/foot-wash/storage/app/images/files/vc2015sp3_redist.x64.exe
Linux下安装firefox：
sudo chmod 777 /tmp/log -R
./start.sh
