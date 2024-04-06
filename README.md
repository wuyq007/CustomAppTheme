# 自定义文字大小主题

### 用法：自定义字体的枚举类型，用label区分不同主题的资源名称

```
// 这里的 label 就是自定义主题资源名称的后缀名
enum class AppTextSizeEnum(val label: String) {  
    Small("_small"), //小号  
    Normal(""),      //常规  
    Large("_large"), //大号  
    Huge("_huge"),   //超大  
}
```

### res/values 中定义各个主题对应的文字大小资源

> [!NOTE]
> ![](https://github.com/wuyq007/CustomAppTheme/blob/main/static/images/image-1.png)
>
> 原textSize的资源名称
> ![](https://github.com/wuyq007/CustomAppTheme/blob/main/static/images/image-2.png)
>
> 自定义的主题的资源名称 = 原资源名称 + 自定义主题后缀
>
> 原资源使用的是 "app_text_title_size"，设置成 Large 时会转为 "app_text_title_size_large"
> ![](https://github.com/wuyq007/CustomAppTheme/blob/main/static/images/image-3.png)


### 初始化字体大小

```
// 一般在 Applocation 中初始化。可以传入持久化保存的字体大小
AppCustomThemeUtils.initCustomTextSize(AppTextSizeEnum.Normal)
```

### Activity 中 或者 Dialog 中

```
//必须在 setContentView() 之前调用
AppCustomThemeUtils.setLayoutInflaterFactory(layoutInflater)
//绑定监听，会在改变字体大小时刷新当前页面
AppCustomThemeUtils.bindingTextSizeChange(this)
```

### 布局文件：TextView中设置 textSize 为指定资源，并且开启 customTextSizeEnabled = true

```
//customTextSizeEnabled 为自定义属性 
<declare-styleable name="CustomChangeTextView">
    <attr name="customTextSizeEnabled" format="boolean" />
</declare-styleable>

//两个属性都设置才生效
<TextView
   android:textSize="@dimen/app_text_title_size"
   app:customTextSizeEnabled="true" />
```


### 刷新当前页面文字大小

```
//这里主要是刷新已存在页面的textSize, LayoutInflaterFactory仅在页面创建前可以设置一次，这里通过遍历所有 TextView 刷新 textSize
AppCustomThemeUtils.refreshViewTextSize(activity, AppTextSizeEnum)
```

### 保存设置，并通知所有的页面刷新字体大小

```
//这里可以设置持久化存储，以便下次启动app，使设置的自定义主题生效
AppCustomThemeUtils.saveAppTextSize(showTextSizeValue)
```


> [!NOTE] 
> ### 混淆
> 刷新页面时，通过反射重置了 AbsListView 和 RecyclerView 的缓存
```
-keepnames class android.widget.AbsListView{
    android.widget.AbsListView$RecycleBin mRecycler;
}

-keepnames class android.widget.AbsListView$RecycleBin{
    void clear();
}

-keepnames class androidx.recyclerview.widget.RecyclerView{
    androidx.recyclerview.widget.RecyclerView$Recycler mRecycler;
}

-keepnames class androidx.recyclerview.widget.RecyclerView$Recycler{
    public void clear();
}
```