# MyPermission
权限管理的小总结
解释：MainActivity的是方法一：在项目的gradle文件中加入
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
    在app的gradle中加入依赖
    compile 'com.github.348476129:RxPermission:0.1.0'
    
    方法二：在SolutionOne包中
    
    代码详细
感谢借鉴于:
https://github.com/348476129/RxPermission
