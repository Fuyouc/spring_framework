#   1、使用前置

- **注意：由于JDK原因，在编译后所有的参数名都会变成arg1,arg2等，这个时候反射就没办法正常使用，需要进行配置** 

  - 如果使用的是`普通Java项目`，需要在 **File->Settings->Build,Execution,Deployment->Compiler->Java Compiler** 中的 **Additional command line parameters** 下方输入框加上  `-parameters`

  - 如果是一个`Maven项目`，需要在**pom.xml**文件中添加以下内容：

  - ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version> <!--maven版本-->
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
            <encoding>UTF-8</encoding>
            <compilerArgs>
                <arg>-parameters</arg> <!--加上这一行-->
            </compilerArgs>
        </configuration>
    </plugin>
    ```

1、在`GitHub`中下载`spring_framework.jar`，引入到项目列表中，**通过IDEA 中的 `File->Project Structure->Libraies `引入即可。**

2、创建启动类，启动类中实现`main方法`，在启动类上标志`@SpringBootApplication`注解，然后在main方法中调用`SpringApplication.run(启动类.class,args)`方法即可启动Spring服务

- ```java
  @SpringBootApplication
  public class API {
      public static void main(String[] args) {
          SpringApplication.run(API.class,args);
     }
  }
  ```

# 2、功能介绍

## 1、控制反转

框架内部使用`IOC`容器来帮助我们管理所有的对象，我们不需要再通过`new`的方式去创建对象，而是通过配置，将对象注入到容器中，再从`IOC`容器中找到我们需要的对象即可。这样子减少了代码的耦合度。

`IOC`容器将每个对象视为`Bean`，管理每个Bean的创建与销毁。

**框架提供了以下几个注解来实现创建对象：**

| 注解                      | 功能介绍                                                     |
| ------------------------- | ------------------------------------------------------------ |
| @Component                | 在类上方使用，在启动时会扫描该类并将类注入到IOC容器中        |
| @Service                  | 在类上方使用，表示当前类是一个`Service`，注入到IOC容器中     |
| @Controller               | 在类上方使用，表示当前类是一个`Controoler`，注入到IOC容器中  |
| @Configuration            | 在类上方使用，表示当前是一个配置类                           |
| @Bean                     | 在配置类的方法上使用，在启动时，会执行该方法，将方法的返回结果视为Bean注入到容器中 |
| @Conditional              | 在类的上方或者配置类的方法上使用，如果容器中存在指定`Bean`，才将该类注入到容器中 |
| @ConditionalOnMissingBean | 在类的上方或者配置类的方法上使用，如果容器中不存在指定`Bean`，才将该类注入到容器中 |

## 2、依赖注入

在将对象交给IOC容器管理后，**如果想获取或者使用指定对象**，则可以通过依赖注入的方式来解决。

**在想使用依赖注入的功能时，应该要确保当前类是一个Bean**

框架提供了以下几种进行依赖注入的注解：

| 注解           | 功能介绍                                                     |
| -------------- | ------------------------------------------------------------ |
| **@Autowired** | 在Bean的字段上标志，**会优先根据字段名进行查找，如果查找不到，则根据字段的类型进行查找**。允许的类型有：**目标类、List<目标类>、Map<String,目标类>**。 |
| @PostConstruct | 在`Bean`中的方法中使用，会在Bean初始化后，调用该方法。根据参数类型进行依赖注入，会优先根据参数名进行查找，再根据参数的类型进行查找。允许的类型：**List、Object** |

## 3、动态代理

**框架对`JDK`动态代理进行了封装。并且提供了十分强大的功能**

**框架提供了以下注解来使用动态代理的功能：**

| 注解    | 功能介绍                                                     |
| ------- | ------------------------------------------------------------ |
| @Advice | **在bean类上使用，表示开启该类的动态代理功能，需要该类实现任意一个接口** |
| @Before | 在被@Advice标记的bean中的方法上使用，表示在执行该方法之前，会先执行@Before指定的方法 |
| @After  | 在被@Advice标记的bean中的方法上使用，在执行完方法后，会执行@After指定的方法 |

### @Before

**在目标方法的上方使用，会在执行该方法之前，执行@before指定的方法**

**@Before注解提供了以下属性：**

- `value`：**方法名称。**如果需要引用外部的方法，则需要使用`全限定类名 方法名`，否则默认在本类中查找
- `methodParameterType`：**方法参数列表，需要在before方法上声明参数。**`Before`方法可以获取目标方法的参数列表，但是顺序要与目标方法从左往右对应上
- `intercept`：**开启拦截功能**。根据before方法返回的boolean类型进行判断，如果返回`true`则表示放行执行目标方法，否则拦截目标方法
- `invokeMethod`：**拦截后的处理方法。**如果目标方法被before拦截，则执行这里的方法。要求跟before的方法参数保存一致

### @After

**在目标方法的上方使用，会在执行该方法后，执行@before指定的方法**

**@After注解提供了以下属性：**

- `value`：**方法名称。**与`@Before`中的value功能一致
- `methodParameterType`：**方法参数列表，需要在After方法上声明参数。**
- `result`：**接收目标方法执行完后的返回值。参数列表要与目标方法一样，并且需要在methodParameterType中声明类型**

## 4、配置文件

框架在启动时，会扫描`src/resources/application.propertips`配置文件，配置文件使用`key=value`来设置配置项。

**框架提供了以下注解来获取配置文件的内容：**

| 注解                     | 功能介绍                                                     |
| ------------------------ | ------------------------------------------------------------ |
| @ConfigurationProperties | 在bean类上使用，`prefix`表示Key的前缀，类中的字段则根据字段名拼接在后面 |
| @Value                   | 在bean的字段上使用，`value`为配置文件中的key，`prefix`属性表示如果类上存在@ConfigurationProperties注解，则会拼接在前面 |

**注意：如果字段采用的是驼峰命名，则配置文件中的key需要使用`-`进行拼接（如：userName=user-name）**

**配置文件中的value可以是基础数据类型，也可以是枚举（枚举值要对应）**

**常用的配置项：**

```properties
#服务器配置
#服务端口
spring.server.port=
#服务名称
spring.server.serverName=

