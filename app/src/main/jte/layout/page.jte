@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
    <title>Page analyzer</title>
</head>
<body>
@if(page != null && page.getFlash() != null)
    <div>
        ${page.getFlash()}
    </div>
@endif
${content}
@if(page != null && page.getDate() != null)
    <div>
        ${page.getDate()}
    </div>
@endif
</body>
</html>
