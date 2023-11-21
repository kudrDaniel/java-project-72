@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.util.DateFormatter
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайты</h1>
        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(int i = 0; i < page.getUrls().size(); i++)
                <tr>
                    <td>
                        ${page.getUrls().get(i).getId()}
                    </td>
                    <td>
                        <a href="${NamedRoutes.urlPath(page.getUrls().get(i).getId())}">${page.getUrls().get(i).getName()}</a>
                    </td>
                    <td>
                        @if(page.getUrls().get(i).getLast() != null)
                            ${DateFormatter.getDateBySlashWithTime(page.getUrls().get(i).getLast().getCreatedAt())}
                    </td>
                    <td>
                        ${page.getUrls().get(i).getLast().getStatus()}
                        @else
                    </td>
                    <td>
                        @endif
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
