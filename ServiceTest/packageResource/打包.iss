; 脚本由 Inno Setup 脚本向导 生成！
; 有关创建 Inno Setup 脚本文件的详细资料请查阅帮助文档！

[Setup]
; 注: AppId的值为单独标识该应用程序。
; 不要为其他安装程序使用相同的AppId值。
; (生成新的GUID，点击 工具|在IDE中生成GUID。)
AppId={{642C9D62-82E5-442F-9DA7-9ECDDEBBE408}
AppName=dongluService
AppVersion=1.0
;AppVerName=dongluService 1.0
AppPublisher=东陆高新实业有限公司
AppPublisherURL=http://www.example.com/
AppSupportURL=http://www.example.com/
AppUpdatesURL=http://www.example.com/
DefaultDirName={pf}\dongluService
DefaultGroupName=dongluService
DisableProgramGroupPage=yes
OutputBaseFilename=控制电脑关机服务
Compression=lzma
SolidCompression=yes

[Languages]
Name: "chinesesimp"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "service.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "delete.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "run.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "service.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "service.ini"; DestDir: "{app}"; Flags: ignoreversion
Source: "setip.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "setip.ini"; DestDir: "{app}"; Flags: ignoreversion
Source: "ServiceTest-0.0.1-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "WinRun4J.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
; 注意: 不要在任何共享系统文件上使用“Flags: ignoreversion”

[Icons]
Name: "{group}\dongluService"; Filename: "{app}\service.exe"

[Run]
Filename: "{app}\setip.exe";