#配置JDBC
#数据源
spring.datasource.dataSource=MYSQL
#JDBC引擎
spring.datasource.driver=
#数据库URL
spring.datasource.url=
#用户名
spring.datasource.username=
#密码
spring.datasource.password=
#是否开启JDBC
spring.datasource.enable=
#是否打印SQL日志
spring.datasource.log=
#是否开启驼峰命名
spring.datasource.map-underscore-to-camel-case=

#Web配置
#静态资源访问前缀
spring.web.resources.prefix=
#是否允许访问静态资源
spring.web.resources.enable=
#静态资源存储路径
spring.web.resources.location=
#文件上传大小（默认1MB）
spring.web.resources.file-max-size=

#数据加密配置
#公钥配置
spring.security.data.public-key=
#私钥配置
spring.security.data.private-key=
#初始化秘钥长度
spring.security.data.initialize=
#配置秘钥生成的盐（防止重复秘钥）
spring.security.data.salt=
```

## 5、JDBC

框架封装了`JDBC`，通过`Dao接口`来执行SQL语句。降低代码的耦合度，大大提升代码的可读性。

可以通过配置文件配置`JDBC`：

```properties
#数据源
spring.datasource.dataSource=MYSQL
#JDBC引擎
spring.datasource.driver=
#数据库URL
spring.datasource.url=
#用户名
spring.datasource.username=
#密码
spring.datasource.password=
#是否开启JDBC
spring.datasource.enable=
#是否打印SQL日志
spring.datasource.log=
#是否开启驼峰命名
spring.datasource.map-underscore-to-camel-case=
```

框架提供以下注解来实现`JDBC`的使用：

| 注解        | 功能介绍                                                     |
| ----------- | ------------------------------------------------------------ |
| @Mapper     | 在接口上使用，表示当前接口是`Dao`接口，可以在该接口的方法中使用`SQL`注解来完成SQL操作 |
| @Insert     | 插入语句，在`Dao`接口的方法中使用                            |
| @Delete     | 删除语句，在`Dao`接口的方法中使用                            |
| @Update     | 修改语句，在`Dao`接口的方法中使用                            |
| @Select     | 查询语句，在`Dao`接口的方法中使用                            |
| @Param      | 在SQL方法的参数中使用，将该参数与`#{}`进行绑定               |
| @TableField | 在实体类的字段上使用，与数据库的字段进行绑定                 |

