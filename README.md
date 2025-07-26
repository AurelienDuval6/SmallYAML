
<p align="center">
  <img src="SmallYAML.svg" width="300" alt="logo">
</p>

<h2 align="center">
Simplified YAML library with limited syntax support and built-in config validation
</h2>

[![](http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-2.png)](http://www.wtfpl.net/)

### Overview

SmallYAML is a simplified YAML library designed with limited syntax support and built-in config validation. It provides a streamlined approach to YAML processing while maintaining essential functionality for most use cases.

### Usage

Take a YAML file :

```yaml
---
data:
- id: 1
  name: Franc
  roles:
    - admin
    - hr
- id: 2
  name: John
  roles:
    - admin
    - finance
```

And play with it :
```java
/**
 * You parse it
 */
var object = YamlParser.parse(inputStream);

/**
 * You use it
 */
var value = object.findValue(YamlPath.of("data", "0", "id"));

/**
 * Value is Optional.of("1")
 */
```

### Limitations

Sky is the limit

### Compatibility

- Uses Java 21
- Compiled with Gradle

### Contributors
- Aurélien Duval ([GitHub](https://github.com/AurelienDuval6))
- Sekelenao ([GitHub](https://github.com/Sekelenao))