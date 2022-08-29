## c基础

### 格式化字符串

- 8进制
  - %o
- 16进制			
  - 小写： %x 
  - 大写：%X
- (0x)+16进制前面 	
  - %#x 

sprintf:

​	将格式化的数据写入第一个参数

```c
#include <stdio.h>
char str[100];
sprintf(str, "img/png_%d.png", 1);
printf("%s", str);

//使用 0 补到3个字符
sprintf(str, "img/png_%03d.png", 1);
printf("%s", str);
```

### 动态内存申请

- [C 库函数 - malloc()](https://www.runoob.com/cprogramming/c-function-malloc.html)
  - 分配所需的内存空间，并返回一个指向它的指针。
  - 没有初始化内存的内容，一般调用函数memset来初始化这部分的内存空间.

- [C 库函数 - calloc()](https://www.runoob.com/cprogramming/c-function-calloc.html)
  - 分配所需的内存空间，并返回一个指向它的指针。
  - 申请内存并将初始化内存数据为NULL.
  - ` int *pn = (int*)calloc(10, sizeof(int));`
  - **malloc** 和 **calloc** 之间的不同点是，malloc 不会设置内存为零，而 calloc 会设置分配的内存为零。

- [C 库函数 - realloc()](https://www.runoob.com/cprogramming/c-function-realloc.html)

  - 尝试重新调整之前调用 **malloc** 或 **calloc** 所分配的 **ptr** 所指向的内存块的大小。

  - ```c
    char *a = (char*)malloc(10);
    realloc(a,20);
    ```

- [C 库函数 - free()](https://www.runoob.com/cprogramming/c-function-free.html)

  - C 库函数 **void free(void \*ptr)** 释放之前调用 calloc、malloc 或 realloc 所分配的内存空间。

  - ```c
    //一定要free 并养成好习惯，并将指针置为 null
    //标准写法为：
    if (di1) {
    	free(di1);
    	di1 = 0;
    }
    ```

- 特别的：**alloca**

  - 在栈申请内存,因此无需释放.
  - `int *p = (int *)alloca(sizeof(int) * 10);`

### 代码

```c
int main()
{
// 基本数据类型 (从与java差异性来看)
//============================================================	 

	 //signed int = int
	 signed int i1 = 1;
	 //无符号 取值范围不同  (推荐使用 uint32_t )
	 unsigned int i2 = 1;
	 // long 就是 long int
	 long l1 = 1L;
	 // long long 才是java的long (推荐使用 int64_t )
	 // C99 stdbool.h中定义了 bool 
	 //非0为true ， null就是 0 

// 格式化 
//============================================================	 
	 //见资料中的笔记   String.format("%d",xx)
	 printf("%u\n",i2);
	 //常用 sprintf 来格式化字符串
	 char str[100];
	 for (size_t i = 0; i < 3; i++)
	 {
		 //讲 2、3参数 格式化的 字符 复制到 str 中
		 sprintf(str,"D:/Lance/ndk/lsn1_c/资料/新建文件夹/%d.txt",i);
		 //  \n 换行
		 printf("%s\n", str);
	 }

// 数组声明
//============================================================	 	 
	 // c当中 定义数组 必须指明数组长度 或者 声明与赋值写在一起
	 // 连续的内存 int=4*6 = 24 字节 栈内存
	 int array1[6];
	 int array2[] = { 1,2,3,4,5,6 };
	
	 //当数据无法确定 或者 比较庞大 需要使用动态内存申请 在堆中
	 int *di1 = (int*)malloc(1 * 1024 * 1024);
	 //动态申请内存应该紧跟 memset 初始化内存
	 memset(di1, 0, 1 * 1024 * 1024);

	 // 申请内存并将内存初始化为 null 
	 int *di2 = (int*)calloc(10, sizeof(int));

	 // 对malloc申请的内存进行大小的调整
	 realloc(di1, 20 * sizeof(int));
	 
	//一定要free 并养成好习惯 将指针置为 null
	 //标准写法为：
	 if (di1) {
		 free(di1);
		 di1 = 0;
	 }
	 if (di2) {
		 free(di2);
		 di2 = 0;
	 }
	
	
	system("pause");
	return 0;
}
```

### 指针

> 指针是一个值为地址的变量。
>
> 声明指针或者不再使用后都要将其置为0 (NULL)
>
> 野指针：未初始化的指针，例如：int *a;
>
> 悬空指针：指针最初指向的内存已经被释放了的一种指针

```c
//声明一个整型变量
int i = 10;
//将i的地址使用取地址符给p指针
int *p = &i;
//输出 0xffff 16进制地址
printf("i的地址：%#x\n", &i);
printf("p的地址：%#x\n", &p);
printf("p的值：%#x\n", p);
//*加上一个指针变量，表示解引用（解析地址的值），这里拿到的是i指向地址的值
//解引用：根据一个内存，解析出这个内存地址存储的数据
printf("p指向地址的值：%d\n", *p);
//指针多少个字节？指向地址，存放的是地址。32位上：占用4个字节；64位上：占用8个字节
printf("指针变量p所占用的字节数：%d\n", sizeof(p));
//为解引用的结果赋值也就是为指针所指的内存赋值；修改地址的值,则i值也变成100
*p = 100;
printf("经过给解引用的结果赋值后i的值为：%d\n", i);
```

> 上述代码输出：
>
> i的地址：0xff77fa64
> p的地址：0xff77fa88
> p的值：0xff77fa64
> p指向地址的值：10
> 指针变量p所占用的字节数：8
> 经过给解引用的结果赋值后i的值为：100

- 数组和指针

  - [C 指针数组](https://www.runoob.com/cprogramming/c-array-of-pointers.html)
  - 指针数组
  - 指针运算

- const char *, char const *, char * const，char const * const 

  ```c
  //从右往左读
  //P是一个指针 指向 const char类型
  char str[] = "hello";
  const char *p = str;
  str[0] = 'c'; //正确
  p[0] = 'c';   //错误 不能通过指针修改 const char
  
  //可以修改指针指向的数据 
  //意思就是 p 原来 指向david,
  //不能修改david爱去天之道的属性，
  //但是可以让p指向lance，lance不去天之道的。
  p = "12345";
  
  //性质和 const char * 一样
  char const *p1;
  
  //p2是一个const指针 指向char类型数据
  char * const p2 = str;
  p2[0] = 'd';  //正确
  p2 = "12345"; //错误
  
  //p3是一个const的指针变量 意味着不能修改它的指向
  //同时指向一个 const char 类型 意味着不能修改它指向的字符
  //集合了 const char * 与  char * const
  char const* const p3 = str;
  ```

- 多级指针

  - 多级指针的意义：见函数部分的引用传值



### 函数

#### 函数的参数

有两种向函数传递参数的方式：传值调用和引用调用

- 传值调用：把参数的值复制给函数的形式参数。修改形参不会影响实参
  - [C 传值方式调用函数](https://www.runoob.com/cprogramming/c-function-call-by-value.html)
- 引用调用：形参为指向实参地址的指针，可以通过指针修改实参。
  - 传递指针可以让多个函数访问指针所引用的对象，而不用把对象声明为全局可访问。
  - [C 引用方式调用函数](https://www.runoob.com/cprogramming/c-function-call-by-pointer.html)

```c
void change1(int i) {
	i = 10;
}

void change2(int* i) {
	*i = 10;
}

void change3(int** i) {
	int j = 100;
	*i = &j;
}
int main()
{
//函数形参
	//1、传值	
	//把参数的值给函数
	int i3 = 1;
	change1(i3);
	printf("change1 i3=%d\n", i3);
	//2、传引用
	//不修改指针的值，修改的是指针指向内存的数据
	int *p9 = &i3;
	//修改i3的值
	change2(p9);
	printf("change2 i3=%d\n",i3);

	
	//p9这个变量的地址给 p10
	int **p10 = &p9;
	//修改指针的值 p9的值 0xXXXXX 地址
	change3(&p9);
    system("pause");
    return 0;
}
```

还有一个例子：

```c
#include <stdio.h>
 
/* 函数定义 */
void swap(int *x, int *y)
{
   int temp;
   temp = *x;    /* 保存地址 x 的值 */
   *x = *y;      /* 把 y 赋值给 x */
   *y = temp;    /* 把 temp 赋值给 y */
  
   return;
}
 
int main ()
{
   /* 局部变量定义 */
   int a = 100;
   int b = 200;
 
   printf("交换前，a 的值： %d\n", a );
   printf("交换前，b 的值： %d\n", b );
 
   /* 调用函数来交换值
    * &a 表示指向 a 的指针，即变量 a 的地址
    * &b 表示指向 b 的指针，即变量 b 的地址
   */
   swap(&a, &b);
 
   printf("交换后，a 的值： %d\n", a );
   printf("交换后，b 的值： %d\n", b );
 
   return 0;
}
```

结果：

```shell
交换前，a 的值： 100
交换前，b 的值： 200
交换后，a 的值： 200
交换后，b 的值： 100
```

#### [可变参数](https://www.runoob.com/cprogramming/c-variable-arguments.html)

```c
#include <stdarg.h>
int add(int num, ...)
{
	va_list valist;
	int sum = 0;
	// 初始化  valist指向第一个可变参数 (...)
	va_start(valist, num);
	for (size_t i = 0; i < num; i++)
	{
		//访问所有赋给 valist 的参数
		int j = va_arg(valist, int);
		printf("%d\n", j);
		sum += j;
	}
	//清理为 valist 内存
	va_end(valist);
	return sum;
}
```

#### [函数指针](https://www.runoob.com/cprogramming/c-fun-pointer-callback.html)

typedef int (*fun_ptr)(int,int); //声明一个指向同样参数、返回值的函数指针类型

常见使用方法：回调



### 预处理器

### 结构体

#### 	字节对齐

- 合理的利用字节可以有效地节省存储空间
- 不合理的则会浪费空间、降低效率甚至还会引发错误。(对于部分系统从奇地址访问int、short等数据会导致错误)

​		

### **共用体**

是一种特殊的数据类型，允许您在相同的内存位置存储不同的数据类型。您可以定义一个带有多成员的共用体，但是任何时候只能有一个成员带有值。共用体提供了一种使用相同的内存位置的有效方式。



## C++

### [C++引用](https://www.runoob.com/cplusplus/cpp-references.html)

```c
//声明形参为引用
void change(int& i) {
	i = 10;
}
int i = 1;
change(i);
printf("%d\n",i); //i == 10
```

引用和指针是两个东西

引用 ：变量名是附加在内存位置中的一个标签,可以设置第二个标签

简单来说 引用变量是一个别名，表示一个变量的另一个名字

### [C++ 字符串](https://www.runoob.com/cplusplus/cpp-strings.html)

#### C字符串

> 字符串实际上是使用 NULL字符 `'\0' `终止的一维字符数组。

```c
//字符数组 = 字符串
char cstr1[6] = {'H', 'e', 'l', 'l', 'o', '\0'};
//自动加入\0
char cstr2[] = "Hello";
```

#### C++ string类

```c
#include <string>
//string 定义在 std命名空间中
usning namespace std;
string str1 = "Hello";
string str2 = "World";
string str3("天之道");
string str4(str3);
//申请内存，会调用构造方法
//使用这种方式（指针）的好处：
//1、出方法，就清理栈内存，而这种是在堆当中
//2、指针在32位架构占4个字节，64位占8个字节。作为参数，传递起来效率更高。
string *str_new = new string;
//也可以new的时候赋值
string *str_new2= new string("new处理的字符串");
//指针的形式调用string的成员函数需要使用->
str_new->size();
str_new->c_str();
//或者使用如下的形式
const char *u = (*str_new).c_str();
//释放内存，使用new申请的内存，要使用delete释放
//使用malloc申请的内存，使用free释放
//使用new[]申请的数组，使用delete []释放内存
delete str_new;
delete str_new2;
	
// str1拼接str2 组合新的string
//+ 其实是操作符重载：操作符重载就是重新定义+, -, *, /等这些运算符的行为
string str5 = str1 + str2;
// 在str1后拼接str2 str1改变
str1.append(str2);
//获得c 风格字符串
const char *s1 = str1.c_str();
//错误，编译不通过，java当中输出一个对象，默认会为我们调用toString()方法，这里不会。需要转成c风格的字符串：str1.c_str()
cout << str1 << endl;
//字符串长度
str1.size();
//长度是否为0
str1.empty();
......等等
```



### 命名空间



### C++面向对象

- 析构函数：类的析构函数是类的一种特殊的成员函数，它会在每次删除所创建的对象时执行(不需要手动调用)。

- 常量函数：函数后写上const，表示不会也不允许修改类中的成员。

- 友元函数

  - 类的友元函数是定义在类外部，但有权访问类的所有私有（private）成员和保护（protected）成员
  - 友元可以是一个函数，该函数被称为友元函数；友元也可以是一个类，该类被称为友元类，在这种情况下，整个类及其所有成员都是友元。

- 静态成员

  - 和JAVA一样，可以使用static来声明类成员为静态的

  - 当我们使用静态成员属性或者函数时候 需要使用 域运算符 :: 

    ```c++
    //Instance.h
    #ifndef INSTANCE_H
    #define INSTANCE_H
    class Instance {
    public:
    	static Instance* getInstance();
    private:
    	static Instance *instance;
    };
    #endif 
    
    //Instance.cpp
    #include "Instance.h"
    Instance* Instance::instance = 0;
    Instance* Instance::getInstance() {
    	//C++11以后，编译器会保证内部静态变量的线程安全性
    	if (!instance) {
    		instance = new Instance;
    	}
    	return instance;
    }
    ```

- [重载运算符和重载函数](https://www.runoob.com/cplusplus/cpp-overloading.html)

  - C++允许重定义或重载大部分 C++ 内置的运算符，这是java所没有的

- 拷贝构造函数

  - 深拷贝：拷贝的是指针指向的数据内容
  - 浅拷贝：拷贝的是指针的地址

- 继承

  - class A:[private/protected/public] B
    - 可以指定作用域，不指定则默认为private继承 
    - B是基类，A称为子类或者派生类 
  - 一个子类可以有多个父类，它继承了多个父类的特性。
    - class <派生类名>:<继承方式1><基类名1>,<继承方式2><基类名2>,…

- 多态

  - 多种形态。当类之间存在层次结构，并且类之间是通过继承关联时，就会用到多态。

  - 静态多态（静态联编）是指在编译期间就可以确定函数的调用地址，通过**函数重载**和**模版（泛型编程）**实现 

  - 动态多态（动态联编）是指函数调用的地址不能在编译器期间确定，必须需要在运行时才确定 ,通过**继承+虚函数** 实现

  - 虚函数

    - **虚函数** 是在基类中使用关键字 **virtual** 声明的函数。在派生类中重新定义基类中定义的虚函数时，会告诉编译器不要静态链接到该函数。

      我们想要的是在程序中任意点可以根据所调用的对象类型来选择调用的函数，这种操作被称为**动态链接**，或**后期绑定**。

  - 纯虚函数

    - 您可能想要在基类中定义虚函数，以便在派生类中重新定义该函数更好地适用于对象，但是您在基类中又不能对虚函数给出有意义的实现，这个时候就会用到纯虚函数。

      ```c++
      // pure virtual function
      // 纯虚函数 继承自这个类需要实现 抽象类型，类似于java中的抽象方法
      virtual int area() = 0;
      ```

  - 构造函数任何时候都不可以声明为虚函数

    析构函数一般都是虚函数,释放先执行子类再执行父类。如果不用虚函数，会调用父类的析构函数，那么子类申请的内存就得不到释放。

- 模板

  - 模板是泛型编程的基础，泛型编程即以一种独立于任何特定类型的方式编写代码。
  - 函数模板
  - 类模板

```c++
//调用构造方法 栈
//出方法释放student 调用析构方法
Student student(1,2,3);
//动态内存(堆)
// new关键字表示在堆中申请内存，并且对这块内存进行了类型定义，将类型定义成了一个Student类，用Student指针来接收这一块内存
Student *student = new Student(1,2,3);
//指针的形式调用成员函数需要使用->
student->setName("Lance");
//释放
delete student;
student = 0;
```

### 类型转换

**[C++中的四种转型操作符](http://blog.csdn.net/fayery/article/details/38468277)**

- static_cast

  - 基础类型之间互转。如：float转成int、int转成unsigned int等

  - 指针与void之间互转。如：float\*转成void\*、Bean\*转成void\*、函数指针转成void\*等

  - 子类指针/引用与 父类指针/引用 转换。

    ```c++
    class Parent {
    public:
    	void test() {
    		cout << "p" << endl;
    	}
    };
    class Child :public Parent{
    public:
    	 void test() {
    		cout << "c" << endl;
    	}
    };
    Parent  *p = new Parent;
    Child  *c = static_cast<Child*>(p);
    //输出c
    c->test();
    
    //Parent test加上 virtual 输出 p
    ```

    

- const_cast

  - 修改类型的const或volatile属性 

    ```c++
    const char *a;
    char *b = const_cast<char*>(a);
    	
    char *a;
    const char *b = const_cast<const char*>(a);
    ```

    

- dynamic_cast

  - 主要 将基类指针、引用 安全地转为派生类.

  - 在运行期对可疑的转型操作进行安全检查，仅对多态有效

    ```c++
    //基类至少有一个虚函数
    //对指针转换失败的得到NULL，对引用失败  抛出bad_cast异常 
    Parent  *p = new Parent;
    Child  *c = dynamic_cast<Child*>(p);
    if (!c) {
    	cout << "转换失败" << endl;
    }
    
    
    Parent  *p = new Child;
    Child  *c = dynamic_cast<Child*>(p);
    if (c) {
    	cout << "转换成功" << endl;
    }
    ```

    

- reinterpret_cast 

  - 对指针、引用进行原始转换

    ```c++
    float i = 10;
    
    //&i float指针，指向一个地址，转换为int类型，j就是这个地址
    int j = reinterpret_cast<int>(&i);
    cout  << hex << &i << endl;
    cout  << hex  << j << endl;
    
    cout<<hex<<i<<endl; //输出十六进制数
    cout<<oct<<i<<endl; //输出八进制数
    cout<<dec<<i<<endl; //输出十进制数
    ```

- char*与int转换

  - ```c++
    //char* 转int float
    int i = atoi("1");
    float f = atof("1.1f");
    cout << i << endl;
    cout << f << endl;
    	
    //int 转 char*
    char c[10];
    //10进制
    itoa(100, c,10);
    cout << c << endl;
    
    //int 转 char*
    char c1[10];
    sprintf(c1, "%d", 100);
    cout << c1 << endl;
    ```

    

### [C++ 动态内存](https://www.runoob.com/cplusplus/cpp-dynamic-memory.html)

### [C++异常处理](https://www.runoob.com/cplusplus/cpp-exceptions-handling.html)

### [文件与流操作](https://www.runoob.com/cplusplus/cpp-files-streams.html)

### [C++ 信号处理](https://www.runoob.com/cplusplus/cpp-signal-handling.html)

### 容器

#### 序列式容器

- vector  支持快速随机访问  
- list  支持快速插入、删除  
- deque  双端队列  允许两端都可以进行入队和出队操作的队列  
- stack  后进先出LIFO(Last In First Out)堆栈  
- queue  先进先出FIFO(First Input First Output)队列  
- priority_queue  有优先级管理的queue

#### 关联式容器

- set

- map

  - unordered_map c++11取代hash_map（哈希表实现，无序）

    哈希表实现查找速度会比RB树实现快,但rb整体更节省内存

    需要无序容器，高频快速查找删除，数据量较大用unordered_map；

    需要有序容器，查找删除频率稳定，在意内存时用map。

- 红黑树

  - [红黑树维基百科](https://zh.wikipedia.org/wiki/%E7%BA%A2%E9%BB%91%E6%A0%91)

### [多线程](https://www.runoob.com/cplusplus/cpp-multithreading.html)

#### 线程属性

#### 分离线程

#### 调度策略与优先级

#### 线程同步

##### 	加入互斥锁

##### 	条件变量

> 条件变量是线程间进行同步的一种机制，主要包括两个动作：一个线程等待"条件变量的条件成立"而挂起；另一个线程使"条件成立",从而唤醒挂起线程

```c++
template <class T>
class SafeQueue {
public:
	SafeQueue() {
		pthread_mutex_init(&mutex,0);
	}
	~SafeQueue() {
		pthread_mutex_destory(&mutex);
	}
  //生产 加入数据
	void enqueue(T t) {
		pthread_mutex_lock(&mutex);
		q.push(t);
		pthread_mutex_unlock(&mutex);
	}
  //消费 取数据
	int dequeue(T& t) {
		pthread_mutex_lock(&mutex);
		if (!q.empty())
		{
			t = q.front();
			q.pop();
			pthread_mutex_unlock(&mutex);
			return 1;
		}
		pthread_mutex_unlock(&mutex);
		return 0;
	}

private:
	queue<T> q;
	pthread_mutex_t mutex;
};
```

上面的模板类存放数据T，并使用互斥锁保证对queue的操作是线程安全的。这就是一个生产/消费模式。

如果在取出数据的时候，queue为空，则一直等待，直到下一次enqueue加入数据。

这就是一个典型的生产/消费模式, 加入条件变量使 “dequeue”  挂起,直到由其他地方唤醒

```c++
#pragma once
#include <queue>
using namespace std;

template <class T>
class SafeQueue {
public:
	SafeQueue() {
		pthread_mutex_init(&mutex,0);
		pthread_cond_init(&cond, 0);
	}
	~SafeQueue() {
		pthread_mutex_destory(&mutex);
		pthread_cond_destory(&cond);
	}
	void enqueue(T t) {
		pthread_mutex_lock(&mutex);
		q.push(t);
		//发出信号 通知挂起线程，两种方式
		//方式一：由系统唤醒一个线程，相当于notify()
		//pthread_cond_signal(&cond);
		//方式二：广播 对应多个消费者的时候 多个线程等待唤醒所有，相当于notifyAll()
		pthread_cond_broadcast(&cond);
		pthread_mutex_unlock(&mutex);
	}
	int dequeue(T& t) {
		pthread_mutex_lock(&mutex);
		//可能被意外唤醒 所以while循环
		while (q.empty())
		{
      //使CPU处于挂起状态，而不是一直在运行的状态，释放锁
			pthread_cond_wait(&cond, &mutex);
		}
		t = q.front();
		q.pop();
		pthread_mutex_unlock(&mutex);
		return 1;
	}

private:
	queue<T> q;
	pthread_mutex_t mutex;
	pthread_cond_t cond;
};
```

```c++
#include "lsn6_example.h"
#include <thread>
#include <pthread.h>

using namespace std;
#include "safe_queue.h"

SafeQueue<int> q;

void *get(void* args) {
	while (1) {
		int i;
		q.dequeue(i);
		cout << "消费:"<< i << endl;
	}
	return 0;
}
void *put(void* args) {
	while (1)
	{
		int i;
		cin >> i;
		q.enqueue(i);
	}
	return 0;
}
int main()
{
	pthread_t pid1, pid2;
	pthread_create(&pid1, 0, get, &q);
	pthread_create(&pid2, 0, put, &q);
	pthread_join(pid2,0);
	system("pause");
	return 0;
}
```



### 智能指针

#### 	shared_ptr

#### 	weak_ptr

#### 	unique_ptr

### 部分C++11,、14特性

#### 	nullptr

#### 	类型推导

#### 	foreach

#### 	lambda表达式















## 配置Android FFMPEG开发环境

### 编译

### 配置CMakeLists

> #### 子项目CMake文件
>
> ```cmake
> # CMakeList.txt: ffmpeg_example 的 CMake 项目，包括源和定义
> # 此处特定于项目的逻辑。
> cmake_minimum_required (VERSION 3.8)
> 
> #能够在 编码的时候 使用ffmpeg的函数
> # 设置头文件的查找位置
> include_directories("D:/Lance/ndk/lsn3_c/ffmpeg-20180813-551a029-win64-dev/include")
> 
> # 设置库文件的查找目录
> link_directories("D:/Lance/ndk/lsn3_c/ffmpeg-20180813-551a029-win64-dev/lib")
> 
> # 将源添加到此项目的可执行文件。会生成ffmpegexample.exe文件
> add_executable (ffmpegexample "ffmpeg_example.cpp" "ffmpeg_example.h")
> 
> #设置编译链接的库：ffmpeg库，在库文件的查找目录中。在编译的时候需要把可执行文件ffmpegexample链接到一起
> target_link_libraries( ffmpegexample  avutil avcodec avfilter  avdevice avformat postproc swresample )
> ```
>
> 
>
> #### 顶级CMake项目文件
>
> ```cmake
> # CMakeList.txt : 顶级 CMake 项目文件，执行全局配置和此处的包含子项目。
> cmake_minimum_required (VERSION 3.8)
> project ("ffmpeg_example")
> # 包含子项目。
> add_subdirectory ("ffmpeg_example")
> ```
>