```java
/**
 * 表示当前接口为Dao接口，通过动态代理生成一个Bean交给IOC容器进行管理
 */
@Mapper
public interface UserDao {
    /**
     * 表示当前方法为添加方法
     */
    @Insert(value = "INSERT INTO user VALUES(#{user.name},#{user.pwd})")
    int add(User user);

    /**
     * 表示当前方法为查询方法
     * 使用@Param注解让参数id与#{id}进行绑定
     * 查询方法的返回值支持以下几种类型：
     *  - 实体类
     *  - List<实体类>
     *  - Map<String,Object>
     *  - List<Map<String,Object>>
     */
    @Select("SELECT * FROM user WHERE id=#{id}")
    Map<String,Object> getUserById(@Param("id") int id);
}
```

### Mapper映射文件

由于在接口上使用`注解`对于SQL的编写有极大的限制，所以推出了使用`xml`文件进行SQL编写。xml文件可以大大增强平常的业务逻辑SQL，并且支持动态SQL的操作。

**映射文件可以存放在启动类下的任意位置。**

需要注意的是：**如果接口上有增删改查的注解，那么会执行注解上的SQL，而不是执行映射文件的**

#### **xml文件介绍**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  在最外层使用 <mapper> 标签包裹，表明这是一个SQL映射文件
  namespace：告诉框架所映射的DAO接口（全限定类名）
-->
<mapper namespace="com.cx.dao.UserDao">
    <!--
      SQL映射文件统一也需要使用以下四个标签来表明SQL的类型
      以下四个标签必须指定id，id为Dao接口的方法名称
      框架会根据名称进行映射处理
    -->
    <insert id="Dao接口方法名称">
        <!--编写插入SQL逻辑-->
    </insert>
    
    <delete id="Dao接口方法名称">
        <!--编写删除SQL逻辑-->
    </delete>
    
    <update id="Dao接口方法名称">
        <!--编写修改SQL逻辑-->
    </update>
    
    <select id="">
        <!--编写查询SQL逻辑-->
    </select>
</mapper>
```

#### **Select标签介绍**

```xml
<!--
   类的字段与SQL字段映射
-->
<resultMap id="result">
    <!--
      使用 <result> 来指明映射规则
      property：类的字段名
      column：数据库的字段名
      例如：
         property="name" column="user_name"
         会把数据库中 user_name 字段映射到类上的 name 字段
    -->
    <result property="" column=""></result>
</resultMap>

<!--
   resultMap：指定映射逻辑。
              在查询结束后，会将结果根据Dao接口方法的返回值进行封装返回，如果不指定，默认使用类的字段名
              如果指定，则引用 <resultMap> 的id属性即可
-->
<select id="" resltMap="result">
    <!--编写查询SQL逻辑-->
