# Dispatcher Sorvlet

[![Build Status](https://github.com/JaPangi/dispatcher-sorvlet/actions/workflows/ci.yml/badge.svg)](https://github.com/JaPangi/dispatcher-sorvlet/actions/workflows/ci.yml)
[![](https://jitpack.io/v/JaPangi/dispatcher-sorvlet.svg)](https://jitpack.io/#JaPangi/dispatcher-sorvlet)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Dispatcher So(cket)rvlet  makes socket requests as if using spring web.

> If you're interested in following the project's progress, please press the ⭐ button.

<br/>

## Dependencies
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```
```groovy
dependencies {
    implementation 'com.github.JaPangi:dispatcher-sorvlet:0.2.0'
}
```

<br/>

## How To Use
You can find the full example code in the [repository](https://github.com/JaPangi/dispatcher-sorvlet-example).

### 1. Configuration
~~~java
@Configuration
@EnableSocketServer
public class ApplicationConfig implements SocketServerConfigurer {

    @Override
    public void setup(SocketServerPropertiesRegistry registry) {
        registry
                .scanBasePackage("io/wwan13/dispatchersorvletexample")
                .port(8070);
    }
}
~~~
It can operate as a socket server with just the `@EnableSocketServer` annotation.  
And then, set the port to be used for socket communication and the scan base package.

<br/>

### 2. Socket Controller

**2.1. Controller Class**
~~~java
@SocketController
@RequestMapping(key = "ITEM")
public class ItemController {
    // controller methods ...
}
~~~
Specify that it is a controller using the `@SocketController` annotation.  

If `@RequestMapping` is declared at the class level, this key becomes a common prefix for the keys of the methods below.  
In this case, the request keys of all methods declared in this class start with `ITEM_`.

<br/>

**2.2. Controller Method**
~~~java
@RequestMapping(key = "GET_ALL")
public SocketResponse getItems() {
    // find all items logics
    // ex. itemService.getItems();
    
    GetItemsResponse response = new GetItemsResponse(List.of("item1", "item2"));
    return SocketResponse.success(response);
}
~~~
~~~json
{"key":"ITEM_GET"}
~~~
This method works when a socket message is sent as in the json format request example above.  
It is also possible to call the Service layer or the Repository layer.

> Unfortunately, we are not yet able to read multiple lines of requests at once.    
> So we have to convert the json to a single line and send the request.

<br/>

**2.3. Controller Method With Entered Data**
~~~java
@RequestMapping(key = "MODIFY_{itemId}")
public SocketResponse modifyItem(
        @RequestBody ModifyItemRequest request,
        @KeyParameter Long itemId
) {
    // modify item logics
    // ex. itemService.modifyItem(itemId, request);

    ModifyItemResponse response = new ModifyItemResponse("item");
    return SocketResponse.success(response);
}
~~~
~~~json
{"key":"ITEM_MODIFY_3","body":{"itemName":"dispatcher sorvlet","itemPrice":3700}}
~~~
If you want to receive some data in a request or include a specific variable in the request key, you can use it as follows.

<br/>

### 3. Exception Handlers
~~~java
@SocketControllerAdvice
public class SocketExceptionHandler {

    @ExceptionHandler(support = IllegalStateException.class)
    public SocketResponse handleIllegalStateException(IllegalStateException e) {
        // some exception handling logics
        return SocketResponse.error("IllegalStateException", e.getMessage());
    }
}
~~~
You can also handle exceptions that occur inside the controller like this.

<br/>

### 4. Enjoy Your Socket Programming
Please contact us via the [email](wwan13@naver.com) if an error occurs during use.

<br/>

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

<br/>

## License
This project is licensed under the terms of the [apache 2.0] license.

[apache 2.0]: LICENSE.txt