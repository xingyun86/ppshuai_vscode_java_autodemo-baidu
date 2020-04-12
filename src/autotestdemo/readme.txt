==========================================================================
1.安装java:
https://github-production-release-asset-2e65be.s3.amazonawhttps://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_windows_hotspot_8u222b10.msi
https://github-production-release-asset-2e65be.s3.amazonawhttps://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_windows_hotspot_8u222b10.zip

2.配置maven
vscode settings中搜索maven for Java节点项:
"maven.executable.options": "assembly:assembly"
"maven.executable.path": ".\\tools\\apache-maven-3.6.3\\bin\\mvn"(此处最好全路径配置)

>cd tools\lib
>mvn install:install-file -Dfile=selenium-server-standalone-3.141.59.jar -DgroupId=selenium-server-standalone -DartifactId=selenium-server-standalone -Dversion=3.141.59 -Dpackaging=jar      

3.安装firefox
firefox:http://www.firefox.com.cn/download/

4.编译打包
package
或
assembly:assembly