</select>
```

#### 动态SQL

`xml`文件不仅仅只支持普通SQL的编写，还支持编写动态的SQL语句，根据条件判断来生成对应的SQL

注意：**在使用动态SQL标签时，如果需要使用到布尔表达式，并且想使用Dao方法的参数列表的话，就需要与参数名对应，如果参数是对象，则需要在前面加上`$`来区分。**

- 如：

  - ```java
    @Mapper
    public interface UserDao{
     User getUser(@Param("name") String name,@Param("user") User user);
    }
    ```

  - ```xml
    <mapper namespace="xxx.UserDao">
      <select id="getUser">
        SELECT * FROM table_name 
        <!--如果参数中的name != null，则并且该语句到SQL中-->
        <if text="name != null">
          WHERE user_name = #{name}
        </if>
        <!--如果引用了对象的参数，则需要再前面使用$来表示这是一个引用，在后面.字段名称-->
        <if text="$user.age > 18">
          AND #{user.age} > 18
        </if>
      </select>
    </mapper>
    ```



##### if

- 介绍：使用逻辑判断动态的为SQL语句添加新SQL

- 属性：

  - `text`：布尔表达式，根据该表达式校验是否需要添加if语句块中的SQL

- 示例：

  - ```xml
    <mapper namespace="xxx.UserDao">
      <select id="getUser">
        SELECT * FROM table_name 
        <if text="name != null">
          WHERE user_name = #{name}
        </if>
        <if text="$user.age > 18">
          AND #{user.age} > 18
        </if>
      </select>
    </mapper>
    ```



##### switch

- 介绍：同Java语言的where，根据条件来决定添加哪条分支的SQL

- 属性：

  - `text`：布尔表达式，根据该表达式校验是否需要添加case语句块中的SQL

- 子标签：

  - `case`：同`Java`的case语句，表示一个分支，只能满足一个case的条件

- 示例：

  - ```xml
    <select id="getUser">
        SELECT * FROM user WHERE 
        <switch>
            <case text="name != null">
                user_name = #{name}
            </case>
            <case text="uid != null">
                uid = #{uid}
            </case>
            <default>
                xxx = #{xxx}
            </default>
        </switch>
    </select>
    ```



##### where

- 介绍：使用传统的方式拼接 where 子句存在一些问题，比如需要判断每个条件是否满足，以及在不同条件组合时需要添加适当的逻辑操作符（如 AND 或 OR）。而 `<where>` 标签正是为了解决这些问题而设计的。

- 子标签：

  - `if`：根据if的判断来决定是否动态添加该SQL（如果if语句块的SQL前面有一些逻辑操作符，在进行第一次添加时，会自动将该操作符抹去）。

- 示例：

  - 

  - ```xml
    <select id="getUser">
        SELECT * FROM user
        <!--
          使用 where 标签，动态的生成 WHERE语句
          如果没有一个if标签的条件满足，则不会生成 where 语句
          如果某个if标签是第一个条件满足，但是它的语句中有逻辑操作符，底层会自动抹除这个操作符
        -->
        <where>
            <if text="name != null">
                and user_name = #{name}
            </if>
            <if text="pwd != null">
                and pass_word = #{pwd}
            </if>
        </where>
    </select>
    ```



##### include

- 介绍：可以将SQL语句写在外边，通过该标签引入，减少冗余

- 属性：

  - `refid`：需要引入的`<sql>`的id

- 示例：

  - 

  - ```xml
    <select id="removeUsers">
        <!--引入SQL片段-->
        <include refid="sql"/>
    </select>
    <sql id="sql">
        DELETE FROM user
    </sql>
    ```



##### foreach

- 介绍：**便利集合。循环动态添加SQL。可以用于执行一些批量操作**

- 属性：

  - `collection`：Dao方法参数中集合对应的key
  - `item`：为集合中每个元素进行命名。（如果是引用类型，则需要使用item.属性来获取该值）
  - `open`：起始符号。默认为`(`
  - `close`：结束符号。默认为`)`
  - `separator`：分隔符。默认为`,`

- 示例：

  - ```xml
    <!--
      以下是进行批量删除的操作
    -->
    <delete id="removeAll">
        DELETE FROM user WHERE id IN
        <foreach collection="list" item="id" open="(" close=")" separator=",">
            #{id}
        </foreach>
    </delete>
    ```



**注意：框架暂时没有实现对事务处理的功能，请等待后续版本中更新。**

## 6、Web

### 服务器

- 框架内部默认集成的服务器为`Tomcat`，通过`SpringApplication.run`方法启动后，Tomcat服务器也会相互启动

- 框架在配置文件中提供了以下配置，方便开发者进行配置服务器：

  - ```properties
    #服务端口
    spring.server.port=
    #服务名称
    spring.server.serverName=
    ```

- **自定义服务器：**

  - ```java
    /**
     * 如果需要自定义服务器，则需要向容器中注入一个新的 SpringWebServerManager即可
     */
    @Bean
    public SpringWebServerManager s
        pringWebServerManager(){
        return new SpringWebServerManager() {
            @Override
            public void start() {
                //启动服务器
            }
    
            @Override
            public void close() {
                //关闭服务器
            }
    
            @Override
            public String getServerName() {
                //服务器名称
                return null;
            }
        };
    }
    ```

### Servlet

**框架封装了原生的`Servlet`，简化了配置，大大降低了开发流程。**

**使用`@Controller`与`@RequestMapping`即可完成API开发**

```java
/**
 * 使用@Controller表示当前类为Controller
 */
