package gg.jte.generated.ondemand.layout;
import hexlet.code.dto.BasePage;
import gg.jte.Content;
public final class JtepageGenerated {
	public static final String JTE_NAME = "layout/page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,16,16,16,18,18,18,20,20,21,21,21,22,22,24,24,24,26,26,29};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content, BasePage page) {
		jteOutput.writeContent("\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"utf-8\"/>\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css\"\r\n          rel=\"stylesheet\"\r\n          integrity=\"sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We\"\r\n          crossorigin=\"anonymous\">\r\n    <title>Page analyzer</title>\r\n</head>\r\n<body>\r\n");
		if (page != null && page.getFlash() != null) {
			jteOutput.writeContent("\r\n    <div>\r\n        ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getFlash());
			jteOutput.writeContent("\r\n    </div>\r\n");
		}
		jteOutput.writeContent("\r\n");
		jteOutput.setContext("body", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n");
		if (page != null && page.getDate() != null) {
			jteOutput.writeContent("\r\n    <div>\r\n        ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getDate());
			jteOutput.writeContent("\r\n    </div>\r\n");
		}
		jteOutput.writeContent("\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		BasePage page = (BasePage)params.getOrDefault("page", null);
		render(jteOutput, jteHtmlInterceptor, content, page);
	}
}
