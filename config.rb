GitHub::Markup.markups.reject! {|markup| markup.regexp.to_s == '(?-mix:adoc|asc(iidoc)?)' }

GitHub::Markup.markup(:asciidoctor, /adoc|asc(iidoc)?/) do |content|
  Asciidoctor::Compliance.unique_id_start_index = 1
  Asciidoctor.convert(content, :safe => :safe, :attributes => %w(showtitle=@ idprefix idseparator=- env=github env-github source-highlighter=html-pipeline))
end

# always include this:
Gollum::Page.send :remove_const, :FORMAT_NAMES if defined? Gollum::Page::FORMAT_NAMES

# remove the original AsciiDoc binding:
Gollum::Markup.formats.delete(:asciidoc)

# and define your own (".asc" is the new primary extension):
Gollum::Markup.formats[:asc] = {
    :name => "AsciiDoc",
    :regexp => /adoc|asc(iidoc)?/
}