package cn.yangeit.config;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //存入到threadlocal中
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    //从threadlocal中获取
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    //从threadlocal中删除
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
