@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.DateFormatter
@import hexlet.code.controller.ChecksController
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайт: ${page.getUrl().getName()}</h1>

        <table class="table table-bordered table-hover mt-3">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${DateFormatter.timestampToString(page.getUrl().getCreatedAt(), "dd/MM/yyyy kk:mm")}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="mt-5">Проверки</h2>
        <form method="post" action="${ChecksController.getINSTANCE().checksPath(page.getUrl().getId())}">
            <button type="submit" class="btn btn-primary">Запустить проверку</button>
        </form>

        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th class="col-1">Код ответа</th>
                <th>title</th>
                <th>h1</th>
                <th>description</th>
                <th class="col-2">Дата проверки</th>
            </tr>
            </thead>
            <tbody>
            @for(int i = page.getChecks().size() - 1; i >= 0; i--)
                <tr>
                    <td>
                        ${page.getChecks().get(i).getId()}
                    </td>
                    <td>
                        ${page.getChecks().get(i).getStatus()}
                    </td>
                    <td>
                        ${page.getChecks().get(i).getTitle()}
                    </td>
                    <td>
                        ${page.getChecks().get(i).getHeader()}
                    </td>
                    <td>
                        ${page.getChecks().get(i).getDescription()}
                    </td>
                    <td>
                        ${DateFormatter.timestampToString(page.getChecks().get(i).getCreatedAt(), "dd/MM/yyyy kk:mm")}
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
