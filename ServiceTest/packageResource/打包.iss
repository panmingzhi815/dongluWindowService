; �ű��� Inno Setup �ű��� ���ɣ�
; �йش��� Inno Setup �ű��ļ�����ϸ��������İ����ĵ���

[Setup]
; ע: AppId��ֵΪ������ʶ��Ӧ�ó���
; ��ҪΪ������װ����ʹ����ͬ��AppIdֵ��
; (�����µ�GUID����� ����|��IDE������GUID��)
AppId={{642C9D62-82E5-442F-9DA7-9ECDDEBBE408}
AppName=dongluService
AppVersion=1.0
;AppVerName=dongluService 1.0
AppPublisher=��½����ʵҵ���޹�˾
AppPublisherURL=http://www.example.com/
AppSupportURL=http://www.example.com/
AppUpdatesURL=http://www.example.com/
DefaultDirName={pf}\dongluService
DefaultGroupName=dongluService
DisableProgramGroupPage=yes
OutputBaseFilename=���Ƶ��Թػ�����
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
; ע��: ��Ҫ���κι���ϵͳ�ļ���ʹ�á�Flags: ignoreversion��

[Icons]
Name: "{group}\dongluService"; Filename: "{app}\service.exe"

[Run]
Filename: "{app}\setip.exe";

