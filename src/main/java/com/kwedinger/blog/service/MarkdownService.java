package com.kwedinger.blog.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    
    public String renderMarkdown(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        
        Node document = parser.parse(content);
        String html = renderer.render(document);
        
        // Insert spacing divs between paragraphs that were separated by blank lines
        // This preserves the visual spacing of blank lines in the original markdown
        html = html.replaceAll("</p>\\s*(?=<p>)", "</p><div class='h-4'></div>");
        
        return html;
    }
}