@Controller
public class UserController {
    /**
     * @RequestMapping 根据访问的URI来映射到Controller的方法上
     * value：接收的请求地址（如：localhost/web）会被该方法接收
     * 默认允许GET与POST请求，如果需要指定，则使用method属性来指定
     */
    @RequestMapping(value = "/web",method = HttpRequestMethod.GET)
    public void http(){
        System.out.println("接收到网络请求");
    }
}
```

#### 方法参数

**框架封装了网络请求时携带的参数，帮助开发人员更简易的获取需要的参数类型，为此提供了以下几种注解：**

##### @RequestParam

- 从`GET`的请求参数中获取值（如果方法参数上没有使用注解，默认情况下使用`@RequestParam`）

##### @RquestPart

- 从`POST`请求中获取参数，针对请求类型`multipart/form-data`和`application/x-www-form-urlencoded`

##### @RequestBody

- 从`POST`的请求体中获取参数，针对请求类型`application/json`

##### @RequestCookie

- 获取请求时的`Cookie`

##### @RequestHeader

- 获取请求头列表

#### JSON返回值

**框架在执行完Servletr的方法后，会将返回值转换成`JSON`格式交给客户端。**

提供了以下注解来使用：

| 注解        | 功能介绍                                           |
| ----------- | -------------------------------------------------- |
| @JsonIgnore | 在实体类的字段中表示，表示在转换JSON时，忽略该字段 |

#### 文件上传

框架支持客户端发送文件上传请求，要求必须为`POST`，并且类型为`multipart/form-data`，才能接收到该文件。

```java
/**
 * 单个文件上传请求（POST请求）
 * 使用@RequestPart来接收指定文件，会封装成 MultipartFile 文件对象
 */
@RequestMapping(value = "/upload",method = HttpRequestMethod.POST)
public String upload(@RequestPart("file")MultipartFile file){
    return "上传成功";
}

/**
 * 多个文件上传请求（POST请求）
 * 可以使用uploadMultipleFile属性来指定是否可以上传多个文件（默认为true）
 * 在参数上使用List<MultipartFile>来接收
 */
@RequestMapping(value = "/upload",method = HttpRequestMethod.POST)
public String uploadList(@RequestPart(value = "file",uploadMultipleFile = true) List<MultipartFile> file){
    return "上传成功";
}
```

#### 过滤器

**框架提供了对于`HTTP`请求的过滤器，可以根据指定条件过滤掉一些非法请求**

```java
/**
 * 自定义过滤器，实现 SpringHttpFilterHandler 接口
 * 将自定义的过滤器注入到IOC容器中
 */
@Component
public class MyFilter implements SpringHttpFilterHandler {
    /**
     * 过滤器，在拦截器之前执行
     * 如果返回true，则表示过滤本次请求
     */
    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
```

#### 拦截器

**框架提供了对于`HTTP`请求的拦截器，拦截器是在`Controller`方法执行前后执行，可以在此进行数据加密或者解密**

```java
/**
 * 自定义拦截器。在过滤器之后执行，将自定义的拦截器注入到IOC容器中
 * 使用@WebInterceptor注解，来指定该拦截器拦截的请求
 */
@WebInterceptor("/user")
@Component
public class MyInterceptor implements SpringWebInterceptor {
    /**
     * 在Controller方法执行前执行
     * @return false表示放行，true表示拦截
     */
    @Override
    public boolean beforeHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method, Object controller) {
        return false;
    }

