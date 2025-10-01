public class LoggerSingleton {
    private static LoggerSingleton instance;
    private LoggerSingleton(){}
    public static synchronized LoggerSingleton getInstance(){
        if(instance==null) instance = new LoggerSingleton();
        return instance;
    }
    public void log(String msg){ System.out.println("[Logger] " + msg); }
}