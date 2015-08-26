关于启动Pick Contacts的Activity在
http://code.google.com/android/reference/android/app/Activity.html
上的示例代码是错误的。

**错误代码是：**

```
startSubActivity(
                 new Intent(Intent.PICK_ACTION,
                 new Uri("content://contacts")),
                 PICK_CONTACT_REQUEST);

```

**正确代码如下：**
```
startSubActivity(
                 new Intent(Intent.PICK_ACTION,
                 Contacts.People.CONTENT_URI), 
                 PICK_CONTACT_REQUEST);
```

**参考**
http://groups.google.com/group/android-developers/browse_frm/thread/f140aeee50ca9e89/5c9a8f7e088db83a?lnk=gst&q=pick+contacts#5c9a8f7e088db83a