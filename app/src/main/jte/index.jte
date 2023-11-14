@import hexlet.code.dto.regular.RootPage
@import hexlet.code.util.NamedRoutes
@param RootPage page

@template.layout.page(
page = page,
content = @`
    <div class="mx-auto p-4 py-md-5">
        <main>
            <h1>Hello world!</h1>
        </main>
    </div>
`
)
