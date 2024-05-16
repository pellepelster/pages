---
title: "JVM: As time goes by"
date: 2024-05-16T12:00:00+01:00
tags: [ "jvm", "java" ]
---

## I just want to parse a timestamp on the JVM

If you are in a hurry and just want to know which JVM (Java/Kotlin) library can parse which date formats look **-->[here](https://htmlpreview.github.io/?https://github.com/pellepelster/kitchen-sink/blob/master/jvm-time-parsing/report.html)<--** for an overview. Alternatively, read on when you want to know why this page exists...

## But Why?

I have been developing software on the JVM platform roughly 20 years now. Every now and then, I need to parse a date from its text representation into the [seconds since epoch](https://www.epochconverter.com/) or just to have it as an object to do some date arithmetics.
And I can't remember how to do it for the life of me. Maybe it's my age, maybe it's the various approaches of date parsing since Java SE 1.4, or I just have a rare form of dyslexia that interferes with my brain when I try to remember the Java/Kotlin date API.

To solve this problem once and for all, I did what any respectable Java developer would do, and overengineered a solution for this problem ;-).

We can use the work of [Iain MacDonald](https://github.com/IJMacD?tab=repositories&q=rfc&type=&language=&sort=) who listed all possible [RFC 3339 and ISO 8601](https://ijmacd.github.io/rfc3339-iso8601/) formats with a tiring precision as a starting point.

From there we can extract a wide range of interesting datetime representations

```shell
2024-05-16T15:22:07Z
2024-05-16T15:22:07.5Z
2024-05-16T15:22:07.53Z
2024-05-16T15:22:07.534Z
2024-05-16T15:22:07.534635Z
2024-05-16t15:22:07z
2024-05-16t15:22:07.534z
2024-05-16T17:22:07+02:00
[...]
```

That we just need to iterate and feed it into all JVM based parsers that we can find

For example `java.time.Instant`

```kotlin
class Instant : Parser {
    override fun parse(now: String) = listOf(safeParse("java.time.Instant.parse") {
        Instant.parse(now).epochSecond
    })
}
```

or the more interesting `java.time.format.DateTimeFormatter`

```kotlin
class DateTimeFormatter : Parser {
    override fun parse(now: String) = listOf(
        "DateTimeFormatter.BASIC_ISO_DATE" to DateTimeFormatter.BASIC_ISO_DATE,
        "DateTimeFormatter.ISO_LOCAL_DATE" to DateTimeFormatter.ISO_LOCAL_DATE,
        "DateTimeFormatter.ISO_OFFSET_DATE" to DateTimeFormatter.ISO_OFFSET_DATE,
        "DateTimeFormatter.ISO_DATE" to DateTimeFormatter.ISO_DATE,
        "DateTimeFormatter.ISO_LOCAL_TIME" to DateTimeFormatter.ISO_LOCAL_TIME,
        "DateTimeFormatter.ISO_OFFSET_TIME" to DateTimeFormatter.ISO_OFFSET_TIME,
        "DateTimeFormatter.ISO_TIME" to DateTimeFormatter.ISO_TIME,
        "DateTimeFormatter.ISO_LOCAL_DATE_TIME" to DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        "DateTimeFormatter.ISO_OFFSET_DATE_TIME" to DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        "DateTimeFormatter.ISO_ZONED_DATE_TIME" to DateTimeFormatter.ISO_ZONED_DATE_TIME,
        "DateTimeFormatter.ISO_DATE_TIME" to DateTimeFormatter.ISO_DATE_TIME,
        "DateTimeFormatter.ISO_ORDINAL_DATE" to DateTimeFormatter.ISO_ORDINAL_DATE,
        "DateTimeFormatter.ISO_WEEK_DATE" to DateTimeFormatter.ISO_WEEK_DATE,
        "DateTimeFormatter.ISO_INSTANT" to DateTimeFormatter.ISO_INSTANT,
        "DateTimeFormatter.RFC_1123_DATE_TIME" to DateTimeFormatter.RFC_1123_DATE_TIME,
    ).map {
        safeParse(it.first) {
            Instant.from(it.second.parse(now)).epochSecond
        }
    }
}
```

after that we just need some glue code to gather the results and generate a nice overview page

![JVM as times goes by](/img/jvm_as_time_goes_by.png)

If you want to experiment on your own, as always the full source code is available [here](https://github.com/pellepelster/kitchen-sink/tree/master/jvm-time-parsing)

## Results and Findings

Looking at the results there were some interesting findings

For the (relatively common) formats:

* 2024-05-16T15:22:07Z
* 2024-05-16T15:22:07.5Z
* 2024-05-16T15:22:07.53Z
* 2024-05-16T15:22:07.534Z
* 2024-05-16T15:22:07.534635Z
* 2024-05-16t15:22:07z
* 2024-05-16t15:22:07.534z
* 2024-05-16T17:22:07+02:00
* 2024-05-16T17:22:07.534+02:00
* 2024-05-16T17:22:07.534635+02:00
* 2024-05-16T15:22:07-00:00
* 2024-05-16T15:22:07.534-00:00
* 2024-05-17T00:07:07+08:45
* 2024-05-16T15:22:07+00:00
* 2024-05-16T15:22:07.534+00:00
* 2024-05-16T17:22:07+02
* 2024-05-16T17:22:07.5+02
* 2024-05-16T17:22:07.53+02
* 2024-05-16T17:22:07.534+02
* 2024-05-16T17:22:07.534635+02
* 2024-05-16T17:22:07.5+02:00
* 2024-05-16T17:22:07.53+02:00
* 2024-05-16T17:22+02:00
* 2024-05-16T17:22+02
* 2024-05-16T15:22Z

The functions

* `DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(String)`
* `DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(String)`

are able to parse all of them. Second place goes to

* `java.time.Instant.parse(String)`
* `DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(String)`
* `DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(String)`
* `DateTimeFormatter.ISO_DATE_TIME.parse(String)`
* `DateTimeFormatter.ISO_INSTANT.parse(String)`

which also are able to parse the majority. Interestingly `org.joda.time.LocalDateTime.parse(String)` fails at this completely. On the other hand for 

* 2024-05-16T17:22:07
* 2024-05-16T17:22:07.5
* 2024-05-16T17:22:07.53
* 2024-05-16T17:22:07,534
* 2024-05-16T17:22:07.534
* 2024-05-16T17:22:07,534635
* 2024-05-16T17:22:07.534635
* 2024-137T17:22:07
* 2024-137T17:22:07.5
* 2024-137T17:22:07.53
* 2024-137T17:22:07,534
* 2024-137T17:22:07.534
* 2024-137T17:22:07,534635
* 2024-137T17:22:07.534635

`org.joda.time.LocalDateTime.parse(String)` and `java.time.LocalDateTime.parse(String)` excel and all other methods fail. Joda also is better at parsing partials formats like

* 2024-05-16T17
* 2024-05-16T17,3
* 2024-05-16T17.3
* 2024-05-16T17:22
* 2024-05-16T17:22,1 
* 2024-05-16T17:22.1

Of course, you could just specify the format, and instruct the parser to parse it like this `DateTimeFormatter.ofPattern("%Y-%M-%Dt%h:%m:%sz").parse(String)` but apart from only being able to parse this single format, it's only half the fun and I would not be able to rant about JVM date parsing :-)  