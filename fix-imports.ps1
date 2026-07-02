# Fix all import statements from com.PS bags.PS bags to com.psbags.PSBags
Get-ChildItem -Path "src\main\java\com\psbags\PSBags" -Recurse -Filter "*.java" | ForEach-Object {
    $content = Get-Content $_.FullName
    $updatedContent = $content -replace 'import com\.garja\.Garja', 'import com.psbags.PSBags'
    Set-Content $_.FullName $updatedContent
    Write-Host "Updated: $($_.Name)"
}

Write-Host "All import statements updated successfully!"