    /**
     * 在Controller方法执行后执行
     * @return 返回false表示使用框架内部的默认响应结果，返回true表示自定义响应结果，不采用框架内部
     */
    @Override
    public boolean afterHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method, Object result) {
        return false;
    }
}
```

#### 异常处理

##### Controller异常

**框架允许用户自定义异常处理器去捕获`Controller`所抛出的异常。可以根据不同的异常信息全局响应不同的状态。**

```java
/**
 * 使用@ControllerAdvice表示该类是一个Controller异常处理器
 */
@Component
@ControllerAdvice
public class MyControllerExceptionHandler {

    /**
     * 在方法上使用@ExceptionHandler来指定异常类.class。
     * 表示该方法专门用于处理这个异常
     * 可以有以下三个参数
     */
    @ExceptionHandler(exceptionClass = {NullPointerException.class})
    public void nullHandler(HttpServletRequest request, HttpServletResponse response,Exception e){

    }
    
    @ExceptionHandler(exceptionClass = NumberFormatException.class)
    public void numberHandler(){
        
    }
}
```

##### 应用内部异常

如果本次`HTTP`请求没有被处理，那么就会被应用异常处理器捕获。默认情况下，会向客户端返回`404`的错误信息。如果有需求需要修改，框架也为我们提供好了指定的接口

```java
/**
 * 实现 SpringWebApplicationExceptionHandler 即可
 */
@Component
public class MyWebApplicationExceptionHandler implements SpringWebApplicationExceptionHandler {
    @Override
    public void exception(HttpServletRequest request, HttpServletResponse response, Throwable exception, int errorCode) {

    }
}
```

#### 静态资源

**框架提供了对于静态资源的管理与访问，可以更简易的管理所有的静态资源文件。**

**默认情况下，状态资源的存放路径在`/resources/static`文件夹中，可以将静态资源放在里面。客户端在访问该静态资源时，会从这里面读取，然后返回给客户端**

**在实际开发过程中，建议将`spring.web.resources.location`设置为外部存储路径，如果采用默认的，有新的图片添加时，也无法实时更新。**

**框架提供了以下配置：**

```properties
#静态资源访问前缀
spring.web.resources.prefix=
#是否允许访问静态资源
spring.web.resources.enable=
#静态资源存储路径
spring.web.resources.location=
```

## 7、Security

框架提供了一系列对于`Controller`接口的数据解密策略。可以更加方便的直接使用加密与解密，保证数据的安全

**注意：目前为止，`Security`只提供了对于`POST`请求的`application/json`的数据加密与解密功能**

**支持以下加密策略：**

- 非对称加密：
  - **值得注意的是，如果使用的是非对称加密，那么服务端无论是加密或者解密都是由私钥来做。而客户端只需要使用公钥来解密和加密即可**
  - `RSA(SpringSecurityRSAEncryptor)`
- 对称加密：
  - `AES（SpringSecurityRSAEncryptor）`

### 使用

```java
/**
 * 在启动类上标志 @EnableSpringSecurity 表示开启SpringSecurity
 */
@EnableSpringSecurity
@SpringBootApplication
public class API {
    public static void main(String[] args) {
        SpringApplication.run(API.class, args);
    }
}
```

```java
@Configuration
public class SpringConfiguration {
    /**
     * 向容器中注入加密策略
     */
    @Bean
    public SpringSecurityEncryptor springSecurityEncryptor(){
        //采用RSA非对称加密策略
        return new SpringSecurityRSAEncryptor();
    }
}
```

```properties
#配置公钥与秘钥（如果是对称加密，则只需要配置公钥即可）
#公钥配置
spring.security.data.public-key=
#私钥配置
spring.security.data.private-key=
#配置秘钥生成长度。如果没有配置公钥与秘钥，那么在启动时会自动生成指定长度的秘钥
spring.security.data.initialize=
#配置秘钥生成的盐（防止重复秘钥）
spring.security.data.salt=
```

```java
@Controller(prefix = "/user")
public class UserController {

