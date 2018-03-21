# XCache 介绍
XCache框架中分为 **FileCache、AppCache**
FileCache 是文件存储，向指定文件目录中缓存
AppCache 是 SharedPreferences 文件缓存工具

##  FileCache功能
### File 存储
1. Strinng 类型存储，String 类型存储，基本能满足很多情况，例如数据 Bean 类、JSON、以及其他基本类型
2. Bitmap 类型存储，Bitmap 存储在 Sdcard 中在 Android 中已经非常常见，也是必须存在的功能。

#### 构造函数

主要参数为

 1. File 缓存目录。
 2. AppVersion 当前 App 版本号
 3. MaxSize 目录缓存最大容量
 

```
 private XCache(File fileDir, int appVersion, long maxSize)
```

#### 方法
1. String 类型存储
    
    ```
    # 存储 String 类型
    void put(String key, String value);
    
    # svaeTime 最大存储时间 秒为单位
    void put(String key, String value, int saveTime);
    ```
2. Bitmap 类型存储

    ```
    void put(String key, Bitmap bitmap);
    # svaeTime 最大存储时间 秒为单位
    void put(String key, Bitmap bitmap, int saveTime);
    # 以 Lru 算法存储 Bitmap
    void putLruBitmap(String key, Bitmap bitmap);
    ```
3. 获取 String 类型数据

    ```
    String getString(String key);
    ```
4. 获取 Bitmaple 类型数据

    ```
    Bitmap getBitmap(String key);
    # 获取 Lru 算法中的 Bitmap
    Bitmap getLruBitmap(String key);
    ```
5. 清理指定缓存

    ```
    boolean remove(String key);
    # 移除 Lru 算法中数据
    boolean removeLru(String key);
    ```
6. 清理全部缓存

    ```
    boolean clear();
    
    # 清理 Lru 算法中的全部数据
    void clearLru();
    ```


## AppCache 功能
### SharedPreferences 存储
1. 类型写入包含 String、int、long、float、boolean
2. 删除功能包含 指定删除、全部删除

### 构造方法
主要参数

1. 缓存名称
2. 上下文 Context

```
 private XAppCache(Context context, String name)
```

### 方法

```
# 存储数据
IAppCache putString(String key, String value);

IAppCache putInt(String key, int value);

IAppCache putLong(String key, long value);

IAppCache putFloat(String key, float value);

IAppCache putBoolean(String key, boolean value);

# 清除数据
IAppCache remove(String key);

IAppCache clear();

# 获取数据
String getStringValue(String key, String defaultValue);

int getIntValue(String key, int defaultValue);

long getLongValue(String key, long defaultValue);

float getFloatValue(String key, float defaultValue);

boolean getBooleanValue(String key, boolean defaultValue);

# 提交
void apply(SharedPreferences.Editor editor);

void apply();
```

## AppDemo 功能
### FileCache 功能

1. String 类型写入、读取
2. Bitmap 类型写入、读取展示

### AppCache 功能

1. String 类型写入、读取
2. Bitmap 类型写入、读取展示



   
    
     
    
    



