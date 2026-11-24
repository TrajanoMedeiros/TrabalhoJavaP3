@echo off
title Iniciar Aluno Online API
cd /d "%~dp0"

echo ===================================================
echo [1/2] Verificando conexao com MySQL na porta 3306...
echo ===================================================

powershell -Command "$c = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue; if ($c.TcpTestSucceeded) { exit 0 } else { exit 1 }"
if %ERRORLEVEL% EQU 0 (
    echo [OK] Servidor MySQL/MariaDB ativo na porta 3306!
) else (
    echo [AVISO] Porta 3306 fechada. Tentando iniciar instancia local...
    if exist "C:\Users\artur\mariadb\mariadb-11.4.5-winx64\bin\mysqld.exe" (
        start /B "" "C:\Users\artur\mariadb\mariadb-11.4.5-winx64\bin\mysqld.exe" "--datadir=C:\Users\artur\mariadb-data" --port=3306 --console
        timeout /t 3 /nobreak > nul
    ) else (
        echo Certifique-se de que o MySQL esta ativo nesta maquina.
    )
)

echo ===================================================
echo [2/2] Iniciando Aplicacao Spring Boot...
echo ===================================================
if not defined JAVA_HOME (
    if exist "C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot" (
        set "JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot"
        set "PATH=C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot\bin;%PATH%"
    )
)

if exist "target\api-0.0.1-SNAPSHOT.jar" (
    echo [INFO] Executando JAR compilado direto (Inicializacao instantanea)...
    java -jar target\api-0.0.1-SNAPSHOT.jar
) else (
    echo [INFO] Executando via Maven Wrapper...
    call mvnw.cmd spring-boot:run
)
pause
