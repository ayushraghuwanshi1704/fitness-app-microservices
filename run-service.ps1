param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    [Parameter(Mandatory=$true)]
    [string]$JarPath
)

$baseDir = $PSScriptRoot
Set-Location -Path $baseDir

if (Test-Path "$baseDir\.env") {
    Get-Content "$baseDir\.env" | Where-Object { $_ -match '^[^#].*=' } | ForEach-Object {
        $parts = $_ -split '=', 2
        if ($parts.Length -ge 2) {
            $k = $parts[0].Trim()
            $v = $parts[1].Trim()
            [System.Environment]::SetEnvironmentVariable($k, $v, 'Process')
        }
    }
}

if (-not (Test-Path "$baseDir\logs")) {
    New-Item -ItemType Directory -Path "$baseDir\logs" -Force | Out-Null
}

$logFile = "$baseDir\logs\$ServiceName.log"
java -jar "$baseDir\$JarPath" *>&1 | Out-File -FilePath $logFile -Encoding utf8
