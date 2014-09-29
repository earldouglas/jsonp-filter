[![Build Status](https://travis-ci.org/earldouglas/jsonp-filter.svg?branch=travis-ci)](https://travis-ci.org/earldouglas/jsonp-filter)

## Usage

Include jsonp-filter in your *pom.xml*:

```xml
<dependencies>
  <dependency>
    <groupId>com.earldouglas</groupId>
    <artifactId>jsonp-filter</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

Add a `JsonPFilter` to your *web.xml*:

```xml
<filter>
  <filter-name>jsonp-filter</filter-name>
  <filter-class>com.earldouglas.jsonpfilter.JsonPFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>jsonp-filter</filter-name>
  <url-pattern>/foo</url-pattern>
</filter-mapping>
```

Optionally configure the callback parameter name (defaults to *callback*):

```xml
<filter>
  <filter-name>jsonp-filter</filter-name>
  <filter-class>com.earldouglas.jsonpfilter.JsonPFilter</filter-class>
  <init-param>
    <param-name>callbackParam</param-name>
    <param-value>calleybackey</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>jsonp-filter</filter-name>
  <url-pattern>/foo</url-pattern>
</filter-mapping>
```

Now any requests to */foo* will have their responses wrapped in *callback(...)*.  In the second case, responses will be wrapped in *calleybackey(...)*.
