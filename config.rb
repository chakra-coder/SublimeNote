# always include this:
Gollum::Page.send :remove_const, :FORMAT_NAMES if defined? Gollum::Page::FORMAT_NAMES

# bind your own extension regex (the new set of extensions will also include `.asc` and `.adoc`):
Gollum::Markup.formats[:asciidoc][:regexp] = /(?:asciidoc|asc|adoc)/