    /**
     * 在开启Security后，所有的 POST application/json 请求都会被拦截解密
     */
    @RequestMapping(value = "/login",method = HttpRequestMethod.POST)
    public void login(@RequestBody Map<String,Object> map){
        ......
    }

    /**
     * 如果不需要被Security解密的操作，则在方法上使用@SecurityIgnore表示忽略该请求
     */
    @SecurityIgnore
    @RequestMapping("/getInfo")
    public Object getInfo(@RequestBody User user){

    }

}
```

# 3、框架开发

## Application

`Application`是一个全局的工具类，在该类中包含`框架版本、ApplicationContext、启动类信息等`。

## ApplicationContext

`ApplicationContext`是一个上下文对象，该对象可以获取`Spring工厂`，工厂列表：

- `BeanFactory`：ICO容器。管理所有Bean
- `ProfileFactory`：配置文件工厂。可以添加配置项、获取配置项
- `SpringWebFactory`：Web工厂。管理所有的Web相关的组件（Web拦截器、Servlet映射工厂）。

## 核心接口

### SpringBeanClassLoaderHandler

**类加载接口**。框架会将扫描到的所有`class`交给该接口实例处理。为此框架还提供了一个`@AutoConfig`注解，在扫描阶段，会最先加载`@AutoConfig`的类。所以，在实现该接口后，需要在类上标记`@AutoConfig`，优先加载到IOC中才可以去处理其他的class。

值得注意的是，框架内部其实还包含`SpringBeanCandidateBeforeClassHandler`与`SpringBeanCandidateAfterClassHandler`接口，但是这两个接口并不常用。都是在类加载结束后执行。这两个接口相当于对于`SpringBeanClassLoaderHandler`来做作为一个候补接口。用于在后期判断是否需要该class注入到Bean中，例如框架内部的`@Conditional与@ConditionalOnMissingBean`注解，都是实现这两个接口来进行处理逻辑。@Conditional是需要在所有类加载完后，判断IOC容器中是否包含指定Bean才向容器中注入新Bean，所有需要实现`SpringBeanCandidateAfterClassHandler`，而`@ConditionalOnMissingBean`需要在所有类加载完后，判断IOC中是否不包含指定Bean，才注入到Bean中，所以实现`SpringBeanCandidateBeforeClassHandler`接口。

总而言之，这两个接口并不只是针对这两个注解进行服务，而是如果日后有需求，在有一定条件的情况下才进行Bean处理获取其他逻辑处理，可以考虑实现这两个接口

```java
/**
 * 自定义类加载器。使用
 */
@AutoConfig
public class MyBeanClassLoaderHandler implements SpringBeanClassLoaderHandler {
    @Override
    public boolean handler(Class<?> targetClass) {
        //可以获取类上的注解。校验该类是否需要加载到IOC容器中...
        return false;
    }
}
```

### InitializingBean

**Bean初始化接口**。如果当前Bean需要进行一切初始化操作，则实现该接口即可。在类加载完后，会调用`init`方法对Bean进行初始化

```java
@Component
public class MyBean implements InitializingBean {
    @Override
    public void init() {
        //Bean在被加载到IOC容器中，会调用init方法进行初始化操作
    }
}
```

### SpringBeanClassHandler

**Bean处理器**。对所有在`IOC中的Bean`进行处理。

### SpringBeanFieldHandler

**Bean字段处理器**。对所有在`IOC中的Bean`的字段进行处理。

### SpringBeanMethodHandler

**Bean方法处理器**。对所有在`IOC中的Bean`的方法进行处理。

### SpringFileHandler

文件处理器，如果扫描到的文件不是一个`.class`，则会教给该处理器进行处理