package com.gugugua




/**
 * @author cairne huangyihua@diandian.com
 * @date 2012-6-10
 */
class DianlogConverter {
    public static void main(String[] args) {
        if (!args){
            println "Usage: java -cp groovy.jar:dianlog-convertor.jar com.gugugua.DianlogConverter [input dianlog backup file] [output wordpress xml file]";
            println "Or: groovy DianlogConverter [input dianlog backup file] [output wordpress xml file]";
            System.exit(0)
        }
        def dianlogBackup=new XmlSlurper().parse(new File(args[0]));
        def writer = new FileWriter(args[1]);
        writer.write("""<?xml version="1.0" encoding="UTF-8" ?>
<!-- This is a WordPress eXtended RSS file generated by WordPress as an export of your site. -->
<!-- It contains information about your site's posts, pages, comments, categories, and other content. -->
<!-- You may use this file to transfer that content from one site to another. -->
<!-- This file is not intended to serve as a complete backup of your site. -->

<!-- To import this information into a WordPress site follow these steps: -->
<!-- 1. Log in to that site as an administrator. -->
<!-- 2. Go to Tools: Import in the WordPress admin panel. -->
<!-- 3. Install the "WordPress" importer from the list. -->
<!-- 4. Activate & Run Importer. -->
<!-- 5. Upload this file using the form provided on that page. -->
<!-- 6. You will first be asked to map the authors in this export file to users -->
<!--    on the site. For each author, you may choose to map to an -->
<!--    existing user on the site or to create a new user. -->
<!-- 7. WordPress will then import each of the posts, pages, comments, categories, etc. -->
<!--    contained in this file into your site. -->

<!-- generator="WordPress/3.3.1" created="2012-06-10 09:15" -->
<rss version="2.0"
    xmlns:excerpt="http://wordpress.org/export/1.1/excerpt/"
    xmlns:content="http://purl.org/rss/1.0/modules/content/"
    xmlns:wfw="http://wellformedweb.org/CommentAPI/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:wp="http://wordpress.org/export/1.1/"
>
<channel>
    <wp:wxr_version>1.1</wp:wxr_version>
    <wp:base_site_url>http://127.0.0.1/wordpress</wp:base_site_url>
    <wp:base_blog_url>http://127.0.0.1/wordpress</wp:base_blog_url>

    <wp:author><wp:author_id>1</wp:author_id><wp:author_login>admin</wp:author_login><wp:author_email>flashsword20@163.com</wp:author_email><wp:author_display_name><![CDATA[admin]]></wp:author_display_name><wp:author_first_name><![CDATA[]]></wp:author_first_name><wp:author_last_name><![CDATA[]]></wp:author_last_name></wp:author>


    <generator>http://wordpress.org/?v=3.3.1</generator>
""");
        def images = [:]
        //handle images
        dianlogBackup.Images.Image.each{
            images[it.Id.toString()]=it.Url.toString();
        }
        dianlogBackup.Posts.Post.each{
            def pubDate = new Date(it.CreateTime.toLong()).format("EEE, d MMM yyyy HH:mm:ss Z");
            def postDate = new Date(it.CreateTime.toLong()).format("yyyy-MM-dd HH:mm:ss");
            def tags ="";
            def matcher=it.Text.toString()=~/(<img\s*id=\"[^\"]+\")/;
            def text=new RegexReplacer(matcher).replace{
                def id=it=~/id=\"([^\"]+)\"/;
                id.find();
                def src=images[id.group(1)];
                if (src!=null){
                    return "<img src=\"$src\""
                }else {
                    return it;
                }
            }

            it.Tags.Tag.each{ tags+="""<category domain="post_tag" nicename="$it"><![CDATA[$it]]></category>\n"""; }
            def wpitem="""
        <item>
        <title>$it.Title</title>
        <link>http://127.0.0.1/wordpress/?p=$it.Id</link>
        <pubDate>$pubDate</pubDate>
        <dc:creator>admin</dc:creator>
        <guid isPermaLink="false">http://127.0.0.1/wordpress/?p=$it.Id</guid>
        <description></description>
        <content:encoded><![CDATA[$text]]></content:encoded>
        <excerpt:encoded><![CDATA[]]></excerpt:encoded>
        <wp:post_id>$it.Id</wp:post_id>
        <wp:post_date>$postDate</wp:post_date>
        <wp:post_date_gmt>$postDate</wp:post_date_gmt>
        <wp:comment_status>open</wp:comment_status>
        <wp:ping_status>open</wp:ping_status>
        <wp:post_name>$it.Title</wp:post_name>
        <wp:status>publish</wp:status>
        <wp:post_parent>0</wp:post_parent>
        <wp:menu_order>0</wp:menu_order>
        <wp:post_type>post</wp:post_type>
        <wp:post_password></wp:post_password>
        <wp:is_sticky>0</wp:is_sticky>
        $tags
        </item>
        """;
            writer.write(wpitem);
        };
        writer.write("""
</channel>
</rss>
""");
        writer.close();
    }
}
