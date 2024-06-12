SpongePowered-Mixin-Cute
===
- 实现了对 [Mixin](https://github.com/SpongePowered/Mixin) 系统引导
- 使用了 [Fabric](https://github.com/FabricMC/fabric) 的代码

使用
------------------------
## 有两种使用情况
### 需要被 Mixin 系统修改
需要以这种方式使用该项目的项目，被称为主程序。

你需要添加一个依赖到你的项目
```xml
<dependency>
    <groupId>io.github.gdrfgdrf</groupId>
    <artifactId>spongepowered-mixin-cute-launcher</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```
注意：请确保 scope 的值为 provided

---

在这种情况下，项目的主类必须实现
io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider

您需要在你的项目的资源文件夹中创建一个 program_description.json 文件.  

如果你的项目支持插件

program_description.json 的格式需要如下
```json
{
  "main-class": "实现了 ProgramProvider 的类",
  "plugin-folder": "存储插件的文件夹",
  "plugin-description-file-name": "等下说"
}
```
如果你的项目不支持插件，  
"plugin-folder" 和 "plugin-description-file-name" 是不需要定义的  

---

之后，你需要打包你的项目并重命名 jar 文件为 program.jar  
然后前往本项目的 Release 页面下载 jar 文件，  
之后将 program.jar 直接放入本项目的 jar 文件的根目录  

用户则需要运行本项目的 jar 文件以使用  

### "plugin-description-file-name"
插件的资源文件夹必须包含一个和 "plugin-description-file-name" 的值同名的文件  

"plugin-description-file-name" 文件必须拥有以下内容
```json
{
  "mixins": [
    "test.mixins.json"
  ]
}
```
"mixins" 键必须存储插件的所有 mixin 配置文件

### 需要使用 Mixin 系统
因为你的项目是作为插件给其他项目的，
所以你需要添加 "其他项目" 作为依赖

你需要添加以下依赖
```xml
<dependencies>
    <dependency>
        <groupId>其他项目的 group id</groupId>
        <artifactId>其他项目的 artifact id</artifactId>
        <version>其他项目的版本</version>
    </dependency>

    <dependency>
        <groupId>io.github.gdrfgdrf</groupId>
        <artifactId>spongepowered-mixin-cute-core</artifactId>
        <version>等下说</version>
    </dependency>
</dependencies>
```

### spongepowered-mixin-cute-core 的版本
你可以使用最新版本或和 "其他项目" 相同的版本，  
但为了稳定性，推荐使用和 "其他项目" 相同的版本

然后，你需要遵循这个[规范](#plugin-description-file-name)  
plugin-description-file-name 的值由 "其他项目" 提供  

然后就能正常使用了  

---
再次重复，本项目使用了 [Fabric](https://github.com/FabricMC/fabric) 的代码，  
你需要遵循 [Fabric](https://github.com/FabricMC/fabric) 的协议