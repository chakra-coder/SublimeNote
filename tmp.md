### Linux
+ **查看CentOS版本**
```bash
 lsb_release -a
```
+ **CentOS上安装Vitualbox**
```bash
yum localinstall VirtualBox-4.3-4.3.36_105129_el5-1.i386.rpm --nogpgcheck
```



# always include this:
Gollum::Page.send :remove_const, :FORMAT_NAMES if defined? Gollum::Page::FORMAT_NAMES

# remove the original AsciiDoc binding:
Gollum::Markup.formats.delete(:asciidoc)

# and define your own (".asc" is the new primary extension):
Gollum::Markup.formats[:adoc] = {
    :name => "AsciiDoc",
    :regexp => /adoc|asc(iidoc)?/
}