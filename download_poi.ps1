$urls = @(
    "https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.5/poi-5.2.5.jar",
    "https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.5/poi-ooxml-5.2.5.jar",
    "https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-lite/5.2.5/poi-ooxml-lite-5.2.5.jar",
    "https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.2.0/xmlbeans-5.2.0.jar",
    "https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar",
    "https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.26.1/commons-compress-1.26.1.jar",
    "https://repo1.maven.org/maven2/commons-io/commons-io/2.15.1/commons-io-2.15.1.jar",
    "https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-api/2.22.1/log4j-api-2.22.1.jar"
)

$libDir = "c:\Project_QLK\QLK_JOLLIBEE\lib"
if (-not (Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir
}

foreach ($url in $urls) {
    $fileName = [System.IO.Path]::GetFileName($url)
    $dest = Join-Path $libDir $fileName
    if (Test-Path $dest) {
        Write-Host "$fileName already exists, skipping."
    } else {
        Write-Host "Downloading $fileName from $url..."
        Invoke-WebRequest -Uri $url -OutFile $dest
    }
}
Write-Host "All downloads complete!"
